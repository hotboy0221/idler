package chj.idler.controller;

import chj.idler.annotation.ValidatePermission;
import chj.idler.controller.viewobject.VideoVO;
import chj.idler.response.BusinessException;
import chj.idler.response.CommonReturnType;
import chj.idler.response.EmBusinessError;
import chj.idler.rocketmq.MqProducer;
import chj.idler.service.VideoService;
import chj.idler.service.VideoSubService;
import chj.idler.service.model.UserModel;
import chj.idler.service.model.VideoModel;
import chj.idler.service.model.VideoSubModel;
import chj.idler.util.JedisClusterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(allowCredentials="true", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/videos")
public class VideoController extends BaseController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoSubService videoSubService;
    @Autowired
    private MqProducer mqProducer;
    /*
    *  爬取视频
    * */
    @ResponseBody
    @RequestMapping(value="/search",method = RequestMethod.GET,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType search(@RequestParam(name="url")String url)throws BusinessException ,JsonProcessingException, MQClientException,InterruptedException, RemotingException, MQBrokerException{
        VideoModel videoModel=videoService.analyseURL(url);
        if(videoModel==null)throw new BusinessException(EmBusinessError.URL_NOT_SUPPORT,"暂不支持该网址");
        return  CommonReturnType.create(convertToVideoVO(videoModel));
    }

    /*
    *  添加爬取视频到用户列表
    * */
    @ValidatePermission
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.POST,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType add(@RequestHeader(value="token")String token,
                                @PathVariable(value="video_id")Integer videoId)throws BusinessException,JsonProcessingException {
        UserModel userModel= JedisClusterUtil.get(token,UserModel.class);
        VideoSubModel videoSubModel=new VideoSubModel();
        videoSubModel.setUserId(userModel.getId());
        videoSubModel.setVideoId(videoId);
        videoSubModel.setAddTime(System.currentTimeMillis()/1000);
        videoSubModel.setStatus(new Byte("1"));
        videoSubService.insert(videoSubModel);
        return CommonReturnType.create(null);
    }

    /*
    *  删除用户列表的视频
    * */
    @ValidatePermission
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.DELETE,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType delete(@RequestHeader(value="token")String token,
                                   @PathVariable(value="video_id")Integer videoId)throws JsonProcessingException,BusinessException{
        UserModel userModel= JedisClusterUtil.get(token,UserModel.class);
        videoSubService.delete(userModel.getId(),videoId);
        return CommonReturnType.create(null);
    }

    /*
    *  订阅邮箱通知
    * */
    @ValidatePermission
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.PATCH,consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType subscribe(@RequestHeader(value="token")String token,
                                      @PathVariable(value="video_id")Integer videoId,
                                      @RequestParam(name="sub")Byte sub)throws JsonProcessingException,BusinessException{
        UserModel userModel= JedisClusterUtil.get(token,UserModel.class);
        VideoSubModel videoSubModel=new VideoSubModel();
        videoSubModel.setUserId(userModel.getId());
        videoSubModel.setVideoId(videoId);
        videoSubModel.setStatus(sub);
        videoSubService.updateSub(videoSubModel);
        return CommonReturnType.create(null);
    }

    /*
     *  获取用户视频列表
     * */
    @ValidatePermission
    @ResponseBody
    @RequestMapping(value="",method = RequestMethod.GET,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType getVideoList(@RequestHeader(value="token")String token)throws JsonProcessingException,BusinessException{
        UserModel userModel= JedisClusterUtil.get(token,UserModel.class);
        List<VideoModel> videoModelList=videoService.getVideoList(userModel.getId());
        List<VideoVO> videoVOList=videoModelList.stream().map(videoModel ->this.convertToVideoVO(videoModel)).collect(Collectors.toList());
        return CommonReturnType.create(videoVOList);
    }

    private VideoVO convertToVideoVO(VideoModel videoModel){
        if (videoModel==null)return null;
        VideoVO videoVO=new VideoVO();
        BeanUtils.copyProperties(videoModel,videoVO);
        return videoVO;
    }
/*
    @ResponseBody
    @RequestMapping(value = "/test" )
    public CommonReturnType getUser(){
        try {
            VideoModel videoModel = new VideoModel();
            videoModel.setName("哈哈1");
            videoModel.setId(1123);
            mqProducer.newVideoNotify(videoModel);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonReturnType.create(null);
    }*/
}
