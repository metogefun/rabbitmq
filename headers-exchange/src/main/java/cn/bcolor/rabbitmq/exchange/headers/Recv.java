package cn.bcolor.rabbitmq.exchange.headers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * headers exchange customer
 *
 * @author wangzhaohui 2017/10/26.
 * @version 1.0
 */
public class Recv {

    private static final String EXCHANGE_NAME = "exchange_headers";
    private static final String QUEUE_NAME = "headers_test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明路由名字和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "headers", false, true, null);
        //创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        //设置消息头键值对信息
        Map<String, Object> headers = new HashMap<>();
        //这里x-match有两种类型
        //all:表示所有的键值对都匹配才能接受到消息
        //any:表示只要有键值对匹配就能接受到消息
        headers.put("x-match", "all");
        headers.put("name", "jack");
        headers.put("age", 31);

        //把队列绑定到路由上并指定headers
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "", headers);
        System.out.println(" Waiting for msg....");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println("Received msg is '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
