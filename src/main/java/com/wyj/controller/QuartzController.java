package com.wyj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wyj.entity.JobDto;
import com.wyj.entity.Retval;
import com.wyj.service.QuartzService;

/**
 * 定时任务 Controller
 * 
 * 
 * @author：WangYuanJun
 * @date：2018年1月7日 下午10:15:33
 */

@Controller
@RequestMapping(value = "/quartz")
public class QuartzController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Scheduler quartzScheduler;

    @Autowired
    private QuartzService quartzService;

    @RequestMapping("/index")
    public String index() {
        return "/quartz/list";
    }

    /**
     * 定时列表页
     * 
     * @return
     * @throws SchedulerException
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    public String listJob(HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        List<JobDto> jobInfos = this.getSchedulerJobInfo();
        return JSON.toJSONString(jobInfos);
    }

    /**
     * 新建job
     * 
     * @param jobDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add")
    public Retval save(JobDto jobDto) {
        Retval retval = Retval.newInstance();
        try {
            quartzService.addJob(jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), Class.forName(jobDto.getJobClass()), jobDto.getCronExpression());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return retval;
    }

    /**
     * 编辑job
     * 
     * @param jobDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Retval edit(JobDto jobDto) {
        Retval retval = Retval.newInstance();
        try {
            boolean result = quartzService.modifyJobTime(jobDto.getOldJobName(), jobDto.getOldJobGroupName(), jobDto.getOldTriggerName(), jobDto.getOldTriggerGroupName(), jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), jobDto.getCronExpression());
            if (result) {
                retval.put("message", "修改任务成功!");
            } else {
                retval.put("message", "修改任务失败!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return retval;
    }

    /**
     * 暂停job
     * 
     * @param jobName
     * @param jobGroupName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/stopJob", method = RequestMethod.POST)
    public Retval stopJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Retval retval = Retval.newInstance();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            retval.fail();
            retval.put("message", "暂停失败");
        } else {
            try {
                quartzService.pauseJob(jobName, jobGroupName);
                retval.put("message", "暂停成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        return retval;
    }

    /**
     * 恢复job
     * 
     * @param jobName
     * @param jobGroupName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
    public Retval resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Retval retval = Retval.newInstance();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            retval.fail();
            retval.put("message", "恢复失败");
        } else {
            try {
                quartzService.resumeJob(jobName, jobGroupName);
                retval.put("message", "恢复成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        }

        return retval;
    }

    /**
     * 删除job
     * 
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @return
     */
    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    @ResponseBody
    public Retval deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName, @RequestParam("triggerName") String triggerName, @RequestParam("triggerGroupName") String triggerGroupName) {
        Retval retval = Retval.newInstance();

        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName) || StringUtils.isEmpty(triggerName) || StringUtils.isEmpty(triggerGroupName)) {
            retval.fail();
            retval.put("message", "删除失败");
        } else {
            try {
                quartzService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                retval.put("message", "删除成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        return retval;
    }

    private List<JobDto> getSchedulerJobInfo() throws SchedulerException {
        List<JobDto> jobInfos = new ArrayList<JobDto>();
        List<String> triggerGroupNames = quartzScheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
            Set<TriggerKey> triggerKeySet = quartzScheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
            for (TriggerKey triggerKey : triggerKeySet) {
                Trigger t = quartzScheduler.getTrigger(triggerKey);
                if (t instanceof CronTrigger) {
                    CronTrigger trigger = (CronTrigger) t;
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jd = quartzScheduler.getJobDetail(jobKey);
                    JobDto jobInfo = new JobDto();
                    jobInfo.setJobName(jobKey.getName());
                    jobInfo.setJobGroupName(jobKey.getGroup());
                    jobInfo.setTriggerName(triggerKey.getName());
                    jobInfo.setTriggerGroupName(triggerKey.getGroup());
                    jobInfo.setCronExpression(trigger.getCronExpression());
                    jobInfo.setNextFireTime(trigger.getNextFireTime());
                    jobInfo.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobInfo.setStartTime(trigger.getStartTime());
                    jobInfo.setEndTime(trigger.getEndTime());
                    jobInfo.setJobClass(jd.getJobClass().getCanonicalName());
                    // jobInfo.setDuration(Long.parseLong(jd.getDescription()));
                    Trigger.TriggerState triggerState = quartzScheduler.getTriggerState(trigger.getKey());
                    jobInfo.setJobStatus(triggerState.toString());// NONE无,
                                                                  // NORMAL正常,
                                                                  // PAUSED暂停,
                                                                  // COMPLETE完全,
                                                                  // ERROR错误,
                                                                  // BLOCKED阻塞
                    JobDataMap map = quartzScheduler.getJobDetail(jobKey).getJobDataMap();
                    if (null != map && map.size() != 0) {
                        jobInfo.setCount(Long.valueOf((String) map.get("count")));
                        jobInfo.setJobDataMap(map);
                    } else {
                        jobInfo.setJobDataMap(new JobDataMap());
                    }
                    jobInfos.add(jobInfo);
                }
            }
        }
        return jobInfos;
    }
}
