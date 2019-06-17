package com.sw.note.api;

import com.sw.note.util.MailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

@Api(value = "邮件", description = "邮件", tags = "6")
@RestController
@RequestMapping(path = "/email")
public class EmailController {
    @ApiOperation(value = "发送", notes = "发送")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiver", paramType = "query", value = "receiver", required = true, dataType = "String"),
            @ApiImplicitParam(name = "subject", paramType = "query", value = "subject", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", paramType = "query", value = "content", required = true, dataType = "String")
    })
    @GetMapping("/send")
    public String send(@RequestParam("receiver") String receiver, @RequestParam("subject") String subject, @RequestParam("content") String content) {
        if (StringUtil.isEmpty(receiver)) {
            return "0";
        }
        MailUtils cn = new MailUtils();
        cn.setAddress("controlservice@sina.com", receiver, subject);
        try {
            cn.send("smtp.sina.com", "controlservice@sina.com", "a123456", content);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
