package com.sw.note.model;

import java.util.Date;

public class VmReset {

    private String sortNo;
    private String status;
    private Date date;

    public VmReset() {
    }


    public VmReset(String sortNo, String status, Date date) {
        this.sortNo = sortNo;
        this.status = status;
        this.date = date;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
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
