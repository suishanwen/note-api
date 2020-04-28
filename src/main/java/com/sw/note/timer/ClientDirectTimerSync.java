package com.sw.note.timer;

import com.sw.note.cache.ClientDirectCache;
import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.entity.ClientDirect;
import com.sw.note.service.ClientDirectService;
import com.sw.note.util.ScheduledExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientDirectTimerSync {

    @Autowired
    private ClientDirectService clientDirectService;
    @Autowired
    private ClientDirectMapper clientDirectMapper;

    private boolean running = false;

    public void run() {
        List<ClientDirect> clientDirectList = clientDirectMapper.selectAllCient();
        ClientDirectCache.init(clientDirectList);
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            try {
                clientDirectService.synchronize(ClientDirectCache.unSynchronized());
            } catch (Exception e) {
                clientDirectService.bugReport("server-sync", e.getMessage());
            }
            running = false;
        };
        ScheduledExecutorUtil.scheduleAtFixedRate(runnable, 0, 60);
    }
}


