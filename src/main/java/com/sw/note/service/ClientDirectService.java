package com.sw.note.service;

import com.sw.note.cache.ClientDirectCache;
import com.sw.note.ip.IPSeeker;
import com.sw.note.mapper.BugReportMapper;
import com.sw.note.mapper.ClientDataMapper;
import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.entity.BugReport;
import com.sw.note.model.entity.ClientData;
import com.sw.note.model.entity.ClientDirect;
import com.sw.note.util.NoteUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
public class ClientDirectService {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ClientDirectMapper clientDirectMapper;
    @Autowired
    BugReportMapper bugReportMapper;
    @Autowired
    ClientDataMapper clientDataMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    ClientDirectCache clientDirectCache;


    public void synchronize(List<ClientDirect> clientDirectList) {
        clientDirectList.forEach(clientDirect -> {
            clientDirectMapper.updateByPrimaryKey(clientDirect);
            clientDirectCache.sync(clientDirect.getId(), true);
        });
    }


    public String selectLocationById(String id) {
        return clientDataMapper.selectLocationById(id);
    }


    public String setInstance(String id, String instance) {
        clientDirectCache.setVal(id, "instance", instance);
//        clientDirectMapper.setInstance(id, instance);
        return instance;
    }

    public String load(String userId, int sortNo, String version) {
        String id = clientDirectMapper.selectIdByUser(userId, sortNo);
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString().split("-")[0];
            clientDirectMapper.insertClient(id, userId, sortNo);
            clientDirectCache.put(new ClientDirect(id, userId, sortNo));
        }
        clientDirectCache.setVal(id, "version", version);
//        clientDirectMapper.setVersion(id, version);
        return id;
    }

    public String direct(Map<String, Object> map) {
        String id = (String) map.get("id");
        clientDirectCache.report(id, map);
        Object direct = clientDirectCache.getVal(id, "direct");
        if (direct == null) {
            return "";
        }
//        clientDirectMapper.updateByPrimaryKeySelective(clientDirect);
//        return clientDirectMapper.selectDirectById(clientDirect.getId());
        return direct.toString();
    }

    public int confirm(String id, String direct) {
        direct = direct.trim();
        ClientDirect clientDirect = clientDirectCache.get(id);
        if (direct.equals(clientDirect.getDirect())) {
            clientDirectCache.setVal(id, "direct", "");

        }
//        clientDirectMapper.confirmDirect(id, direct);
        return 1;
    }

    public void deleteClient(String id) {
        clientDirectMapper.deleteById(id);
        clientDirectCache.remove(id);
    }

    public List<ClientDirect> selectByUserId(String userId) {
        return clientDirectMapper.selectByUserId(userId);
    }

    public void updateDirect(String ids, Integer all, String direct) {
        direct = direct.trim();
        String[] idArray = ids.split(",");
        if (all == 1) {
            clientDirectCache.setValAll("direct", direct);
//            clientDirectMapper.updateDirectAll(direct);
        } else {
            for (String id : idArray) {
                clientDirectCache.setVal(id, "direct", direct);
//                clientDirectMapper.updateDirect(id, direct);
            }
        }
    }

    public long checkVersion() {
        return clientDirectMapper.checkVersion();
    }

    public void upgradeLatest() {
        clientDirectMapper.updateLatestVersion(clientDirectMapper.selectLatestVersion());
    }

    public int dataUpload(ClientData clientData) {
        clientData.setIp(NoteUtil.getIpAddr(request));
        clientData.setLocation(IPSeeker.getInstance().getAddress(clientData.getIp()));
        try {
            clientDataMapper.insert(clientData);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<ClientData> selectDataByUserId(String userId, String date) {
        return clientDataMapper.selectDataByUserId(userId, date);
    }

    public String selectDetail(String id, String date) {
        return clientDataMapper.selectDetail(id, date);
    }

    public BufferedImage activeClient(String now) {
        int activeClient = clientDirectMapper.selectActive();
        int width = 300;
        int height = 80;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //填充一个矩形 左上角坐标(0,0),宽350,高230;填充整张图片
        g2.fillRect(0, 0, width, height);
        //设置颜色
        g2.setColor(Color.WHITE);
        //填充整张图片(其实就是设置背景颜色)
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.WHITE);
        //画边框
        g2.drawRect(0, 0, width - 1, height - 1);
        //设置字体:字体、字号、大小
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        //设置背景颜色
        g2.setColor(Color.red);
        //向图片上写字符串
        g2.drawString(String.format("%s", now), 15, 30);
        g2.setColor(Color.red);
        g2.drawString("Active Robots : " + activeClient, 55, 60);
        return bi;
    }

    public void bugReport(String id, String msg) {
        try {
            bugReportMapper.insert(new BugReport(id, msg));
        } catch (Exception e) {
            logger.warn("bugReport:" + e.getMessage());
        }
    }

}
