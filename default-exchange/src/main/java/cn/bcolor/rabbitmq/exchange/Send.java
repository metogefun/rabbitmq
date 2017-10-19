package cn.bcolor.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送者
 *
 * @author wangzhaohui 2017/10/19.
 * @version 1.0
 */
public class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // socket连接的抽象
        Connection connection = factory.newConnection();

        // 大多数完成任务的API都在其中
        Channel channel = connection.createChannel();

        // 创建一个队列，我们可以发送消息给他
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");
    }
}
