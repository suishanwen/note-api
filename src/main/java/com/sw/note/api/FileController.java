package com.sw.note.api;

import com.sw.note.service.FileService;
import com.sw.note.util.DateUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;


@Api(value = "文件", description = "文件", tags = "3")
@RestController
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    FileService fileService;

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParam(name = "type", paramType = "query", value = "类型", dataType = "int")
    @PostMapping(value = "upload", headers = "content-type=multipart/form-data")
    public String upload(@RequestParam(value = "type", required = false, defaultValue = "0") int type, @ApiParam(value = "文件", required = true) @RequestBody MultipartFile file) {
        return fileService.upload(file, type);
    }


    @ApiOperation(value = "获取统计数", notes = "获取统计数")
    @GetMapping(value = "statics", produces = MediaType.IMAGE_PNG_VALUE)
    public void statistic(HttpServletResponse response) {
        String now = DateUtil.getDate();
        BufferedImage bufferedImage = fileService.statistic(now);
        try {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Etag", UUID.randomUUID().toString());
            response.setHeader("Date", now);
            ImageIO.write(bufferedImage, "PNG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
