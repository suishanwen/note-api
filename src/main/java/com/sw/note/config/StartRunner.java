package com.sw.note.config;

import com.sw.note.timer.VoteProjectTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartRunner implements ApplicationRunner {

    @Autowired
    private VoteProjectTimer voteProjectTimer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        voteProjectTimer.run();
    }
}
