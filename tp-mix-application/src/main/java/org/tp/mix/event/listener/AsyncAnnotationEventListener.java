package org.tp.mix.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.tp.mix.event.BillOverdueEvent;

/**
 * 注解式 异步事件监听器
 *
 */
@Slf4j
//@Component
public class AsyncAnnotationEventListener {


    @EventListener(BillOverdueEvent.class)
    @Async
    public void onApplicationEvent(BillOverdueEvent billOverdueEvent) {
        log.error("AsyncAnnotationEventListener rcv !!!!");
    }
}
