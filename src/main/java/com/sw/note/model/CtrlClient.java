package com.sw.note.model;

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
    private Integer autoVote;
    private Integer overAuto;
    private String user;
    private Integer sort;
    private Integer state;

    public CtrlClient() {
    }
}
