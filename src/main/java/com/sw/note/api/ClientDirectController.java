package com.sw.note.api;

import com.sw.note.model.ClientDirect;
import com.sw.note.service.ClientDirectService;
import com.sw.note.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Api(value = "指令", description = "指令", tags = "7")
@RestController
@RequestMapping(path = "/direct")
public class ClientDirectController {

    private ClientDirectService clientDirectService;

    public ClientDirectController(ClientDirectService clientDirectService) {
        this.clientDirectService = clientDirectService;
    }


    @ApiOperation(value = "加载客户端信息", notes = "加载客户端信息")
    @GetMapping(value = "load")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sortNo", paramType = "query", required = true, dataType = "int"),
    })
    public String load(String userId, int sortNo) {
        return clientDirectService.load(userId, sortNo);
    }

    @ApiOperation(value = "上报并获取指令", notes = "上报并获取指令")
    @PostMapping(value = "report")
    public String direct(@RequestBody ClientDirect clientDirect) {
        return clientDirectService.direct(clientDirect);
    }

    @ApiOperation(value = "确认指令", notes = "确认指令")
    @GetMapping(value = "confirm")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "direct", paramType = "query", required = true, dataType = "String"),
    })
    public int confirm(String id, String direct) {
        return clientDirectService.confirm(id, direct);
    }

    @ApiOperation(value = "通过用户Id查看列表", notes = "通过用户Id查看列表")
    @GetMapping(value = "selectByUserId")
    @ApiImplicitParam(name = "userId", paramType = "query", required = true, dataType = "String")
    public List<ClientDirect> selectByUserId(String userId) {
        return clientDirectService.selectByUserId(userId);
    }

    @ApiOperation(value = "删除", notes = "删除")
    @PostMapping(value = "delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sortNo", paramType = "query", required = true, dataType = "int"),
    })
    public void deleteClient(String userId, int sortNo) {
        clientDirectService.deleteClient(userId, sortNo);
    }

    @ApiOperation(value = "指令下发", notes = "指令下发")
    @PostMapping(value = "send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "direct", paramType = "query", required = true, dataType = "String"),
    })
    public void selectByUserId(String ids, String direct) {
        clientDirectService.updateDirect(ids, direct);
    }

    @ApiOperation(value = "获取活跃客户端数", notes = "获取活跃客户端数")
    @GetMapping(value = "active", produces = MediaType.IMAGE_PNG_VALUE)
    public void activeClient(HttpServletResponse response) {
        String now = DateUtil.getDate();
        BufferedImage bufferedImage = clientDirectService.activeClient(now);
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