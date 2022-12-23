package multi_thread;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 并发包工具类测试
 *
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class ConcurrencyToolsTest {

    @Test
    public void countDownLatchTest() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(3);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
        executor.execute(() -> handle(1, "handle user data", cdl));
        executor.execute(() -> handle(2, "handle goods data", cdl));
        executor.execute(() -> handle(4, "handle order data", cdl));
        boolean await = cdl.await(3, TimeUnit.SECONDS);
        if (!await) {
            System.out.println("tasks done part");
        }
    }

    private static void handle(int timeout, String handlData, CountDownLatch cdl) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
            System.out.println(handlData);
        } catch (InterruptedException e) {
        }
        cdl.countDown();
    }

    @Test
    public void cyclicBarrierTest() {
        AtomicIntegerArray aia = new AtomicIntegerArray(3);
        CyclicBarrier cb = new CyclicBarrier(3, ()-> System.out.println(aia));
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
        executor.execute(()-> handle("update index 0", 0, aia, cb));
        executor.execute(()-> handle("update index 1", 1, aia, cb));
        executor.execute(()-> handle("update index 2", 2, aia, cb));

    }
    private static void handle(String desc, int val, AtomicIntegerArray aia, CyclicBarrier cb) {
        try {
            System.out.println(desc);
            aia.getAndSet(val, val);
            cb.await();
            System.out.println("restart " + val);
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void semaphoreTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5, 5, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                try {
                    semaphore.acquire();// 控制 rpc 并发调用数
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " : rpc invoke");
                    semaphore.release();
                } catch (InterruptedException e) {
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void forkJoinTest() throws ExecutionException, InterruptedException {
        ForkJoinPool fjp = new ForkJoinPool();
        CountTask countTask = new CountTask(1, 4);
        ForkJoinTask<Integer> result = fjp.submit(countTask);
        Assert.assertEquals(10, (int) result.get());
    }

}
