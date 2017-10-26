package cn.bcolor.rabbitmq.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * TODO
 *
 * @author wangzhaohui 2017/10/25.
 * @version 1.0
 */
public class CallbackSend {
    final static String QUEUE_NAME = "confirm-test-callback";
    private static final int MSG_COUNT = 1000;

    private static volatile SortedSet<Long> unconfirmedSet =
            Collections.synchronizedSortedSet(new TreeSet());

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();


        ch.addConfirmListener(new ConfirmListener() {
            public void handleAck(long seqNo, boolean multiple) {
                if (multiple) {
                    unconfirmedSet.headSet(seqNo + 1).clear();
                } else {
                    unconfirmedSet.remove(seqNo);
                }
            }

            public void handleNack(long seqNo, boolean multiple) {
                // handle the lost messages somehow
            }
        });
        ch.confirmSelect();
        for (long i = 0; i < MSG_COUNT; ++i) {
            unconfirmedSet.add(ch.getNextPublishSeqNo());
            ch.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC,
                    "nop".getBytes());
        }
    }
}
