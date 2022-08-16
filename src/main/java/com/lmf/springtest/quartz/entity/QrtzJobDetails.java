package com.lmf.springtest.quartz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (QrtzJobDetails)表实体类
 *
 * @author lmf
 * @since 2022-08-09 11:04:05
 */
@Data
@TableName("qrtz_job_details")
public class QrtzJobDetails {

    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String schedName;
    private String isDurable;
    private String isNonconcurrent;
    private String isUpdateData;
    private String requestsRecovery;
    private String jobData;

}
