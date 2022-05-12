package org.tp.mix.flink.table.api.example;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.types.Row;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author gunten
 * 2022/5/9
 */
public class TableApiTest {

    public static StreamExecutionEnvironment env = null;
    public static StreamTableEnvironment tableEnv = null;
    public static Table table = null;

    @Before
    public void before() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING).setParallelism(1);

        List<Student> list = Arrays.asList(
                new Student("a", 60L),
                new Student("a", 80L),
                new Student("a", 70L),
                new Student("b", 60L),
                new Student("b", 80L),
                new Student("c", 50L)
        );

        //数据源
        DataStreamSource<Student> source = env.fromCollection(list);

        //环境配置
        EnvironmentSettings setting = EnvironmentSettings.newInstance()
                .inBatchMode()
                .withBuiltInCatalogName("test_catalog")
                .withBuiltInDatabaseName("test_database")
                .build();
        tableEnv = StreamTableEnvironment.create(env, setting);
        // Create a Table object from DataStream
        table = tableEnv.fromDataStream(source);
        table.printSchema();
        System.out.println("==================");

    }
    @Test
    public void table2DataStreamTest() throws Exception {

        DataStream<Student> dataStream = tableEnv.toDataStream(table, Student.class);
        dataStream.print("tableStream");
        env.execute("table2DataStreamTest");
    }

    @Test
    public void tableApiTest() throws Exception {
        //创建临时视图
        tableEnv.createTemporaryView("student", table);

        Table t = tableEnv.from("student")
                .groupBy($("name"))
                .select($("name"), $("score").sum().as("total"));

        DataStream<Row> dataStream = tableEnv.toChangelogStream(t, Schema.derived(), ChangelogMode.all());

        dataStream.print("tableStream");
        env.execute("tableApiTest");
    }

    @Test
    public void sqlTest() throws Exception {

        //创建临时视图
        tableEnv.createTemporaryView("student", table);
        String sql = "select name, sum(score) as total from student group by name";
        Table t = tableEnv.sqlQuery(sql);

        DataStream<Row> dataStream = tableEnv.toChangelogStream(t, Schema.derived(), ChangelogMode.upsert());

        dataStream.print("tableStream");
        env.execute("sqlTest");
    }

    @Test
    public void tableSink1Test() throws Exception {

        Schema schema = Schema.newBuilder()
                .column("name", DataTypes.STRING())
                .column("score", DataTypes.BIGINT())
                .build();
        //创建表
        tableEnv.createTable("table1", TableDescriptor.forConnector("filesystem")
                .schema(schema)
                .option("path", "D:\\tmp")
                .format(FormatDescriptor.forFormat("csv")
                        .option("field-delimiter", "|")
                        .build())
                .build());

        //创建临时视图
        tableEnv.createTemporaryView("student", table);
        String sql = "select name, score from student";
        Table t = tableEnv.sqlQuery(sql);
        DataStream<Row> dataStream = tableEnv.toChangelogStream(t, Schema.derived(), ChangelogMode.insertOnly());

        dataStream.print("tableStream");
        t.executeInsert("table1");
        env.execute("tableSink1Test");
    }

    @Test
    public void tableSink2Test() throws Exception {

        String creatTable = "CREATE TABLE table2 (\n" +
                "  name STRING,\n" +
                "  total BIGINT\n" +
                ") WITH (\n" +
                "'connector' = 'filesystem',\n" +
                "'path' = 'file:///D:\\tmp\\SinkTable.csv',\n" +
                "'format' = 'csv',\n" +
                "'csv.field-delimiter'='|'\n" +
                ")";
        //创建表
        TableResult tableResult = tableEnv.executeSql(creatTable);

        //创建临时视图
        tableEnv.createTemporaryView("student", table);
        String sql = "select name, sum(score) as total from student group by name";
        Table t = tableEnv.sqlQuery(sql);

        DataStream<Row> dataStream = tableEnv.toChangelogStream(t, Schema.derived(), ChangelogMode.upsert());

        dataStream.print("tableStream");
        t.executeInsert("table2");
        env.execute("sqlTest");
    }

    @Test
    public void explainTest() throws Exception {

        //创建临时视图
        tableEnv.createTemporaryView("student", table);
        String sql = "select name, sum(score) as total from student group by name";
        Table t = tableEnv.sqlQuery(sql);

        System.out.println(t.explain());
    }

}
