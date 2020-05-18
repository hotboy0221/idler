package chj.idler.service.spider.iqiyi;

import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Pattern;

public class IqiyiVideoProcessor extends IqiyiProcessor implements PageProcessor {
    private VideoModel videoModel = new VideoModel();
    private static Pattern ID_PATTERN = Pattern.compile(">\\s*(\\d+)\\s*</a>");
    private static Pattern STATUS_ICON_PATTERN = Pattern.compile("src=\"([^\"]*)\"");
    private static Pattern URL_PATTERN = Pattern.compile("href=\"([^\"]*)\"");




    @Override
    public void process(Page page) {
//        if(page.getUrl().regex(URL_WATCH).match()){
//            videoModel.setName(page.getHtml().xpath("//h2[@class='header-txt']/a[@class='header-link']/text()").get());
//            page.addTargetRequest("https://so.iqiyi.com/so/q_"+videoModel.getName());
//        }else if(page.getUrl().regex(URL_SEARCH).match()){
//            videoModel.setImg(page.getHtml().xpath("//a[@class='qy-mod-link']/img[@alt='"+videoModel.getName()+"']/@src").get());
//            String current=page.getHtml().xpath("//a[@class='qy-mod-link']/p[@class='icon-br']/span[@class='qy-mod-label']/text()").get();
//            Matcher matcher1= Pattern.compile("更新至(\\d+)集").matcher(current);
//            Matcher matcher2=Pattern.compile("(\\d+)集全").matcher(current);
//            if(matcher1.find()) {
//                videoModel.setTotal(Integer.valueOf(matcher1.group(1)));
//                videoModel.setEnd(false);
//            }else if(matcher2.find()){
//                videoModel.setTotal(Integer.valueOf(matcher2.group(1)));
//                videoModel.setEnd(true);
//            }
//            List<String> episodeList = page.getHtml().xpath("//div[@class='qy-search-result-album']").nodes().get(0).xpath("//ul[@class='album-list' and @style='display:none;']/li[@class='album-item']").all();
//            for (String str : episodeList) {
//                EpisodeModel episodeModel = new EpisodeModel();
//                Matcher m1 = ID_PATTERN.matcher(str);
//                Matcher m2 = STATUS_ICON_PATTERN.matcher(str);
//                Matcher m3 = URL_PATTERN.matcher(str);
//                if (m1.find())
//                    episodeModel.setId(m1.group(1));
//                if (m2.find()) {
//                    String statusIcon = m2.group(1);
//                    episodeModel.setStatusIcon(statusIcon);
//                    if (statusIcon.contains("vip"))
//                        episodeModel.setStatus(2);
//                    else if (statusIcon.contains("pre"))
//                        episodeModel.setStatus(1);
//                } else {
//                    episodeModel.setStatus(0);
//                }
//                if (m3.find())
//                    episodeModel.setUrl(m3.group(1));
//                videoModel.addEpisode(episodeModel.getId()+"_"+episodeModel.getStatus(),episodeModel);
//            }
 //       }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public VideoModel getVideoModel() {
        return videoModel;
    }
}
