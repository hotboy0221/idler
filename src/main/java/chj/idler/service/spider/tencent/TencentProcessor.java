package chj.idler.service.spider.tencent;

import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Pattern;

public class TencentProcessor implements PageProcessor {
    protected static final String URL_WATCH="https://v\\.qq\\.com/x/cover/.+\\.html";
    protected static final String URL_SEARCH="https://v\\.qq\\.com/x/search/\\?q=.+";


    protected static String EPISODES="//div[@class='result_episode_list cf']";
    protected static String SOURCE="//div[@class='_playsrc']/div[@class='result_source']/span[@class='_cur_playsrc']/span[@class='icon_text']/span/text()";
    protected static String INFO="//div[@class='_infos']/div";

    protected static String NOW="/div/a/span[@class='figure_caption']/span[@class='figure_info']/text()";
    protected static String IMAGE="/div/a/img[@class='figure_pic']/@src";

    protected static String NAME="/div/h2[@class='result_title']/a/em[@class='hl']/text()";
    protected static String SUB="/div/h2[@class='result_title']/a/span[@class='sub']/text()";
    protected static String TYPE="/div/h2[@class='result_title']/a/span[@class='type']/text()";

    protected static String NAME2="/div/div[@class='result_info']/div[@class='info_item info_item_odd']/span[@class='content']";
    protected static String DESCRIPTION="/div/div[@class='result_info']/div[@class='info_item info_item_desc']/span[@class='desc_text']/text()";



    protected Site site = Site
            .me()
            .setDomain("v.qq.com")
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    protected VideoModel videoModel = new VideoModel();

    public VideoModel getVideoModel() {
        return videoModel;
    }

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return null;
    }
}
