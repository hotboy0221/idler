package chj.idler;

import chj.idler.controller.VideoController;
import chj.idler.dao.UserDOMapper;
import chj.idler.response.BusinessException;
import chj.idler.rocketmq.MqProducer;
import chj.idler.service.model.VideoModel;
import chj.idler.service.spider.iqiyi.IqiyiVideoProcessor;
import chj.idler.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.mail.MessagingException;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

@SpringBootTest
class SpiderTest {
    @Autowired
    private VideoController videoController;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private MqProducer mqProducer;
    @Test
    void main() throws  JsonProcessingException, MessagingException {
        String []strings=new String[]{"1240063344@qq.com"};
            emailUtil.newVideoNotify(strings,null);
    }
    @Test
    void mai3n() throws  JsonProcessingException, MQClientException,InterruptedException, RemotingException, MQBrokerException{

    }

    @Test
    void main2()throws Exception {
        System.out.println(Thread.currentThread().getName());
        long t1=System.currentTimeMillis();
//        videoController.search("https://v.qq.com/x/cover/mzc002008ve9hgc.html");
        videoController.search("https://www.iqiyi.com/v_19rxo0pcu4.html");
//        Spider.create(new IqiyiVideoProcessor()).addUrl("https://www.iqiyi.com/v_19rxo0pcu4.html").thread(1).run();
        long t2=System.currentTimeMillis();
        System.out.println(t2-t1);
        System.out.println((t2-t1)/1000);
    }

    @Test
    void main3()throws SchedulerException,InterruptedException{


    }
    @Test
    void main4()throws BusinessException{
    }
}
