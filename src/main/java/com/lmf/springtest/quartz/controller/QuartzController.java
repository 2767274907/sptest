package com.lmf.springtest.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmf.springtest.ftp.dto.JobAndTriggerDto;
import com.lmf.springtest.quartz.entity.QrtzJobDetails;
import com.lmf.springtest.quartz.service.QuartzService;
import com.lmf.springtest.utils.result.BaseResult;
import com.lmf.springtest.utils.result.ECode;
import com.lmf.springtest.utils.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "/quartz")
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    /**
     * 新增定时任务
     *
     * @param jName 任务名称
     * @param jGroup 任务组
     * @param tName 触发器名称
     * @param tGroup 触发器组
     * @param cron cron表达式
     * @return ResultMap
     */
    @PostMapping(path = "/addjob")
    @ResponseBody
    public BaseResult<String> addjob(String jName, String jGroup, String tName, String tGroup, String cron) {
        try {
            quartzService.addjob(jName, jGroup, tName, tGroup, cron);
            return new BaseResult(ECode.OK ,"添加任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(ECode.FAIL ,"添加任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param jName 任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/pausejob")
    @ResponseBody
    public BaseResult<String> pausejob(String jName, String jGroup) {
        try {
            quartzService.pausejob(jName, jGroup);
            return new BaseResult(ECode.OK ,"暂停任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new BaseResult(ECode.FAIL ,"暂停任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param jName 任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/resumejob")
    @ResponseBody
    public BaseResult<String> resumejob(String jName, String jGroup) {
        try {
            quartzService.resumejob(jName, jGroup);
            return new BaseResult(ECode.OK ,"恢复任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new BaseResult(ECode.FAIL ,"恢复任务失败");
        }
    }

    /**
     * 重启任务
     *
     * @param jName 任务名称
     * @param jGroup 任务组
     * @param cron cron表达式
     * @return ResultMap
     */
    @PostMapping(path = "/reschedulejob")
    @ResponseBody
    public BaseResult<String> rescheduleJob(String jName, String jGroup, String cron) {
        try {
            quartzService.rescheduleJob(jName, jGroup, cron);
            return new BaseResult(ECode.OK ,"重启任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new BaseResult(ECode.FAIL ,"重启任务失败");
        }
    }

    /**
     * 删除任务
     *
     * @param jName 任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/deletejob")
    @ResponseBody
    public BaseResult<String> deletejob(String jName, String jGroup) {
        try {
            quartzService.deletejob(jName, jGroup);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new BaseResult(ECode.FAIL ,"删除任务失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResult(ECode.OK ,"删除任务成功");
    }

    /**
     * 查询任务
     *
     * @param pageNum 页码
     * @param pageSize 每页显示多少条数据
     * @return Map
     */
    @GetMapping(path = "/queryjob")
    @ResponseBody
    public BaseResult<?> queryjob(Integer pageNum, Integer pageSize) {
        Page<QrtzJobDetails> pageInfo = quartzService.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(pageInfo.getTotal())) {
            map.put("JobAndTrigger", pageInfo);
            map.put("number", pageInfo.getTotal());
            return new BaseResult(ResultCode.OK , map);
        }
        return new BaseResult(ECode.FAIL ,"查询任务成功失败，没有数据");
    }
}
