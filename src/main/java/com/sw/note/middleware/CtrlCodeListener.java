package com.sw.note.middleware;

import com.rabbitmq.client.Channel;
import com.sw.note.util.MqUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;

//@Component
//@RabbitListener(queues = "ctrl")
public class CtrlCodeListener {

    @RabbitHandler
    @OnMessage
    public void receiver(Channel channel, Message message, String data) {
        MqUtil.consume(channel, message, data, MqUtil.TYPE_CLIENT);
    }
}
