package chj.idler.quartz;

import chj.idler.dao.EpisodeDOMapper;
import chj.idler.dao.VideoDOMapper;
import chj.idler.quartz.job.VideoSpiderJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class MySchedulerFactory extends StdSchedulerFactory implements ApplicationRunner {

    public void videoSpider() {
        try {
            Scheduler scheduler = this.getScheduler();
            JobDetail job = newJob(VideoSpiderJob.class)
                    .withIdentity("videoSpiderJob", "group1")
                    .build();

            CronTrigger trigger = newTrigger()
                    .withIdentity("videoSpiderTrigger", "group1")
                    .withSchedule(cronSchedule("0 1 0/1 * * ?"))
                    .build();
//            Trigger trigger = newTrigger()
//                    .withIdentity("trigger1", "group1")
//                    .startNow()
//                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        }catch (Exception e){
            this.getLog().error(e.getMessage(),e);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
            videoSpider();
    }
}
