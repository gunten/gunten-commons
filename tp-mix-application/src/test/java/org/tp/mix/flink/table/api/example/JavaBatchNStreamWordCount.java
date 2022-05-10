package org.tp.mix.flink.table.api.example;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.net.URLDecoder;


public class JavaBatchNStreamWordCount {

	public static void main(String[] args) throws Exception {

		String path = JavaBatchNStreamWordCount.class.getResource("/flink/words.txt").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		System.out.println(path);


		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1)
		.setRuntimeMode(RuntimeExecutionMode.STREAMING)
		;

		//构建Table环境
		EnvironmentSettings settings = EnvironmentSettings.newInstance()
//				.inBatchMode()
				.inStreamingMode()
				.build();
		//setting 参数可有可无
		StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

		/** 1、Create a Table object from a SQL query
		 	需要依赖flink-csv
		 */
//		tableEnv.executeSql("CREATE TABLE t_words (" +
//				"  word STRING" +
//				") WITH ( " +
//				"  'connector' = 'filesystem'," +
//				"  'path' = 'file:///" + path + "'," +
//				"  'format' = 'csv'" +
////				"  'csv.field-delimiter' = ','" +
//				")");

		/**
		 * 2、 Create a Table object from Table descriptor
		* */
		final TableDescriptor sourceDescriptor = TableDescriptor.forConnector("filesystem")
				.schema(Schema.newBuilder()
						.column("word", DataTypes.STRING())
						.build())
				.option("path", path)
				.format(FormatDescriptor.forFormat("csv")
//						.option("field-delimiter", "|")
						.build())
				.build();
		tableEnv.createTemporaryTable("t_words", sourceDescriptor);


		Table readedTable = tableEnv.sqlQuery("select word,count(*) as cnt from t_words group by word");
//		Table readedTable = tableEnv.sqlQuery("select word from t_words ");

		//打印Schema
		System.out.println("Schema信息:");
		readedTable.printSchema();
		readedTable.execute().print();

		/*
        需要将结果转换成DataStream，才能打印结果，需要指定 封装的泛型;
        默认 ChangelogMode.all()*/

		DataStream<Row> outStream = tableEnv.toChangelogStream(readedTable);
//		DataStream<Row> outStream = tableEnv.toChangelogStream(readedTable,Schema.newBuilder()
//				.column("word", DataTypes.STRING())
//				.column("cnt", DataTypes.BIGINT())
//				.build());
//		DataStream<Row> outStream = tableEnv.toChangelogStream(readedTable, Schema.derived(), ChangelogMode.upsert());
		outStream.print("---");
		env.execute();
	}
}
