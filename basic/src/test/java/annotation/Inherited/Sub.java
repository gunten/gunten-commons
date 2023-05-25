package annotation.Inherited;

/**
 * 2023/6/14
 */
@BTable
public class Sub extends Super {
    private int subx;
    public int suby;

    private Sub() {
    }

    public Sub(int i) {
    }

    //私有
    private int subX() {
        return 0;
    }
    //公有
    public int subY() {
        return 0;
    }

}

