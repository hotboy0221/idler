package chj.idler.service.impl;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import chj.idler.service.spider.tencent.TencentVideoProcessor;
import chj.idler.service.VideoService;
import chj.idler.service.model.VideoModel;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.net.MalformedURLException;
import java.net.URL;
@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public VideoModel analyseURL(String url) throws BusinessException{
        try {
            URL url1 = new URL(url);
            if(url1.getHost().equals("v.qq.com")) {
                TencentVideoProcessor tencentVideoProcessor = new TencentVideoProcessor();
                Spider.create(tencentVideoProcessor).addUrl(url).thread(1).run();
                return tencentVideoProcessor.getVideoModel();
            }else if(url1.getHost().equals("www.iqiyi.com")){
                IqiyiVideoProcessor iqiyiVideoProcessor=new IqiyiVideoProcessor();
                Spider.create(iqiyiVideoProcessor).addUrl(url).thread(1).run();
                return iqiyiVideoProcessor.getVideoModel();
            }else throw new BusinessException(EmBusinessError.URL_ERROR,"暂不支持此网址");
        }catch (MalformedURLException e){
            throw new BusinessException(EmBusinessError.URL_ERROR);
        }
    }
}
