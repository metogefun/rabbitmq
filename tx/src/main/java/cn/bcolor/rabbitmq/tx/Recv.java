package cn.bcolor.rabbitmq.tx;

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
    final static String QUEUE_NAME = "tx-test";
    final static int MSG_COUNT = 10000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();
        ch.queueDeclare(QUEUE_NAME, true, false, true, null);
        QueueingConsumer qc = new QueueingConsumer(ch);
        ch.basicConsume(QUEUE_NAME, true, qc);
        for (int i = 0; i < MSG_COUNT; ++i) {
            qc.nextDelivery();
            System.out.printf("Consumed %d\n", i);
        }
        ch.close();
        conn.close();

    }
}
