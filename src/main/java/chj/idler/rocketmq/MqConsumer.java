package chj.idler.rocketmq;

import chj.idler.service.VideoService;
import chj.idler.service.model.UserModel;
import chj.idler.service.model.VideoModel;
import chj.idler.service.spider.tencent.TcAllEpisodeProcessor;
import chj.idler.util.EmailUtil;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Component
public class MqConsumer {

    private DefaultMQPushConsumer notifyVideoConsumer;
    private DefaultMQPushConsumer registerConsumer;
    private DefaultMQPushConsumer episodesConsumer;
    @Value("${rocketmq.nameserver.addr}")
    private String nameAddr;

    @Value("${rocketmq.topic.mail}")
    private String mailTopic;

    @Value("${rocketmq.topic.spider}")
    private String spiderTopic;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private VideoService videoService;

    @PostConstruct
    public void init() throws MQClientException {

        consume(notifyVideoConsumer,"mail_consumer_group",mailTopic,"notifyVideo",(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)-> {
            VideoModel videoModel=JSON.parseObject(new String(list.get(0).getBody()), VideoModel.class);
            List<String> mails=videoService.selectSubEmails(videoModel.getId());
            try {
                emailUtil.newVideoNotify((String[]) mails.toArray(), videoModel);
            }catch (MessagingException e){
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
        );

        consume(registerConsumer,"mail_consumer_group",mailTopic,"register",(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)-> {
            Map map=JSON.parseObject(list.get(0).getBody(), HashMap.class);
            try {
                emailUtil.activateUser((String)map.get("registerToken"),(UserModel) map.get("userModel"));
            }catch (MessagingException e){
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
        );

        consume(episodesConsumer, "spider_consumer_group", spiderTopic, "episodes",(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)-> {
               VideoModel videoModel=JSON.parseObject(list.get(0).getBody(),VideoModel.class);
               videoService.episodesSpider(videoModel);
               return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
         );

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
                if (registerConsumer != null) {
                    registerConsumer.shutdown();
                }
                if(notifyVideoConsumer!=null){
                    notifyVideoConsumer.shutdown();
                }
                if(episodesConsumer!=null){
                    episodesConsumer.shutdown();
                }
        }));
    }

    private void consume(DefaultMQPushConsumer consumer,String group,String topic,String tag,MessageListenerConcurrently messageListenerConcurrently)throws MQClientException{
        consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(this.nameAddr);
        consumer.subscribe(topic,tag);
        consumer.registerMessageListener(messageListenerConcurrently);
        consumer.start();
    }

}

