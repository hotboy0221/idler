package chj.idler.service.model;

public class EpisodeModel {
    private String id;
    //0正常，1预告，2vip
    private Integer status;
    private String statusIcon;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(String statusIcon) {
        this.statusIcon = statusIcon;
    }
}
