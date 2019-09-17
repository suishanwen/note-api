package com.sw.note.timer;

import com.google.common.collect.Lists;
import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.VoteProject;
import com.sw.note.service.VoteProjectSerivce;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoteProjectTimer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VoteProjectSerivce voteProjectSerivce;
    @Autowired
    RestTemplate restTemplate;

    private String dataSource = "http://pt.yhxz777.com/rw/tp/index.asp";

    private boolean running = false;

    public void run() {
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            String html = getHtml();
            List<VoteProject> voteProjectList = analyzeHtml(html);
            saveVoteProject(voteProjectList);
            running = false;
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
    }


    private String getHtml() {
        String url = dataSource + "?t=" + Math.random();
        Charset charset = Charset.forName("gb2312");
        url = new String(url.getBytes(StandardCharsets.UTF_8), charset);
        return restTemplate.getForObject(url, String.class);
    }


    private List<VoteProject> analyzeHtml(String html) {
        List<VoteProject> voteProjectList = Lists.newArrayList();
        Elements tableElements = Jsoup.parse(html).select("table").select("tr");
        Date date = new Date();
        for (int i = 1; i < tableElements.size(); i++) {
            Elements tdElements = tableElements.get(i).select("td");
            Element projectTd = tdElements.get(1);
            Element amountTd = tdElements.get(2);
            Element backgroundTd = tdElements.get(3);
            String backGrounInfo = backgroundTd.text();
            if (backGrounInfo != null) {
                if (backGrounInfo.toUpperCase().contains("Q")) {
                    continue;
                }
                Matcher matcher = Pattern.compile("\\d{3}").matcher(backGrounInfo);
                if (matcher.find()) {
                    backGrounInfo = matcher.group(0);
                } else {
                    backGrounInfo = null;
                }
            }
            Integer backGroundNo = Strings.isNotEmpty(backGrounInfo) ? Integer.parseInt(backGrounInfo) : null;
            Elements projectInfo = projectTd.select("span");
            Elements addressInfo = projectTd.select("a");
            String[] autoInfo = projectInfo.get(2).selectFirst("span").text().split("-");
            if (!"自动".equals(autoInfo[0])) {
                continue;
            }
            int ipDial = autoInfo[1].contains("不换") ? 0 : 1;
            double price = Double.parseDouble(projectInfo.get(1).select("span").text().split("/")[0]);
            Element projectElement = projectInfo.get(0);
            String downloadAddress = addressInfo.get(0).attr("href");
            String backgroundAddress = addressInfo.get(1).attr("href");
            String projectName = projectElement.text().split("-")[3];
            String[] amountInfo = amountTd.text().split("：");
            int hot = Integer.parseInt(amountInfo[1].split(" ")[0]);
            long finishQuantity = Long.parseLong(amountInfo[2].split("/")[0]);
            long totalRequire = Long.parseLong(amountInfo[2].split("/")[1]);
            long remains = totalRequire >= finishQuantity ? totalRequire - finishQuantity : 0;
            VoteProject voteProject = new VoteProject(projectName, ipDial, hot, price, totalRequire, finishQuantity, remains, backGroundNo, backgroundAddress, downloadAddress, "Q7", date);
            voteProjectList.add(voteProject);
        }
        return voteProjectList;
    }

    private void saveVoteProject(List<VoteProject> voteProjectList) {
        VoteProjectCache.set(voteProjectList);
    }
}


