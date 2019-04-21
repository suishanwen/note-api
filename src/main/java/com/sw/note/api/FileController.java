package com.sw.note.api;

import com.sw.note.service.FileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;


@Api(value = "文件", description = "文件", tags = "3")
@RestController
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    FileService fileService;

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParam(name = "type", paramType = "query", value = "类型", dataType = "int")
    @PostMapping(value = "upload", headers = "content-type=multipart/form-data")
    public void upload(@RequestParam("type") int type, @ApiParam(value = "文件", required = true) @RequestBody MultipartFile file) {
        fileService.upload(file, type);
    }


    @ApiOperation(value = "获取统计数", notes = "获取统计数")
    @GetMapping(value = "statistic",produces = MediaType.IMAGE_PNG_VALUE)
    public void statistic(HttpServletResponse response) {
        BufferedImage bufferedImage =  fileService.statistic();
        try {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            ImageIO.write(bufferedImage,"PNG",response.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
