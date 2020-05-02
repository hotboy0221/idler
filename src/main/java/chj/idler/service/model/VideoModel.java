package chj.idler.service.model;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.TreeMap;

public class VideoModel {

    private String img;
    private String name;
    private Integer total;
    private boolean end;
    
    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    private TreeMap<String, EpisodeModel> episodes=new TreeMap<String , EpisodeModel>();

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public TreeMap<String, EpisodeModel> getEpisodes() {
        return episodes;
    }

    public void addEpisode(String eid,EpisodeModel episodeModel) {
        this.episodes.put(eid,episodeModel);
    }

}
