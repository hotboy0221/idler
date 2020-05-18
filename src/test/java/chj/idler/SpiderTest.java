package chj.idler;

import chj.idler.controller.VideoController;
import chj.idler.response.BusinessException;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class SpiderTest {
    @Autowired
    private VideoController videoController;
    @Test
    void main(){

    }

    @Test
    void main2()throws BusinessException {
        videoController.search("https://v.qq.com/x/cover/m441e3rjq9kwpsc/h0025iluh3s.html");
    }

    @Test
    void main3(){
        String s="<span class=\"content\"><em class=\"hl\">狐狸的夏天第</em>2<em class=\"hl\">季</em>  <em class=\"hl\">狐狸的夏天第二季</em>  谎言女友</span>";
        Pattern p=Pattern.compile("<[^>*]>");
        Matcher m=p.matcher(s);
        if(m.find()){
            for(int i=0;i<m.groupCount();i++)
                System.out.println(m.group(i));
        }
    }
    @Test
    void main4()throws BusinessException{
        long a=System.currentTimeMillis();
//        Spider.create(new TencentVideoProcessor()).addUrl("https://v.qq.com/x/cover/otff8quzy6b2dlw.html").thread(1).run();
        System.out.println(videoController.search("https://v.qq.com/x/cover/mzc00200k12f5gi.html"));
//        Spider.create(new TencentVideoProcessor()).addUrl("https://v.qq.com/x/cover/a7red88y4nwhbka.html").thread(1).run();
        long b=System.currentTimeMillis();
        System.out.println(b-a);
    }


}
