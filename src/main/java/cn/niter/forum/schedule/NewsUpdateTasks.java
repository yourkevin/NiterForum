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

    @Scheduled(cron = "0 7 10 * * ?") //每天10点7分更新
    public void updateDiannaoNewsSchedule() {
        log.info("updateDiannaoNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_DIANNAO.getStrId(),1);
        log.info("updateDiannaoNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 2 8,13,18,23 * * ?") //每天1,9,17更新
    public void updateGuoneiNewsSchedule() {
        log.info("updateGuoneiNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_GUONEI.getStrId(),20);
        log.info("updateGuoneiNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 8 11 * * ?") //每天11点8分更新
    public void updateHulianwangNewsSchedule() {
        log.info("updateHulianwangNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_HULIANWANG.getStrId(),1);
        log.info("updateHulianwangNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 9 12 * * ?") //每天12点9分更新
    public void updateKejiNewsSchedule() {
        log.info("updateKejiNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_KEJI.getStrId(),1);
        log.info("updateKejiNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 10 13 * * ?")//每天13点10分更新
    public void updateKepuNewsSchedule() {
        log.info("updateKepuNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_KEPU.getStrId(),1);
        log.info("updateKepuNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 6 6,13,20 * * ?")
    public void updateShumaNewsSchedule() {
        log.info("updateShumaNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_SHUMA.getStrId(),15);
        log.info("updateShumaNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 7 7,15,23 * * ?")
    public void updateTiyuNewsSchedule() {
        log.info("updateTiyuNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_TIYU.getStrId(),15);
        log.info("updateTiyuNewsSchedule stop {}", new Date());
    }
    @Scheduled(cron = "0 8 12,22 * * ?") //每天1,9,17更新
    public void updateYuleNewsSchedule() {
        log.info("updateYuleNewsSchedule start {}", new Date());
        aliProvider.autoGetNews(NewsColumnEnum.NEWS_COLUMN_YULE.getStrId(),15);
        log.info("updateYuleNewsSchedule stop {}", new Date());
    }

}
