package chj.idler.service.impl;

import chj.idler.dao.VideoSubDOMapper;
import chj.idler.dataobject.VideoSubDO;
import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import chj.idler.service.VideoSubService;
import chj.idler.service.model.VideoSubModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VideoSubServiceImpl implements VideoSubService {

    @Autowired
    private VideoSubDOMapper videoSubDOMapper;

    @Override
    public void insert(VideoSubModel videoSubModel) throws BusinessException {
        VideoSubDO videoSubDO=convertToVideoSubDO(videoSubModel);
        if(videoSubDO==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        int f=0;
        try{
            f=videoSubDOMapper.insert(videoSubDO);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.VIDEO_IS_SUB);
        }catch (Exception e){
            throw e;
        }
        if(f==0)throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
    }

    @Override
    public void updateSub(VideoSubModel videoSubModel) throws BusinessException {
        VideoSubDO videoSubDO=convertToVideoSubDO(videoSubModel);
        if(videoSubDO==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        if(videoSubDOMapper.updateByPrimaryKeySelective(videoSubDO)==0)throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
    }

    @Override
    public void delete(Integer userId,Integer videoId) throws BusinessException {
        if(videoSubDOMapper.deleteByPrimaryKey(userId,videoId)==0)throw new BusinessException(EmBusinessError.VIDEO_NOT_SUB);
    }


    private VideoSubDO convertToVideoSubDO(VideoSubModel videoSubModel){
        if(videoSubModel==null)return null;
        VideoSubDO videoSubDO=new VideoSubDO();
        BeanUtils.copyProperties(videoSubModel,videoSubDO);
        return videoSubDO;
    }
}
