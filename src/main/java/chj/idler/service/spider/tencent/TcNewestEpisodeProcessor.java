package chj.idler.service.spider.tencent;

import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcNewestEpisodeProcessor extends TcProcessor implements PageProcessor {
    private List<VideoModel> videoModelList = new ArrayList<>();

    @Override
    public void process(Page page) {
        Matcher id = Pattern.compile("^https://s\\.video\\.qq\\.com/get_playsource\\?.+id=([^&]*)$").matcher(page.getUrl().get());
        if (!id.find()) return;
        VideoModel videoModel = new VideoModel();
        //在这detailUrl实际上是video的在网站里的标识字符串，用来标识每一集对应的影视
        videoModel.setDetailUrl(id.group(1));
        initVideoEpisode(page,videoModel);
        videoModelList.add(videoModel);
    }

    public List<VideoModel> getVideoModelList() {
        return videoModelList;
    }
}
