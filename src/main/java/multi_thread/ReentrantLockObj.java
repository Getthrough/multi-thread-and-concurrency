package multi_thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class ReentrantLockObj {

    private int a = 0;
    private Lock lock = new ReentrantLock();

    public void write() {
        lock.lock();
        try {
            a++;
        } finally {
            lock.unlock();
        }
    }

    public void read() {
        lock.lock();
        try {
            int i = a;
        } finally {
            lock.unlock();
        }
    }

}
