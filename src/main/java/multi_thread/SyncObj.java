package multi_thread;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class SyncObj {

    public synchronized void syncMethod(String memo) {
        int a = 0;
        System.out.println(memo + ClassLayout.parseInstance(this).toPrintable());
    }

    public void syncBlock() {
        synchronized (this) {
            int a = 0;
            // System.out.println("代码加锁过程中：" + ClassLayout.parseInstance(this).toPrintable());
        }
    }

}
