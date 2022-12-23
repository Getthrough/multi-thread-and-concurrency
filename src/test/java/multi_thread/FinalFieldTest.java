package multi_thread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class FinalFieldTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    public void testFinalFieldReorder() {
        for (int i = 0; i < 1000000000; i++) {
            executorService.execute(FinalField::write);
            executorService.execute(FinalField::read);
            if (i % 10000 == 0)
                System.out.println(i);
        }
    }

}