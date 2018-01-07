package com.wyj.entity;

import java.io.Serializable;
import java.util.Date;

import org.quartz.JobDataMap;

/**
 * Job Dto 与数据库无关联
 * 
 * @author：WangYuanJun
 * @date：2018年1月6日 下午10:16:59
 */
public class JobDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String jobName;// 任务名

    private String jobGroupName;// 任务组

    private String triggerName;// 触发器名称

    private String triggerGroupName;// 触发器组名称

    private String cronExpression;// cron表达式

    private Date previousFireTime;// 上次运行时间

    private Date nextFireTime;// 下次运行时间

    private String jobStatus;// 任务状态

    // private Long duration;

    private Date startTime;

    private Date endTime;

    private String jobClass;

    private Long count;

    private JobDataMap jobDataMap;

    private String oldJobName;// 任务名

    private String oldJobGroupName;// 任务组

    private String oldTriggerName;// 触发器名称

    private String oldTriggerGroupName;// 触发器组名称

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getOldJobName() {
        return oldJobName;
    }

    public void setOldJobName(String oldJobName) {
        this.oldJobName = oldJobName;
    }

    public String getOldJobGroupName() {
        return oldJobGroupName;
    }

    public void setOldJobGroupName(String oldJobGroupName) {
        this.oldJobGroupName = oldJobGroupName;
    }

    public String getOldTriggerName() {
        return oldTriggerName;
    }

    public void setOldTriggerName(String oldTriggerName) {
        this.oldTriggerName = oldTriggerName;
    }

    public String getOldTriggerGroupName() {
        return oldTriggerGroupName;
    }

    public void setOldTriggerGroupName(String oldTriggerGroupName) {
        this.oldTriggerGroupName = oldTriggerGroupName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
