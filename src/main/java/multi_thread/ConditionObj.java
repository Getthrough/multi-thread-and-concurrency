package multi_thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class ConditionObj {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

}
