package com.sw.note.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.sw.note.beans.Enquiry;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String path0 = "/etc/nginx/html/file/upload/";
    private static final String path1 = "/etc/nginx/html/file/";
    private static final String home = "/home/";

    public String upload(MultipartFile file, int type) {
        String fileName = file.getOriginalFilename();
        String uploadPath = fileName.contains("sw-") ? home : type == 0 ? path0 : path1;
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = file.getInputStream();
            File f = new File(uploadPath + fileName);
            logger.info("upload " + fileName + " to " + uploadPath);
            os = new FileOutputStream(f);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            String url = "http://bitcoinrobot.cn/file/upload/" + fileName;
            return "{url:'" + url + "', error:'0',message:''}";
        } catch (IOException e) {
            e.printStackTrace();
            return String.format("{url:'', error:'1',message: %s}", e.getMessage());
        } finally {
            IOUtils.close(os);
            IOUtils.close(ins);
        }
    }

    public BufferedImage statistic(String now) {
        Ini ini = new Ini();
        Double today = 0D;
        Double sum = 0D;
        try {
            ini.load(new URL("https://bitcoinrobot.cn/balance/ok/config.ini"));
            String count = ini.get("okb_usdt-stat").get("count");
            List<Double> data = JSON.parseArray(count, Double.class);
            today = data.get(data.size() - 1);
            sum = data.stream().mapToDouble(x -> x).sum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bi = new BufferedImage(300, 230, BufferedImage.TYPE_INT_RGB);
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //填充一个矩形 左上角坐标(0,0),宽300,高230;填充整张图片
        g2.fillRect(0, 0, 300, 230);
        //设置颜色
        g2.setColor(Color.WHITE);
        //填充整张图片(其实就是设置背景颜色)
        g2.fillRect(0, 0, 300, 230);
        g2.setColor(Color.WHITE);
        //画边框
        g2.drawRect(0, 0, 300 - 1, 230 - 1);
        //设置字体:字体、字号、大小
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        //设置背景颜色
        g2.setColor(Color.RED);
        //向图片上写字符串
        g2.drawString("收益统计", 100, 50);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("时间:%s", now), 3, 100);
        g2.drawString(String.format("今日:%.3f", today), 3, 150);
        g2.drawString(String.format("本月:%.3f", sum), 3, 200);
        return bi;
    }

}

