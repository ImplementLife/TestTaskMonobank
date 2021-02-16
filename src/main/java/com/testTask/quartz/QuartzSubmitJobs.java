package com.testTask.quartz;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_FIVE_MINUTES = "0 0/1 * ? * * *";

    @Bean(name = "execute")
    public JobDetailFactoryBean jobMemberStats() {
        return QuartzConfig.createJobDetail(Exe.class, "Update RequestPay status");
    }
    
    /*
    @Bean(name = "exeTrigger")
    public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("execute") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, 60000, "Exe Trigger");
    }
    */

    @Bean(name = "exeTrigger")
    public CronTriggerFactoryBean triggerMemberClassStats(@Qualifier("execute") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_FIVE_MINUTES, "Exe Trigger");
    }
}
