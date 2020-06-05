package chj.idler.service;

import chj.idler.response.BusinessException;
import chj.idler.service.model.VideoSubModel;

import java.util.List;

public interface VideoSubService {
    void insert(VideoSubModel videoSubModel)throws BusinessException;
    void updateSub(VideoSubModel videoSubModel)throws BusinessException;
    void delete(Integer userId,Integer videoId)throws BusinessException;

}
