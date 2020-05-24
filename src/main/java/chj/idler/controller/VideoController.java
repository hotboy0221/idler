package chj.idler.controller;

import chj.idler.controller.viewobject.VideoVO;
import chj.idler.response.BusinessException;
import chj.idler.response.CommonReturnType;
import chj.idler.response.EmBusinessError;
import chj.idler.service.VideoService;
import chj.idler.service.model.VideoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(allowCredentials="true", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/videos")
public class VideoController extends BaseController {

    @Autowired
    private VideoService videoService;
    /*
    *  爬取视频
    * */
    @ResponseBody
    @RequestMapping(value="/search",method = RequestMethod.GET,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType search(@RequestParam(name="url")String url)throws BusinessException {
        VideoModel videoModel=videoService.analyseURL(url);
        if(videoModel==null)throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"暂不支持该网址");
        return  CommonReturnType.create(convertToVideoVO(videoModel));
    }

    /*
    *  添加爬取视频到用户列表
    * */
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.POST,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType add(@RequestParam(name="token")String token,@PathVariable(value="video_id")Integer videoId){

        return null;
    }

    /*
    *  删除用户列表的视频
    * */
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.DELETE,consumes =CONTENT_TYPE_FORMED)
    public CommonReturnType delete(@RequestParam(name="token")String token,@PathVariable(value="video_id")Integer videoId){

        return null;
    }

    /*
    *  订阅邮箱通知
    * */
    @ResponseBody
    @RequestMapping(value="/{video_id}/users",method = RequestMethod.PATCH,consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType subscribe(@RequestParam(name="token")String token,
                                      @PathVariable(value="video_id")Integer videoId,
                                      @RequestParam(name="sub")Byte sub){

        return null;
    }

    private VideoVO convertToVideoVO(VideoModel videoModel){
        if (videoModel==null)return null;
        VideoVO videoVO=new VideoVO();
        BeanUtils.copyProperties(videoModel,videoVO);
        return videoVO;
    }


}
