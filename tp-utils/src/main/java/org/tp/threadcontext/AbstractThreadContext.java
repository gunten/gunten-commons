package org.tp.threadcontext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/1/21
 */
public abstract class AbstractThreadContext {
    public AbstractThreadContext() {
    }

    protected abstract ThreadLocal<Map<String, Object>> getThreadContext();

    public void set(String key, Object o) {
        ThreadLocal<Map<String, Object>> threadContext = this.getThreadContext();
        Map<String, Object> map = (Map)threadContext.get();
        if (this.isMapNull((Map)map)) {
            map = new HashMap();
        }

        ((Map)map).put(key, o);
        if (o instanceof String) {
            ((Map)map).put((String)o, Thread.currentThread().getName());
        }

        threadContext.set(map);
    }

    public void removeKey(String key) {
        ThreadLocal<Map<String, Object>> threadContext = this.getThreadContext();
        Map<String, Object> map = (Map)threadContext.get();
        if (this.isMapNull((Map)map)) {
            map = new HashMap();
        }

        ((Map)map).remove(key);
        threadContext.set(map);
    }

    public Object get(String key) {
        ThreadLocal<Map<String, Object>> threadContext = this.getThreadContext();
        Map<String, Object> map = (Map)threadContext.get();
        return this.isMapNull(map) ? null : map.get(key);
    }

    public void clean() {
        ThreadLocal<Map<String, Object>> threadContext = this.getThreadContext();
        Map<String, Object> map = (Map)threadContext.get();
        if (!this.isMapNull(map)) {
            map.clear();
            threadContext.set(map);
        }

    }

    private boolean isMapNull(Map<String, Object> map) {
        return map == null;
    }
}
