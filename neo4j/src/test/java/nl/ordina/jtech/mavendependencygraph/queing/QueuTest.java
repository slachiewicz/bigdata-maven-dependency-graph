package nl.ordina.jtech.mavendependencygraph.queing;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Class: QueuTest
 */


public class QueuTest {

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(1000);

    @Test
    public void testSimple() throws Exception {
        String var1 = "A";
        String var2 = "B";
        MyConsumer consumer = new MyConsumer(queue);
        Runnable consumer1 = QueueConsumerFactory.getConsumer(queue, o -> System.out.println(Thread.currentThread().getName() + " -> " + o));

        new Thread(consumer1).start();

        for (int i = 0; i < 10; i++) {
            new Thread(new MyProducer(queue)).start();
        }

        Thread.sleep(5000);

    }

    private class MyConsumer implements Runnable {

        BlockingQueue<String> queue;

        public MyConsumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String take = queue.take();
                    System.out.println(Thread.currentThread().getName() + " ==> take = " + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyProducer implements Runnable {

        BlockingQueue<String> queue;

        public MyProducer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(250);
                    String s = LocalDateTime.now() + " -> " + Thread.currentThread().getName();
//                    System.out.println("s = " + s);
                    queue.add(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
