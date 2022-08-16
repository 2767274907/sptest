package com.lmf.springtest.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmf.springtest.ftp.dto.JobAndTriggerDto;
import com.lmf.springtest.quartz.entity.QrtzJobDetails;
import org.quartz.*;

/**
 * @author lmf
 * @date 2022/8/8
 */
public interface QuartzService {

    Page<QrtzJobDetails> getJobAndTriggerDetails(Integer pageNum, Integer pageSize);

    void addjob(String jName, String jGroup, String tName, String tGroup, String cron);

    void pausejob(String jName, String jGroup) throws SchedulerException;

    void resumejob(String jName, String jGroup) throws SchedulerException;

    void rescheduleJob(String jName, String jGroup, String cron) throws SchedulerException;

    void deletejob(String jName, String jGroup) throws Exception;


}
