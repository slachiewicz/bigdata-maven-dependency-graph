package nl.ordina.jtech.mavendependencygraph.queing;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * Class: QueueConsumerFactory
 */
public class QueueConsumerFactory<T> {

    private static BlockingQueue queue;
    private static Consumer consumer;

    private static final Runnable INSTANCE = () -> {
        while (true) {
            try {
                consumer.accept(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static Runnable getConsumer(BlockingQueue queue, Consumer consumer) {
        assert (!(QueueConsumerFactory.queue != null && !QueueConsumerFactory.queue.equals(queue)));
        assert (!(QueueConsumerFactory.consumer != null && QueueConsumerFactory.consumer.equals(consumer)));
        QueueConsumerFactory.queue = queue;
        QueueConsumerFactory.consumer = consumer;
        return INSTANCE;
    }
}
