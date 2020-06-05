package chj.idler.service.model;

import lombok.Data;

@Data
public class VideoModel {
    private Integer id;
    private Integer episodeId;
    //视频名称
    private String name;
    //类型，电视剧，动漫，综艺
    private String type;
    //详情链接
    private String detailUrl;
    //视频别名
    private String name2;
    //视频图片
    private String image;
    //标签
    private String tags;
    //简介
    private String description;
    //评分
    private Double score;
    //视频来源，0未知，1腾讯，2爱奇艺，3优酷
    private Byte source;
    //是否完结
    private Byte finish;
    //0正常，1预告，2vip
    private Byte status;
    //最新一集链接
    private String url;
    //最新一集名称
    private String title;
    //当前第几集
    private Integer now;
    //当前集照片
    private String picture;
    //是否订阅通知
    private Byte sub;
    //该集添加时间
    private Long createTime;


}
