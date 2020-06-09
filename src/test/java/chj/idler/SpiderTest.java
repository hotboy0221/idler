package chj.idler;

import chj.idler.controller.VideoController;
import chj.idler.dao.UserDOMapper;
import chj.idler.response.BusinessException;
import chj.idler.rocketmq.MqProducer;
import chj.idler.service.model.VideoModel;
import chj.idler.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        VideoModel videoModel=new VideoModel();
        videoModel.setName("哈哈1");
        videoModel.setId(100186);

        Thread.sleep(20000);
    }

    @Test
    void main2()throws Exception {
        long t1=System.currentTimeMillis();
        videoController.search("https://v.qq.com/x/cover/ipmc5u3dwb48mv2/f0020pa88so.html");
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
