package cn.bcolor.rabbitmq.exchange.headers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * headers exchange publisher
 *
 * @author wangzhaohui 2017/10/26.
 * @version 1.0
 */
public class Send {

    private static final String EXCHANGE_NAME = "exchange_headers";

    private static final String MESSAGE = "hello world";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明路由名字和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "headers", false, true, null);

        //设置消息头键值对信息
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "jack");
        headers.put("age", 30);
        Builder builder = new Builder();
        builder.headers(headers);

        channel.basicPublish(EXCHANGE_NAME, "", builder.build(), MESSAGE.getBytes());
        System.out.println("Sent msg is '" + MESSAGE + "'");

        channel.close();
        connection.close();
    }
}
