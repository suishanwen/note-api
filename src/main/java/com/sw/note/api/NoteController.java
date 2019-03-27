package com.sw.note.api;

import com.sw.note.beans.BusinessException;
import com.sw.note.beans.Enquiry;
import com.sw.note.model.Note;
import com.sw.note.service.NoteSerivce;
import com.sw.note.util.NoteUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "笔记", description = "笔记", tags = "1")
@RestController
@RequestMapping(path = "/note")
public class NoteController {

    @Autowired
    NoteSerivce noteSerivce;
    @Autowired
    HttpServletRequest request;

    @ApiOperation(value = "获取笔记列表", notes = "获取笔记列表")
    @GetMapping("/getAll")
    public List<Note> getAll() {
        return noteSerivce.getAllPost();
    }


    @ApiOperation(value = "获取置顶笔记", notes = "获取置顶笔记")
    @GetMapping("/recommend")
    public List<Note> getRecommend() {
        return noteSerivce.getRecommend();
    }

    @ApiOperation(value = "通过id查询笔记内容", notes = "通过id查询笔记内容")
    @ApiImplicitParam(name = "id", paramType = "query", value = "id", dataType = "String")
    @GetMapping("/get")
    public Note get(@RequestParam("id") Integer id) {
        return noteSerivce.get(id);
    }

    @ApiOperation(value = "添加笔记", notes = "添加笔记")
    @PostMapping("/add")
    public Note add(@ApiParam(value = "Note", required = true) @RequestBody Note note) {
        note.setIp(NoteUtil.getIpAddr(request));
        return noteSerivce.add(note);
    }

    @ApiOperation(value = "编辑笔记", notes = "编辑笔记")
    @PostMapping("/edit")
    public Note edit(@ApiParam(value = "Note", required = true) @RequestBody Note note) {
        String ip = NoteUtil.getIpAddr(request);
        if (ip == null || !ip.equals("97.64.39.244")) {
            throw new BusinessException("当前IP没有编辑权限！");
        }
        return noteSerivce.edit(note);
    }


    @ApiOperation(value = "删除笔记", notes = "删除笔记")
    @ApiImplicitParam(name = "id", paramType = "path", value = "id", dataType = "String")
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, @ApiParam(value = "Note", required = true) @RequestBody String authCodeIn) {
        String authCode = NoteUtil.readToString("/home/authCode");
        if (authCode != null && authCodeIn.equals(authCode.trim())) {
            noteSerivce.delete(id);
        } else {
            throw new BusinessException("授权码错误！");
        }
    }

    @ApiOperation(value = "发送询问", notes = "发送询问")
    @PostMapping("enquiry")
    public void enquiry(@ApiParam(value = "文件", required = true) @RequestBody Enquiry enquiry) {
        noteSerivce.sendEnquiry(enquiry);
    }

}
