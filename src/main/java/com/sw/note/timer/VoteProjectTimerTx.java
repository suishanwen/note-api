package com.sw.note.timer;

import com.google.common.collect.Lists;
import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.VoteProject;
import com.sw.note.service.ClientDirectService;
import com.sw.note.service.VoteProjectSerivce;
import com.sw.note.util.ScheduledExecutorUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoteProjectTimerTx {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VoteProjectSerivce voteProjectSerivce;
    @Autowired
    private ClientDirectService clientDirectService;
    @Autowired
    RestTemplate restTemplate;

    private String dataSource = "http://2019.txtpw.com/rwlb8.asp";

    private boolean running = false;

    public void run() {
        Runnable runnable = () -> {
            if (running) {
                return;
            }
            running = true;
            try {
                String html = getHtml();
                List<VoteProject> voteProjectList = analyzeHtml(html);
                saveVoteProject(voteProjectList);
            } catch (Exception e) {
                if(!e.getMessage().contains("SocketTimeoutException")){
                    clientDirectService.bugReport("server", e.getMessage());
                }            }
            running = false;
        };
        ScheduledExecutorUtil.scheduleAtFixedRate(runnable, 0, 10);
    }


    private String getHtml() {
        String url = dataSource + "?t=" + Math.random();
        Charset charset = Charset.forName("gb2312");
        url = new String(url.getBytes(StandardCharsets.UTF_8), charset);
        return restTemplate.getForObject(url, String.class);
    }


    private List<VoteProject> analyzeHtml(String html) {
        List<VoteProject> voteProjectList = Lists.newArrayList();
        Elements tableElements = Jsoup.parse(html).select("table[style=\"border-left:1px solid #009cec; border-right:1px solid #009cec; background-color:#FFF\"]").select("tr");
        Date date = new Date();
        for (int i = 1; i < tableElements.size(); i++) {
            Elements tdElements = tableElements.get(i).select("td");
            Element backgroundTd = tdElements.get(12);
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
            int ipDial = tdElements.get(4).text().contains("不换") ? 0 : 1;
            double price = Double.parseDouble(tdElements.get(5).text());
            String downloadAddress = tdElements.get(9).selectFirst("a").attr("href").replace(" ", "");
            String backgroundAddress = tdElements.get(8).selectFirst("a").attr("href").replace(" ", "");
            String projectName = tdElements.get(2).text().trim();
            int hot = 0;
            if (!tdElements.get(3).text().contains("新任务") && !tdElements.get(3).text().contains("未知")) {
                hot = Double.valueOf(tdElements.get(3).text().split("/")[0].substring(1).trim()).intValue();
            }
            long finishQuantity = 0;
            long totalRequire = 0;
            long remains = Long.parseLong(tdElements.get(7).text());
            VoteProject voteProject = new VoteProject(projectName, ipDial, hot, price, totalRequire, finishQuantity, remains, backGroundNo, backgroundAddress, downloadAddress, "TX", date);
            voteProjectList.add(voteProject);
        }
        return voteProjectList;
    }

    private void saveVoteProject(List<VoteProject> voteProjectList) {
        VoteProjectCache.setListTx(voteProjectList);
    }
}


