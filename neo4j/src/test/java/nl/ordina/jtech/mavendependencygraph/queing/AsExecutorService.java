package nl.ordina.jtech.mavendependencygraph.queing;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class: AsExecutorService
 */
public class AsExecutorService {

    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
    ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, queue);

    @Test
    public void testSimple() throws Exception {
        MyProducer producer = new MyProducer(queue);
        new Thread(producer).start();
        Thread.sleep(5000);
    }

    class MyTask implements Callable<Void> {

        private final String consumer;

        MyTask(String consumer) {
            this.consumer = consumer;
        }


        @Override
        public Void call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " = > " + consumer);
            return null;
        }
    }

    private class MyProducer implements Runnable {

        BlockingQueue<Runnable> queue;

        public MyProducer(BlockingQueue<Runnable> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(250);
                    System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
                    executorService.submit(new MyTask("jdklajdla"));
                    System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
