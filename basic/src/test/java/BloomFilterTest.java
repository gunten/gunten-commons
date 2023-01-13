import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

public class BloomFilterTest {

    private static int size = 1000000;
    private static BloomFilter<Integer> bloomFilter =
            BloomFilter.create(Funnels.integerFunnel(), size, 0.001);

    public static void main(String[] args) {
        //放百万个key到过滤器
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        List<Integer> list = new ArrayList<>((1000));
        //取20000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 1; i < size + 20000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("误判的数量:" + list.size());

    }
}
