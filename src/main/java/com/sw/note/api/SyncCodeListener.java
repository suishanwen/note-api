package com.sw.note.api;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.sw.note.beans.BusinessException;
import com.sw.note.beans.CtrlCode;
import com.sw.note.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import java.io.IOException;

@Component
@RabbitListener(queues = "sync")
public class SyncCodeListener {
    private static Logger logger = LoggerFactory.getLogger(SyncCodeListener.class);

    @Autowired
    VoteService voteService;

    @RabbitHandler
    @OnMessage
    public void receiver(Channel channel, Message message, String data) {
        try {
            logger.info("<=============监听到sync队列消息============>" + data);
            voteService.syncCtrlCode(data);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("CtrlSyncError：" + e.getMessage());
            if (message.getMessageProperties().getRedelivered()) {
                logger.error("消息已重复处理失败,拒绝再次接收:" + data);
                try {
                    //requeue为false,拒绝
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    //消息再次返回队列
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
