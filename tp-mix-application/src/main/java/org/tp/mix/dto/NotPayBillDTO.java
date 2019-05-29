package org.tp.mix.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 待还账单-模板
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class NotPayBillDTO implements Serializable {

    private static final long serialVersionUID = -6491448910625116431L;
    /**
     * 出帐日期
     */
    private Date billDate;
    /**
     * 出帐期次
     */
    private String period;
    /**
     * 账单本金
     */
    private BigDecimal billPrincipal;
    /**
     * 账单利息
     */
    private BigDecimal billInterest;
    /**
     * 账单罚息
     */
    private BigDecimal billPinterest;
    /**
     * 账单服务费
     */
    private BigDecimal billServiceFee;
    /**
     * 账单逾期服务费
     */
    private BigDecimal billOverdueServiceFee;
    /**
     * 已还本金
     */
    private BigDecimal paidPrincipal;
    /**
     * 已还利息
     */
    private BigDecimal paidInterest;
    /**
     * 已还罚息
     */
    private BigDecimal paidPinterest;
    /**
     * 已还服务费
     */
    private BigDecimal paidServiceFee;
    /**
     * 已还逾期服务费
     */
    private BigDecimal paidOverdueServiceFee;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 总笔数
     */
    private Integer quantity;
    /**
     * 已还总笔数
     */
    private Integer paidQuantity;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 证件类型 1 身份证，2 军官证，3 护照，4 户口簿，5 士兵证，6 港澳来往内地通行证，7 台湾同胞来往内地通行证，8 临时身份证，9 外国人居留证，10 警官证
     */
    private Integer idType;
    /**
     * 证件号码
     */
    private String idNo;
    /**
     * 手机号码
     */
    private String mobileNo;



    /**
     * 获取全部待还金额
     *
     * @return 全部待还
     */
    public BigDecimal getTotalDueRepayAmount() {
        return billPrincipal.add(billInterest)
                            .add(billServiceFee)
                            .add(billPinterest)
                            .add(billOverdueServiceFee);
    }
}
