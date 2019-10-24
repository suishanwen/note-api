package com.sw.note.api;

import com.alibaba.fastjson.JSONObject;
import com.sw.note.util.MailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.util.StringUtil;

@Api(value = "速递", description = "速递", tags = "6")
@RestController
@RequestMapping(path = "/email")
public class EmailController {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${telegram.token}")
    public String token;
    @Value("${telegram.chatId}")
    public String chatId;

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

    @ApiOperation(value = "发送", notes = "发送")
    @PostMapping("/telegram")
    public boolean send(@RequestBody String msg) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", token, chatId, msg);
        String resp = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(resp);
        if (jsonObject.containsKey("ok")) {
            return jsonObject.getBooleanValue("ok");
        }
        return false;
    }
}
