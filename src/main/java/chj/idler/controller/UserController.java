package chj.idler.controller;

import chj.idler.controller.viewobject.UserVO;
import chj.idler.response.BusinessException;
import chj.idler.response.CommonReturnType;
import chj.idler.service.UserService;
import chj.idler.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType login(@RequestParam(name = "username") String username,
                                  @RequestParam(name = "password") String password) throws BusinessException,NoSuchAlgorithmException, UnsupportedEncodingException{
        UserModel userModel=userService.login(username,EncodeByMd5(password));
        String token= UUID.randomUUID().toString().replace("-","");
        return CommonReturnType.create(token);
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType register(@RequestParam(name = "username") String username,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "email") String email) throws BusinessException,NoSuchAlgorithmException, UnsupportedEncodingException{

        UserModel userModel=new UserModel();
        userModel.setUsername(username);
        userModel.setPassword(EncodeByMd5(password));
        userModel.setEmail(email);
//        userModel.setTelephone(telephone);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    @ResponseBody
    @RequestMapping(value = "/forgot", method = RequestMethod.PATCH, consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType forgot(@RequestParam(name = "email") String email)throws BusinessException {
        UserModel userModel=new UserModel();
        userModel.setEmail(email);
        userService.forgot(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        StringBuffer sb=new StringBuffer();
        sb.append("chj");
        sb.append( base64en.encode(md5.digest(str.getBytes("utf-8"))));
        sb.append("cm");
        return sb.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET, consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType getUser(@PathVariable(value="user_id")Integer userId)throws BusinessException {
        UserModel userModel=userService.getUser(userId);
        UserVO userVO=convertToUserVO(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertToUserVO(UserModel userModel){
        if(userModel==null)return null;
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
