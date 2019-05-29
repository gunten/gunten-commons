package org.tp.mix.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.tp.mix.dto.NotPayBillDTO;

/**
 * 账单逾期事件
 *
 * @Description
 * @Date 2018/9/26
 **/
@Getter
@Setter
public class BillOverdueEvent extends ApplicationEvent {

    private NotPayBillDTO notPayBillDTO;

    public BillOverdueEvent(Object source) {
        super(source);
    }

    public BillOverdueEvent(Object source, NotPayBillDTO notPayBillDTO) {
        super(source);
        this.notPayBillDTO = notPayBillDTO;
    }
}
