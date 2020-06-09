package chj.idler.service.spider.tencent;

import chj.idler.dataobject.EpisodeDO;
import chj.idler.service.model.VideoModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcProcessor {
    public static final String URL_WATCH="^https://v\\.qq\\.com/x/cover/.+\\.html.*$";
    public static final String URL_DETAIL="^https://v\\.qq\\.com/detail/.+$";
    public static final String URL_EPISODES="^https://s\\.video\\.qq\\.com/get_playsource\\?.+$";
    private Pattern payTypePat=Pattern.compile("<payType>([\\d])</payType>");
    private Pattern typePat=Pattern.compile("<type>([\\d])</type></videoPlayList>");
    private Pattern picPat=Pattern.compile("<pic>(.*)</pic>");
    private Pattern playUrlPat=Pattern.compile("<playUrl>(.*)</playUrl>");
    private Pattern titlePat=Pattern.compile("<title>(.*)</title>");
    private Pattern episodeNumberPat=Pattern.compile("<episode_number>([\\d]+)</episode_number>");
    protected Site site = Site
            .me()
            .setDomain("v.qq.com")
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

    protected  void initVideoEpisode(Page page,VideoModel videoModel){
        String[] videoList = page.getRawText().split("<videoPlayList>");
        /*          type    payType
         *  付费       1       1
         *  预告       2       0
         *  普通       1       0
         *  超前点播   2       1
         */
        Matcher payType = payTypePat.matcher(videoList[1]);
        Matcher type = typePat.matcher(videoList[1]);
        Matcher pic = picPat.matcher(videoList[1]);
        Matcher playUrl = playUrlPat.matcher(videoList[1]);
        Matcher title = titlePat.matcher(videoList[1]);
        Matcher episodeNumber=episodeNumberPat.matcher(videoList[1]);
        if (payType.find() && type.find()) {
            int t = Integer.valueOf(type.group(1));
            int pt = Integer.valueOf(payType.group(1));
            if (pt == 1) {
                if(t==2) {
                    videoModel.setStatus(new Byte("2"));
                }else if(t==1) {
                    videoModel.setStatus(new Byte("1"));
                }
            } else {
                if (t == 2) {
                    videoModel.setStatus(new Byte("3"));
                } else {
                    videoModel.setStatus(new Byte("0"));
                }
            }
        }
        if (pic.find())
            videoModel.setPicture(pic.group(1));
        if (playUrl.find())
            videoModel.setUrl(playUrl.group(1));
        if (title.find())
            videoModel.setTitle(title.group(1));
        if(episodeNumber.find())
            videoModel.setNow(Integer.valueOf(episodeNumber.group(1)));
        videoModel.setCreateTime(System.currentTimeMillis()/1000);
    }

    protected  void initVideoEpisode(Page page, List<EpisodeDO> episodeDOList) {
        String[] videoList = page.getRawText().split("<videoPlayList>");
        /*          type    payType
         *  付费       1       1
         *  预告       2       0
         *  普通       1       0
         *  超前点播   2       1
         */
        for (int i = videoList.length-1; i >0; i--) {
            Matcher payType = payTypePat.matcher(videoList[i]);
            Matcher type = typePat.matcher(videoList[i]);
            Matcher pic = picPat.matcher(videoList[i]);
            Matcher playUrl = playUrlPat.matcher(videoList[i]);
            Matcher title = titlePat.matcher(videoList[i]);
            Matcher episodeNumber = episodeNumberPat.matcher(videoList[i]);
            EpisodeDO episodeDO=new EpisodeDO();
            if (payType.find() && type.find()) {
                int t = Integer.valueOf(type.group(1));
                int pt = Integer.valueOf(payType.group(1));
                if (pt == 1) {
                    if(t==2) {
                        episodeDO.setStatus(new Byte("2"));
                    }else if(t==1) {
                        episodeDO.setStatus(new Byte("1"));
                    }
                } else {
                    if (t == 2) {
                        episodeDO.setStatus(new Byte("3"));
                    } else {
                        episodeDO.setStatus(new Byte("0"));
                    }
                }
            }
            if (pic.find())
                episodeDO.setPicture(pic.group(1));
            if (playUrl.find())
                episodeDO.setUrl(playUrl.group(1));
            if (title.find())
                episodeDO.setTitle(title.group(1));
            if (episodeNumber.find())
                episodeDO.setNow(Integer.valueOf(episodeNumber.group(1)));
            episodeDO.setCreateTime(System.currentTimeMillis() / 1000);
            episodeDOList.add(episodeDO);
        }
    }
    public Site getSite() {
        return site;
    }
}
