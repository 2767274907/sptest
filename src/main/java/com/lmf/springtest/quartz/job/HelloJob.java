package com.lmf.springtest.quartz.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmf.springtest.quartz.entity.QrtzJobDetails;
import com.lmf.springtest.quartz.service.QuartzService;
import com.lmf.springtest.utils.ApplicationContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("运行 HelloJob");
    }
}
