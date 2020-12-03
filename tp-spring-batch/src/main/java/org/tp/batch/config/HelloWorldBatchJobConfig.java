package org.tp.batch.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tp.batch.PersonItemProcessor;
import org.tp.transactional.annotation.entity.ReadData;
import org.tp.transactional.annotation.entity.WriteData;


@Configuration
public class HelloWorldBatchJobConfig {


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    /**
     * helloWorldJob任务配置
     *
     * @return
     */
    @Bean //(name = "helloWorldJob")
    public Job helloWorldJob() throws Exception {
        return jobBuilderFactory.get("helloWorldJob")
                .incrementer(new RunIdIncrementer()) //使每个job的运行id都唯 可以重复执行job
                .start(helloWorldStep()).build();
    }

    /**
     * helloWorld步骤任务配置
     *
     * @return
     */
    @Bean
    public Step helloWorldStep() {
        return stepBuilderFactory.get("helloWorldStep")
                .<ReadData, WriteData>chunk(10)
                .reader(mbReader())
                .processor(processor())
                .writer(mbWriter())
                .build();
    }


    /**
     * 获取数据方法
     * 这里使用的文件处理类(如需要读取数据库数据可使用JDBC为前缀的ReaderBuilder)
     *
     * @return
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<ReadData> mbReader() {
        MyBatisPagingItemReader<ReadData> itemReader = new MyBatisPagingItemReader();
        itemReader.setSqlSessionFactory(sqlSessionFactory);
        itemReader.setQueryId("org.tp.transactional.annotation.mapper.ReadMapper.getResourceData");
        itemReader.setPageSize(10);
        return itemReader;
    }


    /**
     * 读取数据之后的处理方法
     *
     * @return
     */
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /**
     * 写入方法
     * 目前配置是都区之后逐条处理,并累计到10条时进行统一写入
     *
     * @return
     */
    @Bean
    @StepScope
    public MyBatisBatchItemWriter mbWriter(){

        MyBatisBatchItemWriter<WriteData> itemWriter = new MyBatisBatchItemWriter();
        try {
            itemWriter.setSqlSessionFactory(sqlSessionFactory);
            itemWriter.setStatementId("org.tp.transactional.annotation.mapper.WriteMapper.setTargetData");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemWriter;
    }

    //构造线程
   /* @Bean
    protected ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(15);
        executor.setKeepAliveSeconds(300);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setQueueCapacity(10000);
        executor.setThreadGroupName("spring_batch");
        return executor;
    }*/

}
