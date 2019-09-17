package com.sw.note.api;

import com.sw.note.middleware.CtrlDeliverSocket;
import com.sw.note.model.CtrlClient;
import com.sw.note.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Api(value = "投票App", description = "投票App", tags = "4")
@RestController
@RequestMapping(path = "/vote")
public class VoteController {

    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @ApiOperation(value = "获取用户控制列表", notes = "获取用户控制列表")
    @PostMapping(value = "queryList")
    public List<CtrlClient> queryList(@RequestBody String user) {
        return voteService.queryList(user);
    }

    @ApiOperation(value = "获取", notes = "获取")
    @PostMapping(value = "query")
    public CtrlClient query(@RequestBody String identity) {
        return voteService.query(identity);
    }


    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "add")
    public void add(@RequestBody CtrlClient ctrlClient) {
        voteService.add(ctrlClient);
    }

    @ApiOperation(value = "上传", notes = "上传")
    @PostMapping(value = "report")
    public void report(@RequestBody CtrlClient ctrlClient) {
        voteService.report(ctrlClient);
    }

    @ApiOperation(value = "获取在线机器", notes = "获取在线机器")
    @PostMapping(value = "online")
    public ConcurrentHashMap<String, CtrlDeliverSocket> online() {
        return CtrlDeliverSocket.wsClientMap;
    }
}
