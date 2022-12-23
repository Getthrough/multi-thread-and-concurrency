package multi_thread;

import junit.framework.TestCase;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReorderTest extends TestCase {

    public void testWriteRead() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        long tryCount = 0;
        while (true) {
            Boolean normal = doCalc(new Reorder(), threadPool);
            tryCount++;
            if (!normal) {
                System.out.println("tried : " + tryCount + " times");
                break;
            }
        }
    }

    private static Boolean doCalc(Reorder reorder, ExecutorService threadPool)
            throws InterruptedException, ExecutionException {
        threadPool.execute(reorder::write);
        Boolean normal = threadPool.submit(reorder::read).get();
        return normal;
    }

    public void testWriteRead2() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        long tryCount = 0;
        while (true) {
            Boolean normal = doCalc2(new Reorder(), threadPool);
            tryCount++;
            if (!normal) {
                System.out.println("tried : " + tryCount + " times");
                break;
            }
        }
    }

    private static Boolean doCalc2(Reorder reorder, ExecutorService threadPool)
            throws InterruptedException, ExecutionException {
        threadPool.execute(reorder::write2);
        Boolean normal = threadPool.submit(reorder::read2).get();
        return normal;
    }

}