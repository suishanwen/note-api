package com.sw.note.middleware;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.sw.note.beans.BusinessException;
import com.sw.note.beans.CtrlCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import java.io.IOException;

@Component
@RabbitListener(queues = "ctrl")
public class CtrlCodeListener {
    private static Logger logger = LoggerFactory.getLogger(CtrlCodeListener.class);

    @RabbitHandler
    @OnMessage
    public void receiver(Channel channel, Message message, String data){
        try {
            logger.info("<=============监听到ctrl_code队列消息============>" + data);
            CtrlCode ctrlCode = JSON.parseObject(data, CtrlCode.class);
            String identity = ctrlCode.getIdentity();
            if (CtrlDeliverSocket.wsClientMap.containsKey(identity)) {
                CtrlDeliverSocket ctrlDeliverSocket = CtrlDeliverSocket.wsClientMap.get(identity);
                ctrlDeliverSocket.sendMessage(ctrlCode.getCode());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                throw new BusinessException(identity + ",不在线！");
            }
        } catch (Exception e) {
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
