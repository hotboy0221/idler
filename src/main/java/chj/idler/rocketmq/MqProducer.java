package chj.idler.rocketmq;


import chj.idler.service.model.UserModel;
import chj.idler.service.model.VideoModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Component
public class MqProducer {

    private DefaultMQProducer producer;


    @Value("${rocketmq.nameserver.addr}")
    private String nameAddr;

    @Value("${rocketmq.topic.mail}")
    private String mailTopic;

    @Value("${rocketmq.topic.spider}")
    private String spiderTopic;
    @PostConstruct
    public void init() throws MQClientException {
        //做mq producer的初始化
        producer = new DefaultMQProducer("mail_producer_group");
        producer.setNamesrvAddr(nameAddr);
        producer.setRetryTimesWhenSendFailed(5);
        producer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if (producer != null) {
                producer.shutdown();
            }
        }));
    }


    //分发通知邮件
    public boolean notifyVideoMail(VideoModel videoModel)
            throws MQClientException,InterruptedException, RemotingException, MQBrokerException {
        Message message = new Message(mailTopic, "notifyVideo", JSON.toJSONBytes(videoModel));
        producer.send(message);
        return true;
    }
    public boolean registerMail(String registerToken,UserModel userModel)throws MQClientException,InterruptedException, RemotingException, MQBrokerException{
        Map<String,Object> map=new HashMap<>();
        map.put("registerToken",registerToken);
        map.put("userModel",userModel);
        Message message = new Message(mailTopic,"register", JSON.toJSONBytes(map));
        producer.send(message);
        return true;
    }
    //通知收集影视每一集信息
    public boolean collectEpisodes(VideoModel videoModel)throws MQClientException,InterruptedException, RemotingException, MQBrokerException{
        Message message=new Message(spiderTopic,"episodes",JSON.toJSONBytes(videoModel));
        producer.send(message);
        return true;
    }
}
