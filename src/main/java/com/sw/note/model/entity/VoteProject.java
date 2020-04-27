package com.sw.note.model.entity;

import javax.persistence.Id;
import java.util.Date;

public class VoteProject {
    @Id
    private String projectName;
    private String type;
    private int ipDial;
    private int hot;
    private double price;
    private long totalRequire;
    private long finishQuantity;
    private long remains;
    private Integer backgroundNo;
    private String backgroundAddress;
    private String downloadAddress;
    private int restrict;
    private String idType;
    private Date refreshDate;
    private int drop;
    private int top;
    private int locked;

    public VoteProject() {
    }

    public VoteProject(String projectName, int ipDial, int hot, double price, long totalRequire, long finishQuantity, long remains, Integer backgroundNo, String backgroundAddress, String downloadAddress, String idType, Date refreshDate) {
        this.projectName = projectName;
        this.ipDial = ipDial;
        this.hot = hot;
        this.price = price;
        this.totalRequire = totalRequire;
        this.finishQuantity = finishQuantity;
        this.remains = remains;
        this.backgroundNo = backgroundNo;
        this.backgroundAddress = backgroundAddress;
        this.downloadAddress = downloadAddress;
        this.idType = idType;
        this.refreshDate = refreshDate;
    }

    public boolean remains() {
        if (remains > 0) {
            return this.remains * this.price >= 50;
        } else {
            return this.totalRequire <= 0;
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIpDial() {
        return ipDial;
    }

    public void setIpDial(int ipDial) {
        this.ipDial = ipDial;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTotalRequire() {
        return totalRequire;
    }

    public void setTotalRequire(long totalRequire) {
        this.totalRequire = totalRequire;
    }

    public long getFinishQuantity() {
        return finishQuantity;
    }

    public void setFinishQuantity(long finishQuantity) {
        this.finishQuantity = finishQuantity;
    }

    public long getRemains() {
        return remains;
    }

    public void setRemains(long remains) {
        this.remains = remains;
    }

    public Integer getBackgroundNo() {
        return backgroundNo;
    }

    public void setBackgroundNo(Integer backgroundNo) {
        this.backgroundNo = backgroundNo;
    }

    public String getBackgroundAddress() {
        return backgroundAddress;
    }

    public void setBackgroundAddress(String backgroundAddress) {
        this.backgroundAddress = backgroundAddress;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public int getRestrict() {
        return restrict;
    }

    public void setRestrict(int restrict) {
        this.restrict = restrict;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }

    public int getDrop() {
        return drop;
    }

    public void setDrop(int drop) {
        this.drop = drop;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
