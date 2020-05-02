package chj.idler.service.spider.iqiyi;

import us.codecraft.webmagic.Site;

public class IqiyiProcessor {
    protected static final String URL_WATCH="https://www\\.iqiyi\\.com/.+\\.html";
    protected static final String URL_SEARCH="https://so\\.iqiyi\\.com/so/q_.+";
    protected Site site = Site
            .me()
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
}
