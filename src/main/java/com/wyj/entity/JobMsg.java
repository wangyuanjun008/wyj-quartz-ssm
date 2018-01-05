package com.wyj.entity;

import java.io.Serializable;

public class JobMsg implements Serializable{
    

    /**
     * 
     */
    private static final long serialVersionUID = 547945218942601997L;

    private Long jobMsgId;
    
    private String msg;
    
    private String trg;
    
    private String mark;

    public Long getJobMsgId() {
        return jobMsgId;
    }

    public void setJobMsgId(Long jobMsgId) {
        this.jobMsgId = jobMsgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTrg() {
        return trg;
    }

    public void setTrg(String trg) {
        this.trg = trg;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
    
}
