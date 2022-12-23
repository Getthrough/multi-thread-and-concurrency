package multi_thread;

import java.util.concurrent.RecursiveTask;

/**
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class CountTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 2;// 切割任务的阈值
    private final int start;
    private final int end;
    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();
            sum = leftResult + rightResult;
        }
        return sum;
    }
}
