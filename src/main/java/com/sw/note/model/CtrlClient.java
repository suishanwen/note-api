package com.sw.note.model;

import javax.persistence.Id;

public class CtrlClient {
    @Id
    private String identity;
    private String uname;
    private Integer startNum;
    private Integer endNum;
    private String workerId;
    private Integer tail;
    private String arrDrop;
    private Integer autoVote;
    private String taskInfos;
    private String user;
    private Integer sort;

    public CtrlClient() {
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Integer getTail() {
        return tail;
    }

    public void setTail(Integer tail) {
        this.tail = tail;
    }

    public String getArrDrop() {
        return arrDrop;
    }

    public void setArrDrop(String arrDrop) {
        this.arrDrop = arrDrop;
    }

    public Integer getAutoVote() {
        return autoVote;
    }

    public void setAutoVote(Integer autoVote) {
        this.autoVote = autoVote;
    }

    public String getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(String taskInfos) {
        this.taskInfos = taskInfos;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
