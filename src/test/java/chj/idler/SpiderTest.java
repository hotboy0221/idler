package chj.idler;

import chj.idler.controller.VideoController;
import chj.idler.dao.UserDOMapper;
import chj.idler.dataobject.UserDO;
import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.VideoService;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private UserDOMapper userDOMapper;
    @Test
    void main(){


    }

    @Test
    void main2()throws BusinessException {
        long t1=System.currentTimeMillis();
        videoController.search("https://v.qq.com/x/cover/m441e3rjq9kwpsc/h0025iluh3s.html");
        long t2=System.currentTimeMillis();
        System.out.println(t2-t1);
        System.out.println((t2-t1)/1000);
    }

    @Test
    void main3(){

    }
    @Test
    void main4()throws BusinessException{
    }


}
