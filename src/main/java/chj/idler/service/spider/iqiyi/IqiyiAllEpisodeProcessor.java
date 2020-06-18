package chj.idler.service.spider.iqiyi;

import chj.idler.dataobject.EpisodeDO;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class IqiyiAllEpisodeProcessor extends IqiyiProcessor implements PageProcessor {
    private List<EpisodeDO> episodeDOList = new ArrayList<>();
    @Override
    public void process(Page page) {

    }

    public List<EpisodeDO> getEpisodeDOList() {
        return episodeDOList;
    }
}
