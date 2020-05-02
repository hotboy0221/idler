package chj.idler.controller.viewobject;

import chj.idler.service.model.EpisodeModel;

import java.util.TreeMap;

public class VideoVO {
    private String img;
    private String name;
    private Integer total;
    private boolean end;
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

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public TreeMap<String, EpisodeModel> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(TreeMap<String, EpisodeModel> episodes) {
        this.episodes = episodes;
    }
}
