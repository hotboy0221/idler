package chj.idler.service.spider.tencent;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class TencentCartoonProcessor extends  TencentProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.putField("html",page.getHtml());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
