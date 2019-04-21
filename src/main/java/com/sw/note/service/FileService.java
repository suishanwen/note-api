package com.sw.note.service;

import com.alibaba.fastjson.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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

    public BufferedImage statistic() {
        BufferedImage bi = new BufferedImage(150, 70, BufferedImage.TYPE_INT_RGB);
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //填充一个矩形 左上角坐标(0,0),宽70,高150;填充整张图片
        g2.fillRect(0, 0, 150, 70);
        //设置颜色
        g2.setColor(Color.WHITE);
        //填充整张图片(其实就是设置背景颜色)
        g2.fillRect(0, 0, 150, 70);
        g2.setColor(Color.RED);
        //画边框
        g2.drawRect(0, 0, 150 - 1, 70 - 1);
        //设置字体:字体、字号、大小
        g2.setFont(new Font("宋体", Font.PLAIN, 18));
        //设置背景颜色
        g2.setColor(Color.BLACK);
        //向图片上写字符串
        g2.drawString(String.valueOf(Math.random()), 3, 50);
        return bi;
    }

}

