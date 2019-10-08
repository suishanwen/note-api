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
import tk.mybatis.mapper.util.StringUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoteProjectTimerAq {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VoteProjectSerivce voteProjectSerivce;
    @Autowired
    private ClientDirectService clientDirectService;
    @Autowired
    RestTemplate restTemplate;

    private String dataSource = "http://www.mianfei99.cn/task.aspx";

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
                if (!e.getMessage().contains("SocketTimeoutException")) {
                    clientDirectService.bugReport("server-aq", e.getMessage());
                }
            }
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
        Elements tableElements = Jsoup.parse(html).select("table[id='MainContent_GridView2']").select("tr");
        Date date = new Date();
        for (int i = 1; i < tableElements.size(); i++) {
            Elements tdElements = tableElements.get(i).select("td");
            if (tdElements.get(0).text().contains("没有符合条件的任务")) {
                continue;
            }
            Element backgroundTd = tdElements.get(11);
            String backGrounInfo = backgroundTd.text();
            if (backGrounInfo != null) {
                if (backGrounInfo.toUpperCase().contains("Q")) {
                    continue;
                }
                Matcher matcher = Pattern.compile("\\d{4}").matcher(tdElements.get(7).text());
                if (matcher.find()) {
                    backGrounInfo = matcher.group(0);
                } else {
                    matcher = Pattern.compile("\\d{3}").matcher(tdElements.get(7).text());
                    if (matcher.find()) {
                        backGrounInfo = matcher.group(0);
                    } else {
                        backGrounInfo = null;
                    }
                }
            }
            Integer backGroundNo = Strings.isNotEmpty(backGrounInfo) ? Integer.parseInt(backGrounInfo) : null;
            int ipDial = tdElements.get(4).text().contains("不换") ? 0 : 1;
            double price = Double.parseDouble(tdElements.get(5).text());
            String downloadAddress = tdElements.get(8).select("a").attr("href").trim();
            String backgroundAddress = tdElements.get(7).select("a").attr("href");
            String projectName = tdElements.get(2).text();
            int hot = 0;
            String hotText = tdElements.get(3).text().replace("(", "").replace(")", "");
            if (StringUtil.isNotEmpty(hotText) && !"-".equals(hotText)) {
                hot = Double.valueOf(hotText).intValue();
            }
            Element amountInfoTd = tdElements.get(9);
            String[] amountArr = amountInfoTd.attr("title").split("\n")[0].split("/");
            long finishQuantity = Long.parseLong(amountArr[0]);
            long totalRequire = Long.parseLong(amountArr[1]);
            long remains = Long.parseLong(amountInfoTd.text());
            VoteProject voteProject = new VoteProject(projectName, ipDial, hot, price, totalRequire, finishQuantity, remains, backGroundNo, backgroundAddress, downloadAddress, "AQ", date);
            voteProjectList.add(voteProject);
        }
        return voteProjectList;
    }

    private void saveVoteProject(List<VoteProject> voteProjectList) {
        VoteProjectCache.setListAq(voteProjectList);
    }

}


