package multi_thread;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

public class SyncObjTest {

    @Test
    public void inspect() {
        SyncObj so = new SyncObj();
        System.out.println("无锁时：" + ClassLayout.parseInstance(so).toPrintable());
        so.syncMethod("偏向锁时：");
        new Thread(() -> so.syncMethod("轻量级锁时：")).start();
    }

}