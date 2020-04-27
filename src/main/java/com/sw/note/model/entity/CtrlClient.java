package com.sw.note.model.entity;

import javax.persistence.Id;

public class CtrlClient {
    @Id
    private String identity;
    private String uname;
    private Integer startNum;
    private Integer endNum;
    private String workerId;
    private Integer workerInput;
    private Integer tail;
    private Integer timeout;
    private Integer autoVote;
    private Integer overAuto;
    private String user;
    private Integer sort;
    private Integer state;

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

    public Integer getWorkerInput() {
        return workerInput;
    }

    public void setWorkerInput(Integer workerInput) {
        this.workerInput = workerInput;
    }

    public Integer getTail() {
        return tail;
    }

    public void setTail(Integer tail) {
        this.tail = tail;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getAutoVote() {
        return autoVote;
    }

    public void setAutoVote(Integer autoVote) {
        this.autoVote = autoVote;
    }

    public Integer getOverAuto() {
        return overAuto;
    }

    public void setOverAuto(Integer overAuto) {
        this.overAuto = overAuto;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
