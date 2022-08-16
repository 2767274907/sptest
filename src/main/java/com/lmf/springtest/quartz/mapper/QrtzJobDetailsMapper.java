package com.lmf.springtest.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmf.springtest.ftp.dto.JobAndTriggerDto;
import com.lmf.springtest.quartz.entity.QrtzJobDetails;

import java.util.List;

/**
 * (QrtzJobDetails)表数据库访问层
 *
 * @author lmf
 * @since 2022-08-09 11:19:33
 */
public interface QrtzJobDetailsMapper extends BaseMapper<QrtzJobDetails> {

    /**
     * 获取作业和触发器详细信息
     * @return
     */
    Page<QrtzJobDetails> getJobAndTriggerDetails(Page<QrtzJobDetails> page);
}
