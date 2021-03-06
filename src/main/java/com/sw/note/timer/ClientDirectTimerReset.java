package com.sw.note.timer;

import com.sw.note.cache.BackGroundCache;
import com.sw.note.cache.ClientDirectCache;
import com.sw.note.cache.VoteProjectCache;
import com.sw.note.mapper.VmResetMapper;
import com.sw.note.model.entity.ClientDirect;
import com.sw.note.model.entity.VmReset;
import com.sw.note.service.ClientDirectService;
import com.sw.note.util.ScheduledExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientDirectTimerReset {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientDirectService clientDirectService;
    @Autowired
    private VmResetMapper vmResetMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ClientDirectCache clientDirectCache;

    @Value("${vm.host}")
    public String host;
    @Value("${vm.username}")
    public String username;
    @Value("${vm.password}")
    public String password;
    @Value("${vm.username1}")
    public String username1;
    @Value("${vm.password1}")
    public String password1;

    private boolean running = false;

    public void run() {
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            try {
                reset();
                BackGroundCache.clear();
            } catch (Exception e) {
                clientDirectService.bugReport("server-reset", e.getMessage());
            }
            running = false;
        };
        ScheduledExecutorUtil.scheduleAtFixedRate(runnable, 0, 300);
    }

    private MultiValueMap<String, String> generateData(String user, String instance) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("host", host);
        map.add("username", "1".equals(user) ? username1 : username);
        map.add("password", "1".equals(user) ? password1 : password);
        map.add("instance", instance);
        return map;
    }

    private void reset() {
        long current = System.currentTimeMillis();
        long timeout = 45 * 60 * 1000;
        long resetInterval = 2 * 60 * 60 * 1000;
        List<ClientDirect> clientDirectList = clientDirectService.selectByUserId("root");
        long nonCount = clientDirectList.stream()
                .filter(clientDirect -> "无".equals(clientDirect.getProjectName()))
                .count();
        if (nonCount < clientDirectList.size() / 2) {
            clientDirectList.stream()
                    .filter(clientDirect -> !StringUtils.isEmpty(clientDirect.getInstance()) &&
                            (current - clientDirect.getUpdateTime().getTime() > timeout) &&
                            (StringUtils.isEmpty(VoteProjectCache.getLocked()) ||
                                    (!StringUtils.isEmpty(clientDirect.getProjectName()) &&
                                            !clientDirect.getProjectName().contains(VoteProjectCache.getLocked()))))
                    .collect(Collectors.toList())
                    .forEach(clientDirect -> {
                        VmReset vmReset = vmResetMapper.getLastReset(clientDirect.getUserId(), String.valueOf(clientDirect.getSortNo()));
                        if (vmReset == null || current - vmReset.getDate().getTime() > resetInterval) {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                                    generateData(clientDirect.getUserId(), clientDirect.getInstance()), headers);
                            //发送请求，设置请求返回数据格式为String
                            String status = "0";
                            try {
                                logger.warn(clientDirect.getUserId() + ": 重启" + clientDirect.getSortNo() + "号");
                                ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://tl.bitcoinrobot.cn/reset/", request, String.class);
                                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                                    status = responseEntity.getBody();
                                }
                            } catch (Exception e) {
                                logger.warn("vm-reset:" + e.getMessage());
                            }
                            try {
                                vmResetMapper.insert(new VmReset(clientDirect.getUserId(), String.valueOf(clientDirect.getSortNo()), status, new Date()));
                            } catch (Exception e) {
                                logger.warn("vm-reset-insert:" + e.getMessage());
                            }
                        }
                    });
        }
    }
}


