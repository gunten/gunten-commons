package org.tp.threadcontext;


import java.util.HashMap;
import java.util.Map;

/** 线程上下文实现类
 * @createDate 2018/11/18
 */
public class ThreadContextStoreTool extends AbstractThreadContext{
    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, String> childValue(Map<String, String> parentValue) {
            return parentValue == null ? null : new HashMap(parentValue);
        }
    };
    private static volatile ThreadContextStoreTool threadContextStore ;

    public static  ThreadContextStoreTool getInstance() {
        ThreadContextStoreTool contextStore = threadContextStore;
        if (contextStore == null) {
            synchronized (ThreadContextStoreTool.class) {
                contextStore = threadContextStore;
                if (contextStore == null) {
                    threadContextStore = contextStore = new ThreadContextStoreTool();
                }
            }
        }
        return contextStore;
    }

    @Override
    protected ThreadLocal<Map<String, Object>> getThreadContext() {
        return this.threadLocal;
    }

}
