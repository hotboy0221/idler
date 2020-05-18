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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@CrossOrigin(allowCredentials="true", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/video")
public class VideoController extends BaseController {

    @Autowired
    private VideoService videoService;

    @ResponseBody
    @RequestMapping(value="/search")
    public CommonReturnType search(@RequestParam(name="url")String url)throws BusinessException {

        VideoModel videoModel=videoService.analyseURL(url);
        if(videoModel==null)throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"暂不支持该网址");
        return  CommonReturnType.create(convertToVideoVO(videoModel));
    }

    private VideoVO convertToVideoVO(VideoModel videoModel){
        VideoVO videoVO=new VideoVO();
        if (videoModel==null)return null;
        BeanUtils.copyProperties(videoModel,videoVO);
        return videoVO;
    }


}
