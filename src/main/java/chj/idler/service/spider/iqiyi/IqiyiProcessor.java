package chj.idler.service.spider.iqiyi;

import us.codecraft.webmagic.Site;

import java.util.regex.Pattern;

public class IqiyiProcessor {
    public static final String URL_WATCH="^https://www\\.iqiyi\\.com/v_.+\\.html.*$";
    public static final String URL_DETAIL="^https://www\\.iqiyi\\.com/a_.+\\.html.*$";
    public static final String URL_EPISODES="^https://pcw-api\\.iqiyi\\.com/album.+$";
//    https://pcw-api.iqiyi.com/albums/album/avlistinfo?aid=252682501&size=500&page=1
    //https://pcw-api.iqiyi.com/album/source/svlistinfo?&sourceid=252682501&timelist=2020
    protected Site site = Site
            .me()
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

    public Site getSite() {
        return site;
    }
}
