package chj.idler.service.spider.iqiyi;

import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IqiyiNewestEpisodeProcessor extends IqiyiProcessor implements PageProcessor {
    private List<VideoModel> videoModelList = new ArrayList<>();

    @Override
    public void process(Page page) {
        Matcher id = Pattern.compile("^https://pcw-api\\.iqiyi\\.com/album.+id=([\\d]+)$").matcher(page.getUrl().get());
        if (!id.find()) return;
        VideoModel videoModel = new VideoModel();
        //在这extra是video的在网站里的id标识字符串，用来标识每一集对应的影视
        videoModel.setExtra(id.group(1));
//        initVideoEpisode(page,videoModel);
        videoModelList.add(videoModel);
    }

    public List<VideoModel> getVideoModelList() {
        return videoModelList;
    }
}
