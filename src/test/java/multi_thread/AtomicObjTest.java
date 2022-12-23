package multi_thread;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicObjTest {

    @Test
    public void atomicArrayTest() throws InterruptedException {
        ThreadPoolExecutor poolExecutor = getTenThreadPool();
        AtomicIntegerArray aia = new AtomicIntegerArray(1);
        for (int i = 0; i < 100; i++) {
            poolExecutor.execute(()-> {
                aia.getAndAdd(0, 1);
            });
        }
        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals(100, aia.get(0));
    }

    private static ThreadPoolExecutor getTenThreadPool() {
        return new ThreadPoolExecutor(
                10, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Test
    public void arrayTest() throws InterruptedException {
        int[] arr = new int[]{0};
        ThreadPoolExecutor poolExecutor = getTenThreadPool();
        for (int i = 0; i < 100; i++) {
            poolExecutor.execute(() -> {
                int val = arr[0];
                arr[0] = ++val;
            });
        }
        TimeUnit.SECONDS.sleep(3);
        Assert.assertEquals(100, arr[0]);
    }

    @Test
    public void atomicReferenceTest() {
        AtomicObj.Person person = new AtomicObj.Person("zhangsan", 13);
        final AtomicReference<AtomicObj.Person> ar = new AtomicReference<>(person);
        AtomicObj.Person updatePerson = new AtomicObj.Person("laowang", 18);
        ar.compareAndSet(person, updatePerson);
        Assert.assertEquals("laowang", ar.get().getName());
        Assert.assertEquals(18, ar.get().getAge());
    }

    @Test
    public void atomicStampedReferenceTest() throws InterruptedException {
        AtomicObj.Person person = new AtomicObj.Person("zhangsan", 13);
        final AtomicStampedReference<AtomicObj.Person> asr = new AtomicStampedReference<>(person, 0);
        new Thread(() -> {
            // 将person先更新成其他对象，再更新回去
            AtomicObj.Person lisi = new AtomicObj.Person("lisi", 16);
            asr.compareAndSet(person, lisi, 0, 1);
            asr.compareAndSet(lisi, new AtomicObj.Person("zhangsan", 13), 1, 2);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        AtomicObj.Person updatePerson = new AtomicObj.Person("laowang", 18);
        // 期待原始的并进行更新将会失败，因为版本号不同，有效避免了ABA问题
        Assert.assertFalse(asr.compareAndSet(person, updatePerson, 0, 1));
        Assert.assertEquals("zhangsan", asr.getReference().getName());
        Assert.assertEquals(13, asr.getReference().getAge());
    }

    @Test
    public void atomicIntegerFieldUpdaterTest() {
        AtomicObj.Person p = new AtomicObj.Person("zhangsan", 13);
        AtomicIntegerFieldUpdater<AtomicObj.Person> aifu =
                AtomicIntegerFieldUpdater.newUpdater(AtomicObj.Person.class, "age");
        aifu.compareAndSet(p, 13, 15);
        Assert.assertEquals(15, aifu.get(p));
    }

}