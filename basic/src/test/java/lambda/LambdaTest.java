package lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 2021/7/1
 */
public class LambdaTest {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        //2、lambda表达式
        Collections.sort(names, (a, b) -> b.compareTo(a));
        Collections.sort(names, Comparator.reverseOrder());
//        Collections.sort(names, Comparator.reverseOrder());
    }
}
