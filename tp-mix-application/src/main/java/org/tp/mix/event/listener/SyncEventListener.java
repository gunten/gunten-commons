package org.tp.mix.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.tp.mix.event.BillOverdueEvent;

/**
 * 同步事件监听器
 *
 */
@Slf4j
@Component
public class SyncEventListener implements ApplicationListener<BillOverdueEvent> {


    @Override
    public void onApplicationEvent(BillOverdueEvent billOverdueEvent) {
        log.error("SyncEventListener rcv !!!!");
    }
}
