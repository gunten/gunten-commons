package org.tp.mix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.tp.mix.dto.NotPayBillDTO;
import org.tp.mix.event.BillOverdueEvent;

import java.util.Date;

/**
 * @author gunten
 * 2019/4/12
 */
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    private ApplicationContext applicationContext;

    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        //在容器加载完毕后获取配置文件中的配置
//        ServerConfig serverConfig = (ServerConfig)event.getApplicationContext().getBean(ServerConfig.class);
        NotPayBillDTO dto = new NotPayBillDTO()
                .setBillDate(new Date())
                .setBillNo("billNo001")
                ;

        applicationContext.publishEvent(new BillOverdueEvent(this,dto));
    }
}
