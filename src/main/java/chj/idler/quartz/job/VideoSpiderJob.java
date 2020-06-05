package chj.idler.quartz.job;

import chj.idler.dao.EpisodeDOMapper;
import chj.idler.dao.VideoDOMapper;
import chj.idler.dataobject.EpisodeDO;
import chj.idler.dataobject.VideoDO;
import chj.idler.service.VideoService;
import chj.idler.service.impl.VideoServiceImpl;
import chj.idler.service.model.VideoModel;
import chj.idler.util.SpringContextUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class VideoSpiderJob implements Job {
    //还不知道job如何正确获取日志
    private Logger logger=LoggerFactory.getLogger(VideoSpiderJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        try {
            VideoService videoService=(VideoServiceImpl)SpringContextUtil.getBean("videoServiceImpl");
            videoService.updateEpisodes();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
