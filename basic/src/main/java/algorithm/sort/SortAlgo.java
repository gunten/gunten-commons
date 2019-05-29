package algorithm.sort;

import java.util.stream.IntStream;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/4/16
 */
public class SortAlgo {

    // 插入排序，a 表示数组，n 表示数组大小
    public static void insertionSort(int[] a, int n) {
        if (n <= 1) return;
        int j;
        for (int i = 1; i < n; ++i) {
            int value = a[i];
            // 查找插入的位置
            for (j = i - 1; j >= 0; --j) {
                if (a[j] > value) {
                    a[j+1] = a[j];  // 数据移动
                } else {
                    break;
                }
            }
            a[j+1] = value; // 插入数据
        }
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 6, 1, 3, 2};
        insertionSort(a ,a.length);
        IntStream.of(a).forEach(System.out::println);
    }
}
