package chj.idler.service.impl;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.spider.iqiyi.IqiyiProcessor;
import chj.idler.service.spider.tencent.TencentProcessor;
import chj.idler.service.VideoService;
import chj.idler.service.model.VideoModel;
import chj.idler.service.spider.tencent.TencentVideoProcessor;
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
                TencentProcessor tencentProcessor = new TencentVideoProcessor();
                Spider.create(tencentProcessor).addUrl(url).thread(1).run();
                return tencentProcessor.getVideoModel();
//            }else if(url1.getHost().equals("www.iqiyi.com")){
//                IqiyiProcessor iqiyiProcessor=new IqiyiProcessor();
//                Spider.create(iqiyiProcessor).addUrl(url).thread(1).run();
//                return iqiyiProcessor.getVideoModel();
            }else return null;
        }catch (MalformedURLException e){
            throw new BusinessException(EmBusinessError.URL_ERROR);
        }catch (Exception e){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
    }
}
