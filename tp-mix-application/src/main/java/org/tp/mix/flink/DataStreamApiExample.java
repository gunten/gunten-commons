package org.tp.mix.flink;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.HashMap;
import java.util.Random;

/**
 * For 【Apache Flink 入门教程】4.DataStream API 编程
 * DataStreamApiExample
 *
 * @author
 * @date 2019/6/27
 **/
public class DataStreamApiExample {

    private static class DataSource extends RichParallelSourceFunction<Tuple2<String, Integer>> {

        private volatile boolean running = true;

        @Override
        public void run(SourceContext<Tuple2<String, Integer>> ctx) throws Exception {
            Random random = new Random(System.currentTimeMillis());
            while (running) {
                Thread.sleep((getRuntimeContext().getIndexOfThisSubtask()+1) + 1000 + 500);
                String key = "类别" + (char)('A' + random.nextInt(3));
                int value = random.nextInt(10)+1;
                System.out.println(String.format("Emit:\t(%s,%d)",key,value));
                ctx.collect(new Tuple2<>(key,value));
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<Tuple2<String, Integer>> ds = env.addSource(new DataSource());
//        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = ds.keyBy(0);
        KeyedStream<Tuple2<String, Integer>, String> keyedStream = ds.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {

            @Override
            public String getKey(Tuple2<String, Integer> value) throws Exception {
                return value.f0;
            }

        });

        keyedStream.sum(1)

                .keyBy(new KeySelector<Tuple2<String, Integer>, Object>() {
                    @Override
                    public Object getKey(Tuple2<String, Integer> stringIntegerTuple2) {
                        System.out.println("getKey:" + stringIntegerTuple2);
                        return "";
                    }
                })
                .countWindowAll(8)
                .aggregate(new AggregateFunction<Tuple2<String, Integer>, HashMap<String, Integer>, HashMap<String, Integer>>() {
                    @Override
                    public HashMap<String, Integer> createAccumulator() {
                        return new HashMap();
                    }

                    @Override
                    public HashMap<String, Integer> add(Tuple2<String, Integer> value, HashMap<String, Integer> accumulator) {
                        accumulator.put(value.f0, value.f1);
//                        System.out.println(accumulator);
                        return accumulator;
                    }

                    @Override
                    public HashMap<String, Integer> getResult(HashMap<String, Integer> accumulator) {
                        return accumulator;
                    }

                    @Override
                    public HashMap<String, Integer> merge(HashMap<String, Integer> a, HashMap<String, Integer> b) {
                        a.putAll(b);
                        System.out.println("merge happen: " + a);
                        return a;
                    }
                })

        .addSink(new SinkFunction<HashMap<String, Integer>>() {
            @Override
            public void invoke(HashMap<String, Integer> value, Context context) throws Exception {
                SinkFunction.super.invoke(value, context);
                // 每个类型的商品成交量
                System.out.println(value);
                // 商品成交总量
                System.out.println(value.values().stream().mapToInt(v -> v).sum());
            }
        });

        env.execute();

    }

}
