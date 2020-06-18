package chj.idler.service.spider.iqiyi;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.model.VideoModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IqiyiVideoProcessor extends IqiyiProcessor implements PageProcessor {
    private VideoModel videoModel = new VideoModel();
    private String episodesUrl;



    @Override
    public void process(Page page)  {
        if(page.getUrl().regex(URL_WATCH).match()){
            videoModel.setDetailUrl("https:"+page.getHtml().xpath("//h1[@class='player-title']/a[@class='title-link']/@href").get());
            //channelId 2:电视剧 4：动漫 6：综艺
            String html=page.getHtml().get();
            Pattern typePat=Pattern.compile("param\\['channelID'\\] = \"([^\"]*)\"");
            Pattern albumIdPat=Pattern.compile("param\\['albumId'\\] = \"([^\"]*)\"");
            Matcher type=typePat.matcher(html);
            Matcher albumId=albumIdPat.matcher(html);
            if(type.find()&&albumId.find()){
                int typeN=Integer.valueOf(type.group(1));
                if(typeN==2){
                    videoModel.setType("电视剧");
                    episodesUrl="https://pcw-api.iqiyi.com/albums/album/avlistinfo?size=5000&page=1&aid="+albumId;
                }else if(typeN==4){
                    videoModel.setType("动漫");
                    episodesUrl="https://pcw-api.iqiyi.com/albums/album/avlistinfo?size=5000&page=1&aid="+albumId;
                } else if(typeN==6){
                    videoModel.setType("综艺");
                    episodesUrl="https://pcw-api.iqiyi.com/album/source/svlistinfo?sourceid="+albumId.group(1);
                }else{
                    videoModel=null;
                }
            }else videoModel=null;

        }else if(page.getUrl().regex(URL_DETAIL).match()){
            try{
            videoModel.setPublishYear(Integer.valueOf(page.getHtml().xpath("//a[@class='tag-item itemprop=']/text()").get()));
            if(videoModel.getType().equals("综艺")){
                this.episodesUrl+="&timelist="+videoModel.getPublishYear();
            }
            }catch (NumberFormatException e){
                videoModel=null;
                return ;
            }
            videoModel.setName(page.getHtml().xpath("//h1[@class='album-head-title']/a[@class='title-link']/text()").get());
            List<String> tags=page.getHtml().xpath("//div[@class='album-head-tag']/a/text()").all();
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<tags.size();i++){
                if(i!=0)sb.append("/");
                sb.append(tags.get(i));
            }
            videoModel.setTags(sb.toString());
            videoModel.setDescription(page.getHtml().xpath("//div[@class='album-head-des']/@title").get());
            videoModel.setSource(new Byte("2"));
            Matcher score=Pattern.compile("([\\d](\\.\\d)?)").matcher(page.getHtml().xpath("//i[@class='effect-score']/text()").get());
            if(score.find())
            videoModel.setScore(Double.valueOf(score.group(1)));
            videoModel.setImage("http:"+page.getHtml().xpath("//meta[@itemprop='image']/@content"));

        }else if(page.getUrl().regex(URL_EPISODES).match()){
            videoModel.setCreateTime(System.currentTimeMillis()/1000);
            if(videoModel.getType().equals("综艺")){
                JSONArray episodes=(JSONArray)((JSONObject)page.getJson().toObject(JSONObject.class).get("data")).get(String.valueOf(videoModel.getPublishYear()));
                JSONObject episode=(JSONObject) episodes.get(episodes.size()-1);

            }else{

            }
        }
    }



    public VideoModel getVideoModel() {
        return videoModel;
    }

    public String getEpisodesUrl() {
        return episodesUrl;
    }
}
