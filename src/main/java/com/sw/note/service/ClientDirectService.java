package com.sw.note.service;

import com.sw.note.mapper.ClientDirectMapper;
import com.sw.note.model.ClientDirect;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class ClientDirectService {
    private ClientDirectMapper clientDirectMapper;

    public ClientDirectService(ClientDirectMapper clientDirectMapper) {
        this.clientDirectMapper = clientDirectMapper;
    }

    public String load(String userId, int sortNo) {
        String id = clientDirectMapper.selectIdByUser(userId, sortNo);
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString().split("-")[0];
            clientDirectMapper.insertClient(id, userId, sortNo);
        }
        return id;
    }

    public String direct(ClientDirect clientDirect) {
        clientDirect.setReportTime(new Date());
        clientDirectMapper.updateByPrimaryKeySelective(clientDirect);
        return clientDirectMapper.selectDirectById(clientDirect.getId());
    }

    public int confirm(String id, String direct) {
        clientDirectMapper.confirmDirect(id, direct);
        return 1;
    }

    public void deleteClient(String userId, int sortNo) {
        clientDirectMapper.deleteByUser(userId, sortNo);
    }


    public List<ClientDirect> selectByUserId(String userId) {
        return clientDirectMapper.selectByUserId(userId);
    }

    public void updateDirect(String ids, String direct) {
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
        g2.drawString("Active Clients Within 15m : " + activeClient, 0, 60);
        return bi;
    }


}
