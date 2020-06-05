package chj.idler.service;

import chj.idler.response.BusinessException;
import chj.idler.service.model.UserModel;

public interface UserService {
    UserModel login(String username,String password)throws BusinessException;
    UserModel getUser(Integer id)throws BusinessException;
    void register(UserModel userModel)throws BusinessException;
    void forgot(UserModel userModel)throws BusinessException;
}
