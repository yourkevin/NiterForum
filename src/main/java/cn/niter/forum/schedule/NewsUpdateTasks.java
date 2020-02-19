package cn.niter.forum.schedule;


import cn.niter.forum.enums.NewsColumnEnum;
import cn.niter.forum.provider.AliProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class NewsUpdateTasks {
    @Autowired
    private AliProvider aliProvider;

    @Scheduled(cron = "0 1 0 5,20 * ?")
    public void updateDiannaoNewsSchedule() {
        log.info("updateDiannaoNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_DIANNAO.getStrId());
        log.info("updateDiannaoNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 2 3,7,11,15,19,23 * * ?") //每天1,9,17更新
    public void updateGuoneiNewsSchedule() {
        log.info("updateGuoneiNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_GUONEI.getStrId());
        log.info("updateGuoneiNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 3 0 3 * ?")
    public void updateHulianwangNewsSchedule() {
        log.info("updateHulianwangNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_HULIANWANG.getStrId());
        log.info("updateHulianwangNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 4 0 2 * ?")
    public void updateKejiNewsSchedule() {
        log.info("updateKejiNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_KEJI.getStrId());
        log.info("updateKejiNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 5 0 1 * ?")
    public void updateKepuNewsSchedule() {
        log.info("updateKepuNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_KEPU.getStrId());
        log.info("updateKepuNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 6 2,8,14,20 * * ?")
    public void updateShumaNewsSchedule() {
        log.info("updateShumaNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_SHUMA.getStrId());
        log.info("updateShumaNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 7 0,4,9,14,19 * * ?")
    public void updateTiyuNewsSchedule() {
        log.info("updateTiyuNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_TIYU.getStrId());
        log.info("updateTiyuNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 8 1,9,17 * * ?") //每天1,9,17更新
    public void updateYuleNewsSchedule() {
        log.info("updateYuleNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_YULE.getStrId());
        log.info("updateYuleNewsSchedule stop {}", new Date());
    }

}
