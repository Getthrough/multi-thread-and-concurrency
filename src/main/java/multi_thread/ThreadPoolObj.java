package multi_thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试实体
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class ThreadPoolObj {

    static class MockTask implements Runnable {
        private final int taskSeq;

        public MockTask(int taskSeq) {
            this.taskSeq = taskSeq;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(taskSeq % 2 == 0 ? 2 : 1);
                System.out.println("task [" + taskSeq + "] execute by " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public int getTaskSeq() {
            return taskSeq;
        }
    }

    static class LogHandler implements RejectedExecutionHandler {

        private int rejectCount;
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof MockTask) {
                MockTask mockTask = (MockTask) r;
                System.out.println("task [" + mockTask.getTaskSeq() + "] is logged");
                ++rejectCount;
            }
        }

        public int rejectCount() {
            return rejectCount;
        }
    }

}
