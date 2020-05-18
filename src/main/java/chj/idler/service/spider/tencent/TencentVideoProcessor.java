package chj.idler.service.spider.tencent;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.model.VideoModel;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TencentVideoProcessor extends TencentProcessor  {


    @Override
    public void process(Page page){
        //先进入视频链接获取名称再进入搜索获取视频信息
        if (page.getUrl().regex(URL_WATCH).match()) {
            setName(page.getHtml().xpath("//h2[@class='player_title']/a/text()"));
            page.addTargetRequest("https://v.qq.com/x/search/?q="+videoModel.getName());
        } else if (page.getUrl().regex(URL_SEARCH).match()) {
            Selectable infoNode=page.getHtml().xpath(INFO).nodes().get(0);
            if(!checkName(infoNode))return;
            setSub(infoNode);
            setType(infoNode);
            setName2(infoNode);
            setDescription(infoNode);
            setImage(infoNode);
            if(!setNow(infoNode))return;
            if(!setEpisode(page.getHtml().xpath(EPISODES).nodes().get(0)))return;
            setSource(page.getHtml().xpath(SOURCE));
        }
    }

    private boolean setName(Selectable node){
        videoModel.setName(node.get());
        return true;
    }
    private boolean checkName(Selectable node){
        if(!StringUtils.equals(node.xpath(NAME).get(),videoModel.getName())){
            videoModel=null;
            return false;
        }
        return true;
    }
    private boolean setName2(Selectable node) {
        StringBuilder sb=new StringBuilder();
        String str=node.xpath(NAME2).get();
        Pattern p=Pattern.compile(">([^<]*)<");
        while(!StringUtils.isEmpty(str)){
            Matcher m=p.matcher(str);
            if(m.find()){
                str=str.substring(m.start(1));
                sb.append(m.group(1));
            }else{
                break;
            }
        }
        videoModel.setName2(sb.toString());
        return true;
    }
    private boolean setImage(Selectable node){
        videoModel.setImage("http:"+node.xpath(IMAGE).get());
        return true;
    }
    private boolean setNow(Selectable node){
        String current=node.xpath(NOW).get();
        Matcher matcher1=Pattern.compile("更新至(\\d+)集").matcher(current);
        Matcher matcher2=Pattern.compile("全(\\d+)集").matcher(current);
        if(matcher1.find()) {
            videoModel.setNow(Integer.valueOf(matcher1.group(1)));
            videoModel.setEnd(false);
        }else if(matcher2.find()){
            videoModel.setNow(Integer.valueOf(matcher2.group(1)));
            videoModel.setEnd(true);
        }else {
            videoModel=null;
            return false;
        }
        return true;
    }

    private boolean setEpisode(Selectable node){
        List<Selectable> episodes=node.xpath("/div/div[@class='item']").nodes();
        //最新一集
        Selectable episode=episodes.get(episodes.size()-1);
        //防止最后一个标签是折叠
        for(int i=episodes.size()-2;StringUtils.contains(episode.toString(),"item_fold")&&i>=0;i--){
            episode=episodes.get(i);
        }
        String url=episode.xpath("/div/a/@href").get();
        if(StringUtils.isEmpty(url)){
            videoModel=null;
            return false;
        }
        videoModel.setUrl(url);
        String status=episode.xpath("/div/span[@class='mark_v']/img/@src").get();
        if(StringUtils.isEmpty(status))videoModel.setStatus(0);
        else if(StringUtils.contains(status,"yu"))videoModel.setStatus(1);
        else if(StringUtils.contains(status,"vip"))videoModel.setStatus(2);
//        videoModel.setNow(Integer.valueOf(episode.xpath("/div/a/text()").get()));
        return true;
    }
    private boolean setSub(Selectable node){
        videoModel.setSub(StringUtils.trim(node.xpath(SUB).get()));
        return true;
    }
    private boolean setDescription(Selectable node){
        videoModel.setDescription(node.xpath(DESCRIPTION).get());
        return true;
    }
    private boolean setSource(Selectable node){
        videoModel.setSource(node.get());
        return true;
    }
    private boolean setType(Selectable node){
        videoModel.setType(node.xpath(TYPE).get());
        return true;
    }

    @Override
    public Site getSite() {
        return site;
    }


}
