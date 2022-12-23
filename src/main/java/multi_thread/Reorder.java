package multi_thread;

public class Reorder {
    private int a = 0;
    private boolean aAssignedFlag = false;

    public void write() {
        a = 1;                  // 1
        aAssignedFlag = true;   // 2
    }

    public synchronized boolean read() {
        if (aAssignedFlag) {    // 3
            int i = a + 1;      // 4

            if (i != 2) {   // if sept 1 and sept switched
                System.out.println("i = " + i + ", a = " + a);
                return false;
            }
        }
        return true;
    }

    public synchronized void write2() {
        a = 1;                  // 1
        aAssignedFlag = true;   // 2
    }

    public boolean read2() {
        if (aAssignedFlag) {    // 3
            int i = a + 1;      // 4

            if (i != 2) {   // if sept 3 and sept 4 switched, and step 3 is before step 1
                System.out.println("i = " + i + ", a = " + a);
                return false;
            }
        }
        return true;
    }
}
