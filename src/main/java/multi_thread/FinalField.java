package multi_thread;

/**
 * final 域测试实体
 *
 * @author maoxianbin1994@gmail.com
 * @since 1.0
 */
public class FinalField {

    int i;
    final int j;
    static FinalField ff;

    public FinalField () {
        i = 1;
        j = 2;
    }

    public static void write() {
        ff = new FinalField();
    }

    public static void read() {
        FinalField ff2 = ff;
        int a = ff2.i;
        int b = ff2.j;
        if (a == 0) {
            throw new RuntimeException("a == 0");
        }
        if (b == 0) {
            throw new RuntimeException("b == 0");
        }
    }
}
