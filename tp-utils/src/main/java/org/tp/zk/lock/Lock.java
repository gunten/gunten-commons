package org.tp.zk.lock;
/**
 * 锁     2018.10.06
 * 锁必须在事务外执行，如果放入事务内，锁的效果无法体现，会出现解锁后并发继续调用，而事务还未提交
 */
public interface Lock {

    /**
     * 获取不到时阻塞等待
     * @param path         锁在ZK的路径，可定义各项业务维度：/xxx/系统模块/系统业务/锁业务定义，
     * @param path
     */
    void tryLock(String path) throws Exception;

    /**
     * 执行锁
     *
     * @param path         锁在ZK的路径，可定义各项业务维度：/xxx/系统模块/系统业务/锁业务定义，
     * @param waitTime     定义锁超时时间，当锁一定时间后还未释放，不能一直占用资源，所以需要设定释放时间 ，一般建议：5000,5秒，不要超过30秒（具体根据程序的执行能力，尽量不要在锁内执行太过复杂的程序，造成锁释放事务丢失，或占用过多系统资源
     * @return
     * @throws Exception
     */
     boolean tryLock(String path, int waitTime) throws Exception;

    /**
     * 解锁
     * 所以使用锁的场景，必须在事务外执行解锁操作
     */
    void unLock();
}
