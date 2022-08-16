package com.lmf.springtest.quartz;

import com.lmf.springtest.quartz.job.HelloJob;
import org.quartz.*;

import java.util.Date;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;
/**
 * @author lmf
 * @date 2022/8/9
 */
public class TestClass {

    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();
        sched.start();

        // 定义工作并将其绑定到我们的 HelloJob 类
        JobDetail job = newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .build();
        // 现在触发作业运行，然后每 40 秒运行一次
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();

        Trigger t = newTrigger()

                .withIdentity("myTrigger")
                .forJob("myJob")
                .withSchedule(dailyAtHourAndMinute(9, 30)) // execute job daily at 9:30
                .modifiedByCalendar("myHolidays") // but not on holidays
                .build();
        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
    }


}
