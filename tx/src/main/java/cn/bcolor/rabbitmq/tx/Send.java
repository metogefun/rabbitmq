package cn.bcolor.rabbitmq.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * TODO
 *
 * @author wangzhaohui 2017/10/25.
 * @version 1.0
 */
public class Send {

    final static String QUEUE_NAME = "tx-test";
    final static int MSG_COUNT = 10000;

    public static void main(String[] args) throws IOException, TimeoutException {
        long startTime = System.currentTimeMillis();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();
        ch.queueDeclare(QUEUE_NAME, true, false, true, null);
        ch.txSelect();

        for (int i = 0; i < MSG_COUNT; ++i) {
            ch.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_BASIC,
                    "nop".getBytes());
            ch.txCommit();
        }
        ch.close();
        conn.close();

        long endTime = System.currentTimeMillis();
        System.out.printf("Test took %.3fs\n", (float)(endTime - startTime)/1000);

    }
}
