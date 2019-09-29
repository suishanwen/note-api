package com.sw.note.config;

import com.sw.note.timer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartRunner implements ApplicationRunner {

    @Autowired
    private VoteProjectTimerQ7 voteProjectTimerQ7;
    @Autowired
    private VoteProjectTimerTx voteProjectTimerTx;
    @Autowired
    private VoteProjectTimerAq voteProjectTimerAq;
    @Autowired
    private VoteProjectTimerMerge voteProjectTimerMerge;
    @Autowired
    private ClientDirectTimerSync clientDirectTimerSync;
    @Autowired
    private ClientDirectTimerReset clientDirectTimerReset;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        clientDirectTimerSync.run();
        voteProjectTimerTx.run();
        voteProjectTimerQ7.run();
        voteProjectTimerAq.run();
        voteProjectTimerMerge.run();
        clientDirectTimerReset.run();
    }
}
