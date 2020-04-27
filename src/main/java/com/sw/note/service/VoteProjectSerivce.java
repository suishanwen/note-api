package com.sw.note.service;

import com.google.common.collect.Lists;
import com.sw.note.model.BackgroundData;
import com.sw.note.cache.BackGroundCache;
import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.entity.VoteProject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VoteProjectSerivce {


    @Autowired
    private RestTemplate restTemplate;

    public List<VoteProject> query() {
        return VoteProjectCache.get();
    }

    public String borrow(String user, String url) {
        List<String> idList = Lists.newArrayList();
        BackgroundData backgroundData = BackGroundCache.find(url);
        if (backgroundData == null || System.currentTimeMillis() - backgroundData.getMills() > 60 * 1000) {
            String html = restTemplate.getForObject(url, String.class);
            if (html != null && url.contains("mmrj")) {
                Elements tableElements = Jsoup.parse(html).select("tr[class='t1'],tr[class='t2']").select("td:eq(0)>font");
                idList = tableElements.stream().map(Element::text).collect(Collectors.toList());
                backgroundData = new BackgroundData(idList, System.currentTimeMillis());
                BackGroundCache.set(url, backgroundData);
            }
            if (html != null && url.contains("120.25.13.127")) {
                Elements tableElements = Jsoup.parse(html).select("tr[bgcolor='White'],tr[bgcolor='Azure']").select("td:eq(0)").select("font[color='Blue']");
                idList = tableElements.stream().map(Element::text).collect(Collectors.toList());
                backgroundData = new BackgroundData(idList, System.currentTimeMillis());
                BackGroundCache.set(url, backgroundData);
            }
        }
        if (backgroundData != null) {
            idList = backgroundData.getOnlineIdList();
            return randomId(user, idList);
        }
        return "";
    }

    private String randomId(String user, List<String> idList) {
        if (user.equals("1")) {
            idList = idList.stream().filter(id -> {
                return id.contains("TX-18-") || id.contains("AQ-14-") || id.contains("Q7-43-");
            }).collect(Collectors.toList());
        } else {
            idList = idList.stream().filter(id -> {
                return id.contains("TX-111-") || id.contains("AQ-111-") || id.contains("Q7-129-");
            }).collect(Collectors.toList());
        }
        int len = idList.size();
        if (len > 0) {
            return idList.get(new Random().nextInt(len));
        }
        return "";
    }
}
