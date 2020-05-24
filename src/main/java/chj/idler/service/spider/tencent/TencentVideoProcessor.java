package chj.idler.service.spider.tencent;

import chj.idler.dao.EpisodeDOMapper;
import chj.idler.dao.VideoDOMapper;
import chj.idler.dataobject.EpisodeDO;
import chj.idler.dataobject.VideoDO;
import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.model.VideoModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TencentVideoProcessor implements PageProcessor {

    private static final String URL_WATCH="https://v\\.qq\\.com/x/cover/.+\\.html.*";
    private static final String URL_DETAIL="https://v\\.qq\\.com/detail/.+";
    private static final String URL_EPISODES="https://s\\.video\\.qq\\.com/get_playsource?.+";
    protected VideoModel videoModel = new VideoModel();
    protected Site site = Site
            .me()
            .setDomain("v.qq.com")
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");


    public VideoModel getVideoModel() {
        return videoModel;
    }


    @Override
    public void process(Page page){
        //先进入视频链接获取名称再进入搜索获取视频信息
        if (page.getUrl().regex(URL_WATCH).match()) {
            videoModel.setDetailUrl("https://v.qq.com"+page.getHtml().xpath("//h2[@class='player_title']/a/@href").get());
        } else if (page.getUrl().regex(URL_EPISODES).match()) {
            String[] videoList=page.getRawText().split("<videoPlayList>");
            /*          type    payType
            *  付费       1       1
            *  预告       2       0
            *  普通       1       0
            */
            Pattern payTypePat=Pattern.compile("<payType>([\\d])</payType>");
            Pattern typePat=Pattern.compile("<type>([\\d])</type></videoPlayList>");
            Pattern picPat=Pattern.compile("<pic>(.*)</pic>");
            Pattern playUrlPat=Pattern.compile("<playUrl>(.*)</playUrl>");
            Pattern titlePat=Pattern.compile("<title>(.*)</title>");
            Pattern episodeNumberPat=Pattern.compile("<episode_number>([\\d]+)</episode_number>");

            Matcher payType=payTypePat.matcher(videoList[1]);
            Matcher type=typePat.matcher(videoList[1]);
            Matcher pic=picPat.matcher(videoList[1]);
            Matcher playUrl=playUrlPat.matcher(videoList[1]);
            Matcher title=titlePat.matcher(videoList[1]);
            Matcher episodeNumber=episodeNumberPat.matcher(videoList[1]);

            if(payType.find()&&type.find()){
                int t=Integer.valueOf(type.group(1));
                int pt=Integer.valueOf(payType.group(1));
                if(pt==1){
                    videoModel.setStatus(new Byte("2"));
                }else{
                    if(t==2){
                        videoModel.setStatus(new Byte("1"));
                    }else{
                        videoModel.setStatus(new Byte("0"));
                    }
                }
            }
            if(pic.find())
                videoModel.setPicture(pic.group(1));
            if(playUrl.find())
                videoModel.setUrl(playUrl.group(1));
            if(title.find())
                videoModel.setTitle(title.group(1));
            if(episodeNumber.find())
                videoModel.setNow(Integer.valueOf(episodeNumber.group(1)));
        }else if(page.getUrl().regex(URL_DETAIL).match()){
            videoModel.setDetailUrl(page.getUrl().get());
            Selectable node=page.getHtml().xpath("//div[@class='detail_video']").nodes().get(0);
            videoModel.setImage("http:"+page.getHtml().xpath("//img[@class='figure_pic']/@src").get());
            videoModel.setName(node.xpath("//h1[@class='video_title_cn']/a/text()").get());
            if("别　名:".equals(node.xpath("/div/[@class='video_type cf']/div/span[@class='type_tit']/text()").get()))
                videoModel.setName2(node.xpath("/div/[@class='video_type cf']/div/span[@class='type_txt']/text()").get());
            videoModel.setType(node.xpath("//h1[@class='video_title_cn']/span[@class='type']/text()").get());
            videoModel.setSource(new Byte("1"));
            String score=node.xpath("//span[@class='score']/text()").get();
            if(!StringUtils.isEmpty(score))
                videoModel.setScore(Double.valueOf(score));
            List<String> tags=node.xpath("//div[@class='video_tag cf']/div[@class='tag_list']/a/text()").all();
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<tags.size();i++){
                if(i!=0)sb.append("/");
                sb.append(tags.get(i));
            }
            videoModel.setTags(sb.toString());
            videoModel.setFinish(node.get().matches("总集数")?new Byte("1"):new Byte("0"));
            videoModel.setDescription(node.xpath("//div[@class='video_desc']/span[@class='desc_txt']/span/text()").get());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }



}
