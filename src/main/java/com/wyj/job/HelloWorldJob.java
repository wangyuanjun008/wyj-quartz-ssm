package com.wyj.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * Job任务
 * 
 * 
 * @author：WangYuanJun
 * @date：2018年1月5日 下午10:22:42
 */
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("----hello world---" + new Date());
    }

}
