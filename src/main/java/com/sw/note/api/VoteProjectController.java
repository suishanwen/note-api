package com.sw.note.api;

import com.sw.note.beans.BackgroundData;
import com.sw.note.cache.BackGroundCache;
import com.sw.note.cache.ClientDirectCache;
import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.VoteProject;
import com.sw.note.service.VoteProjectSerivce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Api(value = "投票数据", description = "投票数据", tags = "8")
@RestController
@RequestMapping(path = "/voteProject")
public class VoteProjectController {

    private VoteProjectSerivce voteProjectSerivce;

    public VoteProjectController(VoteProjectSerivce voteProjectSerivce) {
        this.voteProjectSerivce = voteProjectSerivce;
    }


    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping(value = "query")
    public List<VoteProject> query() {
        return voteProjectSerivce.query();
    }

    @ApiOperation(value = "查询锁定", notes = "查询锁定")
    @PostMapping(value = "getLock")
    public String getLock() {
        return VoteProjectCache.getLocked();
    }

    @ApiOperation(value = "锁定项目", notes = "锁定项目")
    @ApiImplicitParam(name = "projectName", paramType = "query", value = "项目名", required = false, dataType = "String")
    @PostMapping(value = "lock")
    public void lock(@RequestParam("projectName") String projectName) {
        VoteProjectCache.setLocked(projectName);
    }

    @ApiOperation(value = "所有后台信息", notes = "所有后台信息")
    @PostMapping(value = "background")
    public Map<String, BackgroundData> background() {
        return BackGroundCache.all();
    }

    @ApiOperation(value = "交换id", notes = "交换id")
    @PostMapping(value = "borrow")
    @ApiImplicitParam(name = "user", paramType = "query", required = true, dataType = "String")
    public String borrow(@RequestParam("user") String user, @RequestBody String url) {
        return voteProjectSerivce.borrow(user, url);
    }

    @ApiOperation(value = "查询用户收入", notes = "查询用户收入")
    @PostMapping(value = "income")
    public double income(@RequestBody String user) {
        BigDecimal bd = new BigDecimal(String.valueOf(ClientDirectCache.income(user)));
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
