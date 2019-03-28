package com.sw.note.api;

import com.sw.note.service.FileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
