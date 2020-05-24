package chj.idler.service.impl;

import chj.idler.dao.EpisodeDOMapper;
import chj.idler.dao.VideoDOMapper;
import chj.idler.dataobject.EpisodeDO;
import chj.idler.dataobject.VideoDO;
import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.VideoService;
import chj.idler.service.model.VideoModel;
import chj.idler.service.spider.tencent.TencentVideoProcessor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDOMapper videoDOMapper;
    @Autowired
    private EpisodeDOMapper episodeDOMapper;

//    private Logger logger= LoggerFactory.getLogger("V")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoModel analyseURL(String url)   {
        try {
            if (url.matches("https://v\\.qq\\.com/x/cover/.+\\.html.*")) {
                TencentVideoProcessor tencentVideoProcessor = new TencentVideoProcessor();
                Spider.create(tencentVideoProcessor).addUrl(url).thread(1).run();
                VideoModel videoModel = tencentVideoProcessor.getVideoModel();
                if (!selectExistVideo(videoModel.getDetailUrl(), videoModel)) {
                    Pattern idPat = Pattern.compile("https://v\\.qq\\.com/detail/[\\w]/(.+)\\.html");
                    Matcher id = idPat.matcher(videoModel.getDetailUrl());
                    if (id.find()) {
                        String episodeUrl = "https://s.video.qq.com/get_playsource?type=4&data_type=3&range=1-9999&id=" + id.group(1);
                        Spider.create(tencentVideoProcessor).addUrl(videoModel.getDetailUrl(), episodeUrl).thread(2).run();
                    }
                    //将来改为延时插入
                     insertVideoModel(videoModel);
                }
                return videoModel;
//            }else if(url1.getHost().equals("www.iqiyi.com")){
//                IqiyiProcessor iqiyiProcessor=new IqiyiProcessor();
//                Spider.create(iqiyiProcessor).addUrl(url).thread(1).run();
//                return iqiyiProcessor.getVideoModel();
            } else return null;
        } catch (Exception e) {
            throw e;
        }
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
        VideoDO videoDO = new VideoDO();
        BeanUtils.copyProperties(videoModel, videoDO);
        return videoDO;
    }

    private EpisodeDO convertToEpisodeDO(VideoModel videoModel) {
        EpisodeDO episodeDO = new EpisodeDO();
        BeanUtils.copyProperties(videoModel, episodeDO);
        return episodeDO;
    }

    public boolean selectExistVideo(String detailUrl, VideoModel videoModel) {
        VideoDO videoDO = videoDOMapper.selectByDetailUrl(detailUrl);
        if (videoDO == null) return false;
        EpisodeDO episodeDO = episodeDOMapper.selectByVideoIdNewest(videoDO.getId());
        if (episodeDO == null) return false;
        BeanUtils.copyProperties(episodeDO, videoModel);
        BeanUtils.copyProperties(videoDO, videoModel);
        return true;
    }
}
