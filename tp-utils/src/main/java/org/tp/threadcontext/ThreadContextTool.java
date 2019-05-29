package org.tp.threadcontext;


import org.tp.enums.ContextEnum;

/**
 * 线程上下文工具类
 *
 */
public class ThreadContextTool {

    private static volatile  ThreadContextTool threadContext ;

    private ThreadContextTool() {}

    public static synchronized ThreadContextTool getInstance() {
        ThreadContextTool context = threadContext;
        if (context == null) {
            synchronized (ThreadContextTool.class) {
                context = threadContext;
                if (context == null) {
                    threadContext = context = new ThreadContextTool();
                }
            }
        }
        return context;
    }

    /**
     * 获取渠道流水号
     *
     * @return
     */
    public static String getChannelSerialNo() {
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.ENTERPRISE_CODE.getCode());
    }

    /**
     * 获取商户号
     *
     * @return
     */
    public static String getEnterpriseCode() {
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.ENTERPRISE_CODE.getCode());
    }

    /**
     * 获取用户号
     *
     * @return
     */
    public static String getSubuserNo() {
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.SUBUSER_NO.getCode());
    }

    /**
     * 获取产品号
     *
     * @return
     */
    public static String getProdCode() {
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.PROD_CODE.getCode());
    }

    /**
     * 获取分控埋点数据
     *
     * @return
     */
    public static String getRccData() {
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.RCC_DATA.getCode());
    }

    /**
     * 获取历史数据
     * @return
     */
    public static String getHistoryData(){
        return (String) ThreadContextStoreTool.getInstance().get(ContextEnum.RCC_DATA.getCode());
    }

}
