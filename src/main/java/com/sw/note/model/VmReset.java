package com.sw.note.model;

import java.util.Date;

public class VmReset {

    private int sortNo;
    private String status;
    private Date date;

    public VmReset() {
    }


    public VmReset(int sortNo, String status, Date date) {
        this.sortNo = sortNo;
        this.status = status;
        this.date = date;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
