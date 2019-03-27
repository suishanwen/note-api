package com.sw.note.service;

import com.alibaba.fastjson.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

}

