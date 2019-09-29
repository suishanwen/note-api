package com.sw.note.timer;

import com.sw.note.cache.ClientDirectCache;
import com.sw.note.config.RestTemplateConfig;
import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.mapper.VmResetMapper;
import com.sw.note.model.ClientDirect;
import com.sw.note.model.VmReset;
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
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ClientDirectTimerReset {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientDirectService clientDirectService;
    @Autowired
    private VmResetMapper vmResetMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${vm.host}")
    public String host;
    @Value("${vm.username}")
    public String username;
    @Value("${vm.password}")
    public String password;

    private boolean running = false;

    public void run() {
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            try {
                reset();
            } catch (Exception e) {
                clientDirectService.bugReport("server-reset", e.getMessage());
            }
            running = false;
        };
        ScheduledExecutorUtil.scheduleAtFixedRate(runnable, 0, 3600);
    }

    private MultiValueMap<String, String> generateData(String instance) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("host", host);
        map.add("username", username);
        map.add("password", password);
        map.add("instance", instance);
        return map;
    }

    private void reset() {
        long current = System.currentTimeMillis();
        List<ClientDirect> clientDirectDropedList = ClientDirectCache.selectByUserId("sw").stream()
                .filter(clientDirect -> current - clientDirect.getUpdateTime().getTime() > 2000 * 1000)
                .collect(Collectors.toList());
        clientDirectDropedList.forEach(clientDirect -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                    generateData(clientDirect.getInstance()), headers);
            //发送请求，设置请求返回数据格式为String
            String status = "0";
            try {
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://tl.bitcoinrobot.cn/reset/", request, String.class);
                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    status = responseEntity.getBody();
                }
            } catch (Exception e) {
                logger.warn("vm-reset:" + e.getMessage());
            }
            try {
                vmResetMapper.insert(new VmReset(String.valueOf(clientDirect.getSortNo()), status, new Date()));
            } catch (Exception e) {
                logger.warn("vm-reset-insert:" + e.getMessage());
            }
        });
    }
}

