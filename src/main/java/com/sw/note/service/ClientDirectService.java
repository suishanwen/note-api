package com.sw.note.service;

import com.sw.note.mapper.BugReportMapper;
import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.BugReport;
import com.sw.note.model.ClientDirect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ClientDirectService {
    private ClientDirectMapper clientDirectMapper;
    private BugReportMapper bugReportMapper;

    public ClientDirectService(ClientDirectMapper clientDirectMapper, BugReportMapper bugReportMapper) {
        this.clientDirectMapper = clientDirectMapper;
        this.bugReportMapper = bugReportMapper;
    }


    public String load(String userId, int sortNo, String version) {
        String id = clientDirectMapper.selectIdByUser(userId, sortNo);
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString().split("-")[0];
            clientDirectMapper.insertClient(id, userId, sortNo);
        }
        clientDirectMapper.setVersion(id, version);
        return id;
    }

    public String direct(ClientDirect clientDirect) {
        //clientDirect.setReportTime(new Date());
        clientDirectMapper.updateByPrimaryKeySelective(clientDirect);
        return clientDirectMapper.selectDirectById(clientDirect.getId());
    }

    public int confirm(String id, String direct) {
        direct = direct.trim();
        clientDirectMapper.confirmDirect(id, direct);
        return 1;
    }

    public void deleteClient(String userId, int sortNo) {
        clientDirectMapper.deleteByUser(userId, sortNo);
    }


    public List<ClientDirect> selectByUserId(String userId) {
        String superUser = "root";
        if (superUser.equals(userId)) {
            return clientDirectMapper.selectAllCient();
        } else {
            return clientDirectMapper.selectByUserId(userId);
        }
    }

    @Transactional
    public void updateDirect(String ids, String direct) {
        direct = direct.trim();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            clientDirectMapper.updateDirect(id, direct);
        }
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
        g2.drawString("Active Clients : " + activeClient, 55, 60);
        return bi;
    }

    public void bugReport(String id, String msg) {
        bugReportMapper.insert(new BugReport(id, msg));
    }

}
