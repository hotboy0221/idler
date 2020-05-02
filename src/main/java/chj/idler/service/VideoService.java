package chj.idler.service;

import chj.idler.response.BusinessException;
import chj.idler.service.model.VideoModel;

public interface VideoService {
    VideoModel analyseURL(String url)throws BusinessException;
}
