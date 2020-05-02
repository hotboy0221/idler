package chj.idler.service.spider.tencent;

import chj.idler.service.model.EpisodeModel;
import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TencentVideoProcessor extends TencentProcessor implements PageProcessor {
    private static Pattern ID_PATTERN = Pattern.compile(">\\s*(\\d+)\\s*</a>");
    private static Pattern STATUS_ICON_PATTERN = Pattern.compile("src=\"([^\"]*)\"");
    private static Pattern URL_PATTERN = Pattern.compile("href=\"([^\"]*)\"");
    private VideoModel videoModel = new VideoModel();


    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL_WATCH).match()) {
            videoModel.setName(page.getHtml().xpath("//h2[@class='player_title']/a/text()").get());
            page.addTargetRequest("https://v.qq.com/x/search/?q="+ videoModel.getName());
            List<String> episodeList = page.getHtml().xpath("//div[@class='mod_episode']/span[@class='item']").all();
            for (String str : episodeList) {
                EpisodeModel episodeModel = new EpisodeModel();
                Matcher m1 = ID_PATTERN.matcher(str);
                Matcher m2 = STATUS_ICON_PATTERN.matcher(str);
                Matcher m3 = URL_PATTERN.matcher(str);
                if (m1.find())
                    episodeModel.setId(m1.group(1));
                if (m2.find()) {
                    String statusIcon = m2.group(1);
                    episodeModel.setStatusIcon(statusIcon);
                    if (statusIcon.contains("vip"))
                        episodeModel.setStatus(2);
                    else if (statusIcon.contains("yu"))
                        episodeModel.setStatus(1);
                } else {
                    episodeModel.setStatus(0);
                }
                if (m3.find())
                    episodeModel.setUrl("https://v.qq.com/" + m3.group(1));
                videoModel.addEpisode(episodeModel.getId()+"_"+episodeModel.getStatus(),episodeModel);
            }
        } else if (page.getUrl().regex(URL_SEARCH).match()) {
            String current=page.getHtml().xpath("//span[@class='figure_caption']/span[@class='figure_info']/text()").get();
            Matcher matcher1=Pattern.compile("更新至(\\d+)集").matcher(current);
            Matcher matcher2=Pattern.compile("全(\\d+)集").matcher(current);
            if(matcher1.find()) {
                videoModel.setTotal(Integer.valueOf(matcher1.group(1)));
                videoModel.setEnd(false);
            }else if(matcher2.find()){
                videoModel.setTotal(Integer.valueOf(matcher2.group(1)));
                videoModel.setEnd(true);
            }
            videoModel.setImg(page.getHtml().xpath("//img[@class='figure_pic']/@src").get());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public VideoModel getVideoModel() {
        return videoModel;
    }
}
