package chj.idler.service.spider.tencent;

import chj.idler.dataobject.EpisodeDO;
import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcAllEpisodeProcessor extends TcProcessor implements PageProcessor {
    private List<EpisodeDO> episodeDOList = new ArrayList<>();
    @Override
    public void process(Page page) {
        Matcher id = Pattern.compile("^https://s\\.video\\.qq\\.com/get_playsource\\?.+id=([^&]*)$").matcher(page.getUrl().get());
        if (!id.find()) return;
        initVideoEpisode(page,episodeDOList);
    }

    public List<EpisodeDO> getEpisodeDOList() {
        return episodeDOList;
    }
}
