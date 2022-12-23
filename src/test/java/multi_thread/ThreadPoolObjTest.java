package multi_thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class ThreadPoolObjTest {

    private ThreadPoolExecutor poolExecutor;

    /**
     * 创建一个核心线程数为2，最大线程数为4，非核心线程空闲时间为10秒，线程线程前缀为mock-task-thread-%d，拒绝策略为日志记录的线程池。
     */
    @Before
    public void initThreadPool() {
        ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(2);
        poolExecutor = new ThreadPoolExecutor(2, 4, 10,
                TimeUnit.SECONDS, taskQueue,
                new ThreadFactoryBuilder().setNameFormat("mock-task-thread-%d").build(), new ThreadPoolObj.LogHandler());
    }

    @After
    public void shutDownPool() {
        poolExecutor.shutdown();
        poolExecutor.shutdownNow();
    }

    @Test
    public void taskEnqueueFirstTest() throws InterruptedException {
        for (int i = 1; i <= 3; i++) {
            ThreadPoolObj.MockTask mockTask = new ThreadPoolObj.MockTask(i);
            poolExecutor.execute(mockTask);
        }
        TimeUnit.SECONDS.sleep(3);
        System.out.println("largestPoolSize :" + poolExecutor.getLargestPoolSize());
        Assert.assertEquals(poolExecutor.getCorePoolSize(), poolExecutor.getLargestPoolSize());
    }

    @Test
    public void nonCoreThreadCreateTest() throws InterruptedException {
        int taskCount = 5;
        for (int i = 1; i <= taskCount; i++) {
            ThreadPoolObj.MockTask mockTask = new ThreadPoolObj.MockTask(i);
            poolExecutor.execute(mockTask);
        }
        TimeUnit.SECONDS.sleep(3);
        System.out.println("largestPoolSize :" + poolExecutor.getLargestPoolSize());
        Assert.assertEquals(3, poolExecutor.getLargestPoolSize());
    }

    @Test
    public void taskRejectTest() throws InterruptedException {
        for (int i = 1; i <= 8; i++) {
            ThreadPoolObj.MockTask mockTask = new ThreadPoolObj.MockTask(i);
            poolExecutor.execute(mockTask);
        }
        TimeUnit.SECONDS.sleep(3);
        System.out.println("largestPoolSize :" + poolExecutor.getLargestPoolSize());
        Assert.assertEquals(2, ((ThreadPoolObj.LogHandler)poolExecutor.getRejectedExecutionHandler()).rejectCount());
    }

    @Test
    public void shutDownTest() throws InterruptedException {
        for (int i = 1; i <= 3; i++) {
            poolExecutor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
            });
        }
        poolExecutor.shutdown();
        System.out.println("shutdown start ####");
        System.out.println("isShutDown :" + poolExecutor.isShutdown());
        System.out.println("isTerminating :" + poolExecutor.isTerminating());
        System.out.println("isTerminated :" + poolExecutor.isTerminated());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("after waiting ####");
        System.out.println("isTerminating :" + poolExecutor.isTerminating());
        System.out.println("isTerminated :" + poolExecutor.isTerminated());
        Assert.assertTrue(poolExecutor.isTerminated());
    }

    @Test
    public void shutDownNowTest() throws InterruptedException {
        for (int i = 1; i <= 3; i++) {
            ThreadPoolObj.MockTask mockTask = new ThreadPoolObj.MockTask(i);
            TimeUnit.MILLISECONDS.sleep(400);
            poolExecutor.execute(mockTask);
        }

        List<Runnable> notExecuteTasks = poolExecutor.shutdownNow();
        for (Runnable notExecuteTask : notExecuteTasks) {
            System.out.println("task [" + ((ThreadPoolObj.MockTask) notExecuteTask).getTaskSeq() + "] not executed");
        }
        System.out.println("shutdownNow start ####");
        System.out.println("isShutDown :" + poolExecutor.isShutdown());
        System.out.println("isTerminating :" + poolExecutor.isTerminating());
        System.out.println("isTerminated :" + poolExecutor.isTerminated());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("after waiting ####");
        System.out.println("isTerminating :" + poolExecutor.isTerminating());
        System.out.println("isTerminated :" + poolExecutor.isTerminated());

        Assert.assertTrue(poolExecutor.isTerminated());
    }

    /**
     * 在如下测试环境<b>未能得出符合预期的结论</b>：<pre>
     *   型号名称：	MacBook Pro
     *   型号标识符：	MacBookPro12,1
     *   处理器名称：	双核Intel Core i5
     *   处理器速度：	2.9 GHz
     *   处理器数目：	1
     *   核总数：	2
     *   L2缓存（每个核）：	256 KB
     *   L3缓存：	3 MB
     *   超线程技术：	已启用
     *   内存：	8 GB
     *   系统固件版本：	430.140.3.0.0
     *   操作系统加载程序版本：	540.120.3~22
     *
     * @throws InterruptedException
     */
    @Test
    public void cpuBusyTest() throws InterruptedException {
        int taskCount = 10;
        List<int[]> suggestDataList = new ArrayList<>(taskCount);
        List<int[]> casualDataList = new ArrayList<>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            int[] randomIntArray = getRandomIntArray(120_000);
            int[] randomIntArrayCopy = Arrays.copyOf(randomIntArray, randomIntArray.length);
            suggestDataList.add(randomIntArray);
            casualDataList.add(randomIntArrayCopy);
        }
        // 测试线程池个数为逻辑核心数
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor suggestExecutor = getThreadPool(processors, "suggest");
        System.out.println("suggest config start");
        execute(suggestExecutor, taskCount, suggestDataList);
        suggestExecutor.shutdown();

        // 测试线程池个数为超过逻辑核心数的随意设置的值
        ThreadPoolExecutor casualExecutor = getThreadPool(10, "casual");
        System.out.println("casual config start");
        execute(casualExecutor, taskCount, casualDataList);
        casualExecutor.shutdown();

    }

    private void execute(ThreadPoolExecutor executor, int taskCount, List<int[]> dataList)
            throws InterruptedException {
        for (int i = 0; i < taskCount; i++) {
            int finalI = i;
            executor.execute(() -> sortArray(dataList.get(finalI)));
        }
        long start = System.currentTimeMillis();
        while (true) {
            if (executor.getCompletedTaskCount() == taskCount) {
                System.out.println((System.currentTimeMillis() - start) + " ms" );
                break;
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    /**
     * 使用耗时较久的选择排序
     *
     * @param nums
     * @return
     */
    public int[] sortArray(int[] nums) {
        int len = nums.length;
        // 循环不变量：[0, i) 有序，且该区间里所有元素就是最终排定的样子
        for (int i = 0; i < len - 1; i++) {
            // 选择区间 [i, len - 1] 里最小的元素的索引，交换到下标 i
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            swap(nums, i, minIndex);
        }
        return nums;
    }

    private void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }

    /**
     * 获取指定数量的 int 数组
     *
     * @param count
     * @return
     */
    private int[] getRandomIntArray(int count) {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = new Random().nextInt(count);
        }
        return result;
    }

    @Test
    public void ioBusyTest() throws InterruptedException {
    }

    private static ThreadPoolExecutor getThreadPool(int max, String name) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(max, max, 3,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat(name + "-%d").build());
        executor.prestartAllCoreThreads();
        return executor;
    }


}
