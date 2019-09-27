package com.sw.note.config;

import com.sw.note.timer.ClientDirectTimerSync;
import com.sw.note.timer.VoteProjectTimerMerge;
import com.sw.note.timer.VoteProjectTimerQ7;
import com.sw.note.timer.VoteProjectTimerTx;
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
    private VoteProjectTimerMerge voteProjectTimerMerge;
    @Autowired
    private ClientDirectTimerSync clientDirectTimerSync;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        clientDirectTimerSync.run();
        voteProjectTimerQ7.run();
        voteProjectTimerTx.run();
        voteProjectTimerMerge.run();
    }
}
