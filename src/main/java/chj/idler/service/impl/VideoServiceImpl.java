package chj.idler.service.impl;

import chj.idler.dao.EpisodeDOMapper;
import chj.idler.dao.UserDOMapper;
import chj.idler.dao.VideoDOMapper;
import chj.idler.dao.VideoSubDOMapper;
import chj.idler.dataobject.EpisodeDO;
import chj.idler.dataobject.VideoDO;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.rocketmq.MqProducer;
import chj.idler.service.VideoService;
import chj.idler.service.model.VideoModel;
import chj.idler.service.spider.iqiyi.IqiyiAllEpisodeProcessor;
import chj.idler.service.spider.iqiyi.IqiyiProcessor;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import chj.idler.service.spider.tencent.TcAllEpisodeProcessor;
import chj.idler.service.spider.tencent.TcNewestEpisodeProcessor;
import chj.idler.service.spider.tencent.TcProcessor;
import chj.idler.service.spider.tencent.TcVideoProcessor;

import chj.idler.util.JedisClusterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDOMapper videoDOMapper;
    @Autowired
    private EpisodeDOMapper episodeDOMapper;
    @Autowired
    private VideoSubDOMapper videoSubDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private MqProducer mqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoModel analyseURL(String url) throws BusinessException,JsonProcessingException,MQClientException,InterruptedException, RemotingException, MQBrokerException
    {
            VideoModel videoModel=null;
            //将来需要先从数据库爬取，新视频把爬取任务延时进行
            if (url.matches(TcProcessor.URL_WATCH)) {
                //缓存是否存在，url->videoModel
                videoModel=JedisClusterUtil.get(url,VideoModel.class);
                if(videoModel!=null)return videoModel;
                //
                TcVideoProcessor tcVideoProcessor = new TcVideoProcessor();
                Spider spider=Spider.create(tcVideoProcessor);
                spider.addUrl(url).thread(1).run();
                videoModel = tcVideoProcessor.getVideoModel();
                if (!selectExistVideo(videoModel)) {
                    Pattern idPat = Pattern.compile("^https://v\\.qq\\.com/detail/[\\w]/(.+)\\.html$");
                    Matcher id = idPat.matcher(videoModel.getDetailUrl());
                    if (id.find()) {
                        String episodeUrl = "https://s.video.qq.com/get_playsource?type=4&data_type=3&range=1-9999&id=" + id.group(1);
                        spider.addUrl(videoModel.getDetailUrl(), episodeUrl).thread(2).run();
                    }
                    //先插数据库后插缓存
                    try {
                        insertVideoModel(videoModel);
                    }catch (DuplicateKeyException e){
                        Thread.sleep(100L);
                        return analyseURL(url);
                    }
                     mqProducer.collectEpisodes(videoModel);
                }
                JedisClusterUtil.set(url,videoModel);
                return videoModel;
            }else if(url.matches(IqiyiProcessor.URL_WATCH)){
                //缓存是否存在，url->videoModel
                videoModel=JedisClusterUtil.get(url,VideoModel.class);
                if(videoModel!=null)return videoModel;
                //
                IqiyiVideoProcessor iqiyiVideoProcessor=new IqiyiVideoProcessor();
                Spider spider=Spider.create(iqiyiVideoProcessor);
                spider.addUrl(url).thread(1).run();
                videoModel=iqiyiVideoProcessor.getVideoModel();
                if(videoModel==null)throw new BusinessException(EmBusinessError.URL_NOT_SUPPORT);
                if(!selectExistVideo(videoModel)){
                    spider.addUrl(videoModel.getDetailUrl(), iqiyiVideoProcessor.getEpisodesUrl()).thread(1).run();
                    //先插数据库后插缓存
                    try {
                        insertVideoModel(videoModel);
                    }catch (DuplicateKeyException e){
                        Thread.sleep(100L);
                        return analyseURL(url);
                    }
                    //这里extra存的是爬取episodes的接口地址
                    videoModel.setExtra(iqiyiVideoProcessor.getEpisodesUrl());
//                    mqProducer.collectEpisodes(videoModel);
                }
//                JedisClusterUtil.set(url,videoModel);
            }
            return videoModel;
    }

    @Override
    public List<String> selectSubEmails(Integer videoId) {
        List<Integer> userIdList=videoSubDOMapper.selectSubVideoId(videoId);
        List<String> emailList=new ArrayList<>();
        for(Integer id:userIdList){
            emailList.add(userDOMapper.selectEmailById(id));
        }
        return emailList;
    }

    @Override
    public List<VideoModel> getVideoList(Integer userId)  {
        if(userId==null)return null;
        List<VideoModel> videoModelList=new ArrayList<>() ;
        //1、查用户订阅videoId
        List<Integer>videoIdList=videoSubDOMapper.selectVideoIdByUserId(userId);
        //2、对每个videoId查出video与对应的最新episode，并合成videoModel
        for(Integer i:videoIdList){
            VideoDO videoDO=videoDOMapper.selectByPrimaryKey(i);
            EpisodeDO episodeDO=episodeDOMapper.selectByVideoIdNewest(i);
            VideoModel videoModel=convertToVideoModel(videoDO,episodeDO);
            videoModel.setSub(videoSubDOMapper.selectSub(userId,i));
            videoModelList.add(videoModel);
    }
        return videoModelList;
    }
    public void episodesSpider(VideoModel videoModel){
        if(videoModel.getSource()==1) {
            TcAllEpisodeProcessor tcAllEpisodeProcessor = new TcAllEpisodeProcessor();
            Pattern idPat = Pattern.compile("^https://v\\.qq\\.com/detail/[\\w]/(.+)\\.html$");
            Matcher id = idPat.matcher(videoModel.getDetailUrl());
            if (id.find()) {
                String url = "https://s.video.qq.com/get_playsource?type=4&data_type=3&range=1-9999&id=" + id.group(1);
                Spider.create(tcAllEpisodeProcessor).addUrl(url).thread(1).run();
            }
            for (EpisodeDO episodeDO : tcAllEpisodeProcessor.getEpisodeDOList()) {
                episodeDO.setVideoId(videoModel.getId());
                episodeDOMapper.insertSelectiveDup(episodeDO);
            }
        }else if(videoModel.getSource()==2){
            IqiyiAllEpisodeProcessor iqiyiAllEpisodeProcessor=new IqiyiAllEpisodeProcessor();
            Spider.create(iqiyiAllEpisodeProcessor).addUrl((String)videoModel.getExtra()).thread(1).run();
            for (EpisodeDO episodeDO : iqiyiAllEpisodeProcessor.getEpisodeDOList()) {
                episodeDO.setVideoId(videoModel.getId());
                episodeDOMapper.insertSelectiveDup(episodeDO);
            }
        }
    }
    public void updateEpisodes()throws MQClientException,InterruptedException, RemotingException, MQBrokerException{
            Pattern idPat = Pattern.compile("^https://v\\.qq\\.com/detail/[\\w]/(.+)\\.html$");
            //video的标识对应episode的title
            Map<String, VideoModel> videoMap = new HashMap<>();
            TcNewestEpisodeProcessor tcNewestEpisodeProcessor = new TcNewestEpisodeProcessor();
            Spider spider = Spider.create(tcNewestEpisodeProcessor);
            List<VideoModel> videoModelList = selectAllVideoNewest();
            for (VideoModel videoModel : videoModelList) {
                Matcher id = idPat.matcher(videoModel.getDetailUrl());
                if (id.find()) {
                    spider.addUrl("https://s.video.qq.com/get_playsource?type=4&data_type=3&range=1-9999&id=" + id.group(1));
                    videoMap.put(id.group(1), videoModel);
                }
            }
            spider.thread(5).run();
            Long nowTime=System.currentTimeMillis()/1000;
            //对比数据库和爬取结果是否一致
            for (VideoModel newVideoModel : tcNewestEpisodeProcessor.getVideoModelList()) {
                VideoModel oldVideoModel=videoMap.get(newVideoModel.getExtra());
                newVideoModel.setCreateTime(nowTime);
                if(oldVideoModel.getNow()==newVideoModel.getNow()&&oldVideoModel.getStatus()==newVideoModel.getStatus()){
                    if(nowTime-oldVideoModel.getCreateTime()>2592000){
                        episodeDOMapper.setFinished(oldVideoModel.getEpisodeId());
                    }
                    continue;
                }
                newVideoModel.setId(oldVideoModel.getId());
                newVideoModel.setName(oldVideoModel.getName());
                EpisodeDO episodeDO=convertToEpisodeDO(newVideoModel);
                episodeDOMapper.insertSelective(episodeDO);
                /*
                *   接下来要开始通知用户了！
                * */

                mqProducer.notifyVideoMail(newVideoModel);
                //                System.out.println(videoModel.getDetailUrl() + " " +episodeMap.get(videoModel.getDetailUrl())+" "+ videoModel.getTitle());
            }
    }
    @Override
    public List<VideoModel> selectAllVideoNewest() {
        List<VideoDO> videoDOList = videoDOMapper.selectAll();
        List<VideoModel> videoModelList=new ArrayList<>();
        for (VideoDO videoDO : videoDOList) {
            EpisodeDO episodeDO = episodeDOMapper.selectByVideoIdNewest(videoDO.getId());
            if(episodeDO.getFinish()==1)continue;
            videoModelList.add(convertToVideoModel(videoDO,episodeDO));
        }
        return videoModelList;
    }


    public void insertVideoModel(VideoModel videoModel) {
        VideoDO videoDO = convertToVideoDO(videoModel);
        videoDOMapper.insertSelective(videoDO);
        videoModel.setId(videoDO.getId());
        EpisodeDO episodeDO = convertToEpisodeDO(videoModel);
        episodeDO.setId(null);
        episodeDO.setVideoId(videoDO.getId());
        episodeDOMapper.insertSelective(episodeDO);
    }

    private VideoDO convertToVideoDO(VideoModel videoModel) {
        if(videoModel==null)return null;
        VideoDO videoDO = new VideoDO();
        BeanUtils.copyProperties(videoModel, videoDO);
        return videoDO;
    }

    private EpisodeDO convertToEpisodeDO(VideoModel videoModel) {
        if(videoModel==null)return null;
        EpisodeDO episodeDO = new EpisodeDO();
        BeanUtils.copyProperties(videoModel, episodeDO);
        episodeDO.setId(videoModel.getEpisodeId());
        episodeDO.setVideoId(videoModel.getId());
        return episodeDO;
    }

    private VideoModel convertToVideoModel(VideoDO videoDO,EpisodeDO episodeDO) {
        if(videoDO==null&&episodeDO==null)return null;
        VideoModel videoModel=new VideoModel();
        BeanUtils.copyProperties(episodeDO,videoModel);
        BeanUtils.copyProperties(videoDO,videoModel);
        videoModel.setEpisodeId(episodeDO.getId());
        return videoModel;
    }

    public boolean selectExistVideo(VideoModel videoModel) {
        VideoDO videoDO = videoDOMapper.selectByDetailUrl(videoModel.getDetailUrl());
        if (videoDO == null) return false;
        EpisodeDO episodeDO = episodeDOMapper.selectByVideoIdNewest(videoDO.getId());
        if (episodeDO == null) return false;
        BeanUtils.copyProperties(episodeDO, videoModel);
        BeanUtils.copyProperties(videoDO, videoModel);
        return true;
    }
}
