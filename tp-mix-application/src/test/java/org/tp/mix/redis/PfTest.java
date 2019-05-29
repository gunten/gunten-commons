package org.tp.mix.redis;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 算法验证 ，与redis连接无关
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/2/1
 */
public class PfTest {

    static class BitKeeper {
        private int maxbits;

        //# 通过随机数记录最大的低位零的个数
        public void random() {
            long value = ThreadLocalRandom.current().nextLong(2L << 32);
            int bits = lowZeros(value);
            if (bits > this.maxbits) {
                this.maxbits = bits;
            }
        }

        //# 算低位零的个数
        private int lowZeros(long value) {
            int i = 1;
            for (; i < 32; i++) {
                if (value >> i << i != value) {
                    break;
                }
            }
            return i - 1;
        }
    }

    static class Experiment {
        private int n;
        private BitKeeper keeper;

        public Experiment(int n) {
            this.n = n;
            this.keeper = new BitKeeper();
        }

        public void work() {
            for (int i = 0; i < n; i++) {
                this.keeper.random();
            }
        }

        public void debug() {
            System.out.printf("%d %.2f %d\n", this.n, Math.log(this.n) / Math.log(2), this.keeper.maxbits);
        }
    }

    public static void main(String[] args) {
        for (int i = 1000; i < 100000; i += 100) {
            Experiment exp = new Experiment(i);
            exp.work();
            exp.debug();
        }
    }

}

