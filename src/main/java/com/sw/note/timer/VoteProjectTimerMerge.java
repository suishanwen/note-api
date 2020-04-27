package com.sw.note.timer;

import com.sw.note.cache.VoteProjectCache;
import com.sw.note.util.ScheduledExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class VoteProjectTimerMerge {

    private boolean running = false;

    public void run() {
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            VoteProjectCache.merge();
            running = false;
        };
        ScheduledExecutorUtil.scheduleAtFixedRate(runnable, 5, 10);
    }
}


