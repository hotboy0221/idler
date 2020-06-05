package chj.idler.service.impl;

import chj.idler.dao.UserDOMapper;
import chj.idler.dataobject.UserDO;
import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.UserService;
import chj.idler.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Override
    public UserModel login(String username,String password) throws BusinessException {
        loginValidator(username,password);
        UserDO userDO=userDOMapper.selectByEmailOrUsername(username);
        if(userDO==null||!userDO.getPassword().equals(password)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserModel userModel = convertToUserModel(userDO);
        return userModel;
    }

    @Override
    public UserModel getUser(Integer id) throws BusinessException {
        if(id==null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        UserModel userModel=convertToUserModel(userDOMapper.selectByPrimaryKey(id));
        return userModel;
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
        registerValidator(userModel);
        UserDO userDO = convertToUserDO(userModel);
        if(userDOMapper.selectByEmail(userModel.getEmail())!=null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"邮箱已被注册");
        if(userDOMapper.selectByUsername(userModel.getUsername())!=null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名已被注册");
        if( userDOMapper.insertSelective(userDO)==0)throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"注册失败");
    }

    @Override
    public void forgot(UserModel userModel) throws BusinessException {
        forgotValidator(userModel);
        //发送邮件
        return ;
    }

    private void registerValidator(UserModel userModel) throws BusinessException {
        if (userModel == null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        if (StringUtils.isEmpty(userModel.getUsername()))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名不能为空");
        if (!userModel.getUsername().matches("^([a-zA-Z0-9_\\u4e00-\\u9fa5]{4,16})$"))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名需为4-16位数字或字母或" +
                    "下划线");
        if (StringUtils.isEmpty(userModel.getEmail()) || !userModel.getEmail().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱格式不正确");
        if (StringUtils.isEmpty(userModel.getPassword()))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "密码不能为空");
    }

    private void loginValidator(String username,String password) throws BusinessException {
        if (StringUtils.isEmpty(username))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名不能为空");
        if (StringUtils.isEmpty(password))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"密码不能为空");
    }

    private void forgotValidator(UserModel userModel) throws BusinessException {
        if (userModel == null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        if (StringUtils.isEmpty(userModel.getEmail()) || userModel.getEmail().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"))
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱格式不正确");
    }

    private UserDO convertToUserDO(UserModel userModel) {
        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserModel convertToUserModel(UserDO userDO) {
        if (userDO == null) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        return userModel;
    }
}
