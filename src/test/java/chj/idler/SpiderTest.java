package chj.idler;

import chj.idler.controller.VideoController;
import chj.idler.response.BusinessException;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import chj.idler.service.spider.tencent.TencentCartoonProcessor;
import chj.idler.service.spider.tencent.TencentVideoProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;

@SpringBootTest
class SpiderTest {
    @Autowired
    private VideoController videoController;
    @Test
    void main(){

    }

    @Test
    void main2()throws BusinessException {
        Spider.create(new TencentCartoonProcessor()).addUrl("https://v.qq.com/x/cover/hzgtnf6tbvfekfv/p0014r4bvbp.html").addPipeline(new FilePipeline("D:/")).run();
    }

    @Test
    void main3(){

Spider.create(new IqiyiVideoProcessor()).addUrl("https://www.iqiyi.com/v_19rrjaaf3s.html").run();
    }
    @Test
    void main4()throws BusinessException{
        long a=System.currentTimeMillis();
//        Spider.create(new TencentVideoProcessor()).addUrl("https://v.qq.com/x/cover/otff8quzy6b2dlw.html").thread(1).run();
        System.out.println(videoController.addWatching("https://v.qq.com/x/cover/mzc00200k12f5gi.html"));
//        Spider.create(new TencentVideoProcessor()).addUrl("https://v.qq.com/x/cover/a7red88y4nwhbka.html").thread(1).run();
        long b=System.currentTimeMillis();
        System.out.println(b-a);
    }


}
