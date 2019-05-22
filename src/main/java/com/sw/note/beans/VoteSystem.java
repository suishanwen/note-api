package com.sw.note.beans;


public class VoteSystem {
    private Integer pollingRatio;

    public VoteSystem() {
    }

    public VoteSystem(Integer pollingRatio) {
        this.pollingRatio = pollingRatio;
    }

    public VoteSystem pollingRatio(Integer pollingRatio) {
        this.pollingRatio = pollingRatio;
        return this;
    }


    public Integer getPollingRatio() {
        return pollingRatio;
    }

    public void setPollingRatio(Integer pollingRatio) {
        this.pollingRatio = pollingRatio;
    }


    public VoteSystem build() {
        return new VoteSystem(pollingRatio);
    }
}
