package cn.bcolor.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送者
 *
 * @author wangzhaohui 2017/10/25.
 * @version 1.0
 */
public class Send {
    private static final int msgCount = 10;
    final static String QUEUE_NAME = "confirm-test";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        long startTime = System.currentTimeMillis();

        // Setup
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();

        ch.queueDeclare(QUEUE_NAME, true, false, false, null);


        ch.confirmSelect();

        // Publish
        for (long i = 0; i < msgCount; ++i) {
            ch.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, "nop".getBytes());
        }

        ch.waitForConfirmsOrDie();

        // Cleanup
        ch.queueDelete(QUEUE_NAME);
        ch.close();
        conn.close();

        long endTime = System.currentTimeMillis();
        System.out.printf("Test took %.3fs\n",
                (float) (endTime - startTime) / 1000);
    }
}
