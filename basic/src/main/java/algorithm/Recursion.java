package algorithm;

/**
 * 1+2+....+n  不能用乘除 循环  ifelse  swithc case
 * 2021/6/25
 */
public class Recursion {

    public static int s(int n){
        if(n == 1) return 1;
        return s(n - 1) + n;
    }

    public static void main(String[] args) {
        //s(n) = s(n-1) +n
        System.out.println(Recursion.s(100));
    }
}
