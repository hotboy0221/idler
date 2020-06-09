package chj.idler.service;

import chj.idler.response.BusinessException;
import chj.idler.service.model.VideoModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

public interface VideoService {
    VideoModel analyseURL(String url)throws BusinessException, JsonProcessingException, MQClientException,InterruptedException, RemotingException, MQBrokerException;
    List<VideoModel> getVideoList(Integer userId) ;
    List<VideoModel> selectAllVideoNewest() ;
    void updateEpisodes()throws MQClientException,InterruptedException, RemotingException, MQBrokerException;
    List<String> selectSubEmails(Integer videoId);
    void episodesSpider(VideoModel videoModel);
}
