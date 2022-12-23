package multi_thread;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * 偏向锁测试
 *
 */
public class BiasedOjbTest {

    /**
     * 测试从无锁升级到偏向锁
     */
    @Test
    public void testWithNoHashCodeInvoke() {
        BiasedObj bo = new BiasedObj();
        System.out.println("无锁时：" + ClassLayout.parseInstance(bo).toPrintable());
        synchronized (bo) {
            System.out.println("加锁后： " + ClassLayout.parseInstance(bo).toPrintable());
        }
    }

    /**
     * 测试执行过 hashCode 方法后从无锁升级成轻量级锁
     */
    @Test
    public void testWithHashCodeInvoke() {
        BiasedObj bo = new BiasedObj();
        System.out.println("无锁时：" + ClassLayout.parseInstance(bo).toPrintable());
        int hashCode = bo.hashCode();
        System.out.println("hashCode后：" + ClassLayout.parseInstance(bo).toPrintable());
        synchronized (bo) {
            System.out.println("加锁后" + ClassLayout.parseInstance(bo).toPrintable());
        }
    }

}