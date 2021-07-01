package algorithm.sort;

import java.util.stream.IntStream;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/4/16
 */
public class SortAlgo {

    // 插入排序，a 表示数组，n 表示数组大小
    //4, 5, 6, 1, 3, 2
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

    public static void bubbleSort(int a[], int n) {
        if (n <= 1) return;

        int temp;
        // 提前退出冒泡循环的标志位
        boolean flag = false;
        for(int i=0; i< n -1; i++){
            for (int j = 0; j < n -1 - i; j++) {
                if(a[j] >a[j+1] ){
                    temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                    flag = true; // 表示有数据交换
                }
            }
            if(!flag) break; // 没有数据交换，提前退出
        }

    }

    //选择排序
    public static void selectionSort(int a[], int n){
        if (n <= 1) return;

        int min;
        for (int i = 0; i < n - 1; ++i) {
            min = i;
            for (int j = i + 1; j < n; j++) {
                if(a[j] < a[min]){
                    min = j; //找到最小数下标
                }
            }

            int temp = a[min];
            a[min] = a[i];
            a[i] = temp;
        }
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 6, 1, 3, 2};
//        insertionSort(a ,a.length);
//        bubbleSort(a, a.length);
        selectionSort(a, a.length);
        IntStream.of(a).forEach(System.out::println);
    }
}
