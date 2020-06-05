package chj.idler.service.spider.tencent;

import chj.idler.service.model.VideoModel;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcVideoProcessor extends TcProcessor implements PageProcessor {

    private VideoModel videoModel = new VideoModel();

    public VideoModel getVideoModel() {
        return videoModel;
    }


    @Override
    public void process(Page page){
        //先进入视频链接获取名称再进入搜索获取视频信息
        if (page.getUrl().regex(URL_WATCH).match()) {
            videoModel.setDetailUrl("https://v.qq.com"+page.getHtml().xpath("//h2[@class='player_title']/a/@href").get());
        } else if (page.getUrl().regex(URL_EPISODES).match()) {
            initVideoEpisode(page,this.videoModel);
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


}
