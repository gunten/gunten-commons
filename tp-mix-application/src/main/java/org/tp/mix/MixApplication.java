package org.tp.mix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.tp.EnableSms;
import org.tp.SmsSender;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.ThreadPoolExecutor;

@EnableSwagger2
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = {"org.tp"})
@EnableAsync
@EnableSms
public class MixApplication {

    /**
     * 认为替换广播器 executor的方法达到事件异步效果
     * 固定 applicationEventMulticaster 名称
     * @return
     */
    @Bean
    protected SimpleApplicationEventMulticaster applicationEventMulticaster(){
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        //https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch25s04.html
        //一种线程池
//        multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        //or 另一种线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        /*
        Reject策略预定义有四种：
        (1)ThreadPoolExecutor.AbortPolicy策略，是默认的策略,处理程序遭到拒绝将抛出运行时 RejectedExecutionException。
        (2)ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        (3)ThreadPoolExecutor.DiscardPolicy策略，不能执行的任务将被丢弃.
        (4)ThreadPoolExecutor.DiscardOldestPolicy策略，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）.
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setKeepAliveSeconds(5);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.initialize();
        multicaster.setTaskExecutor(taskExecutor);
        return multicaster;
    }

    public static void main(String[] args) {
//        SpringApplication app =new SpringApplication(MixApplication.class);
//        app.run(args);

        new SpringApplicationBuilder(MixApplication.class)
//                .listeners(event -> { // 增加监听器
//                    System.err.println("监听到事件 ： " + event.getClass().getSimpleName());
//                })
                .run(args)
//                .close()
                ;

    }
}
