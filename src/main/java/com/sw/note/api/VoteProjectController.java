package com.sw.note.api;

import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.VoteProject;
import com.sw.note.service.VoteProjectSerivce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @PostMapping(value = "lock")
    public void lock(@RequestBody String projectName) {
        VoteProjectCache.setLocked(projectName);
    }
}
