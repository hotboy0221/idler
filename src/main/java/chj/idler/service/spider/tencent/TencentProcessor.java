package chj.idler.service.spider.tencent;

import us.codecraft.webmagic.Site;

public class TencentProcessor {
    protected static final String URL_WATCH="https://v\\.qq\\.com/x/cover/.+\\.html";
    protected static final String URL_SEARCH="https://v\\.qq\\.com/x/search/\\?q=.+";
    protected Site site = Site
            .me()
            .setDomain("v.qq.com")
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
}
