package multi_thread;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 原子操作测试实体
 *
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class AtomicObj {

    @Data
    @AllArgsConstructor
    static class Person {
        private String name;
        public volatile int age;
    }
}
