package cn.bcolor.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * TODO
 *
 * @author wangzhaohui 2017/10/25.
 * @version 1.0
 */
public class Recv {

    private static final int msgCount = 10000;
    final static String QUEUE_NAME = "confirm-test";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // Setup
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();
        ch.queueDeclare(QUEUE_NAME, true, false, false, null);

        // Consume
        QueueingConsumer qc = new QueueingConsumer(ch);
        ch.basicConsume(QUEUE_NAME, true, qc);
        for (int i = 0; i < msgCount; ++i) {
            qc.nextDelivery();
        }

        // Cleanup
        ch.close();
        conn.close();

    }
}
