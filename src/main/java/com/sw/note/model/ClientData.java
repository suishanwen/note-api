package com.sw.note.model;

import javax.persistence.Transient;

public class ClientData {
    private String id;
    private String date;
    private String reward;
    private String detail;
    @Transient
    private String sortNo;

    public ClientData() {
    }

    public String getId() {
        return id;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
