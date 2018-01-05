package com.wyj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wyj.entity.JobEntity;
import com.wyj.entity.Retval;
import com.wyj.service.QuartzService;
@Controller
@RequestMapping(value="/quartz")
public class QuartzController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Scheduler quartzScheduler;
    
    @Autowired
    private QuartzService quartzService;
    
    @RequestMapping("/index")
    public String index(){
        return "/quartz/list1";
    }
    
    
    /**
     * 定时列表页
     * 
     * @return
     * @throws SchedulerException
     */
    @ResponseBody
    @RequestMapping(value="/list")
    public String listJob(HttpServletRequest request,HttpServletResponse response) throws SchedulerException {
        List<JobEntity> jobInfos = this.getSchedulerJobInfo();
//        request.setAttribute("jobInfos", jobInfos);
//        List<JobEntity> jobEntities = new ArrayList<JobEntity>();
//        JobEntity jobEntity = new JobEntity();
//        jobEntity.setJobId(1l);
//        jobEntity.setJobName("job1");
//        jobEntities.add(jobEntity);
//        JobEntity jobEntity1 = new JobEntity();
//        jobEntity1.setJobId(2l);
//        jobEntity1.setJobName("job2");
//        jobEntities.add(jobEntity1);
        return JSON.toJSONString(jobInfos);
    }
    
    @ResponseBody
    @RequestMapping(value="/add")
    public Retval save(JobEntity jobEntity){
        Retval retval = Retval.newInstance();
        try {
            quartzService.addJob(jobEntity.getJobName(), jobEntity.getJobGroupName(), jobEntity.getTriggerName(), jobEntity.getTriggerGroupName(), Class.forName(jobEntity.getJobClass()), jobEntity.getCronExpression());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        
        return retval;
    }
    
    
    /**
     * 编辑job
     * 
     * @return
     * @throws SchedulerException
     * @throws ClassNotFoundException 
     */
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    public String edit(JobEntity jobEntity) throws SchedulerException, ClassNotFoundException {
//        String jobName = request.getParameter("jobName");
//        String jobGroupName = request.getParameter("jobGroupName");
//        String triggerName = request.getParameter("triggerName");
//        String triggerGroupName = request.getParameter("triggerGroupName");
//        String clazz = request.getParameter("clazz");
//        Class cls = Class.forName(clazz);
//        String cron = request.getParameter("cron");
//        
//        String oldjobName = request.getParameter("oldjobName");
//        String oldjobGroup = request.getParameter("oldjobGroup");
//        String oldtriggerName = request.getParameter("oldtriggerName");
//        String oldtriggerGroup = request.getParameter("oldtriggerGroup");
        
//        boolean result = quartzService.modifyJobTime(oldjobName, oldjobGroup, oldtriggerName, oldtriggerGroup, 
//                jobEntity.getJobName(), jobEntity.getJobGroupName(), jobEntity.getTriggerName(), jobEntity.getTriggerGroupName(), jobEntity.getCronExpression());
//        if(result){
//            request.setAttribute("message", "修改任务成功!");
//        }else{
//            request.setAttribute("message", "修改任务失败!");
//        }
//        request.setAttribute("opName", "更新任务!");
        return "quartz/message";
    }
    
    
    private List<JobEntity> getSchedulerJobInfo() throws SchedulerException {
        List<JobEntity> jobInfos = new ArrayList<JobEntity>();
        List<String> triggerGroupNames = quartzScheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
            Set<TriggerKey> triggerKeySet = quartzScheduler
                    .getTriggerKeys(GroupMatcher
                            .triggerGroupEquals(triggerGroupName));
            for (TriggerKey triggerKey : triggerKeySet) {
                Trigger t = quartzScheduler.getTrigger(triggerKey);
                if (t instanceof CronTrigger) {
                    CronTrigger trigger = (CronTrigger) t;
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jd = quartzScheduler.getJobDetail(jobKey);
                    JobEntity jobInfo = new JobEntity();
                    jobInfo.setJobName(jobKey.getName());
                    jobInfo.setJobGroupName(jobKey.getGroup());
//                    jobInfo.setJobGroup(jobKey.getGroup());
                    jobInfo.setTriggerName(triggerKey.getName());
                    jobInfo.setTriggerGroupName(triggerKey.getGroup());
                    jobInfo.setCronExpression(trigger.getCronExpression());
                    jobInfo.setNextFireTime(trigger.getNextFireTime());
                    jobInfo.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobInfo.setStartTime(trigger.getStartTime());
                    jobInfo.setEndTime(trigger.getEndTime());
                    jobInfo.setJobClass(jd.getJobClass().getCanonicalName());
                    // jobInfo.setDuration(Long.parseLong(jd.getDescription()));
                    Trigger.TriggerState triggerState = quartzScheduler
                            .getTriggerState(trigger.getKey());
                    jobInfo.setJobStatus(triggerState.toString());// NONE无,
                                                                    // NORMAL正常,
                                                                    // PAUSED暂停,
                                                                    // COMPLETE完全,
                                                                    // ERROR错误,
                                                                    // BLOCKED阻塞
                    JobDataMap map = quartzScheduler.getJobDetail(jobKey)
                            .getJobDataMap();
                    if (null != map&&map.size() != 0) {
                        jobInfo.setCount(Long.valueOf((String) map
                                .get("count")));
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
