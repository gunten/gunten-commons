package org.tp.zk.lock;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.tp.zk.ZKPathUtil;
import org.tp.zk.exception.ZKLockException;

import java.io.IOException;
import java.util.List;

/**
 * 锁实现
 */
public class DistributedLock extends AbstractLock implements Lock  {

    private static final int DEFAULT_THREAD_WAIT_TIME = 120;

    private static final int DEFAULT_LOCK_WAIT_TIME = 5000;

    private ThreadLocal<String> local = new ThreadLocal<String>();

    public DistributedLock(String zkServers, int zkClientTimeout) {
        zkClient = new ZkClient(zkServers, zkClientTimeout);
    }


    @Override
    public void tryLock(String path) throws Exception{
        if ( !tryLock(path, -1) )
        {
            throw new IOException("Lost connection while trying to acquire lock: " + path);
        }
    }

    @Override
    public boolean tryLock(String path, int waitTime) {
        if (StringUtils.isEmpty(path)) {
            throw new ZKLockException("路径不能为空或者空字符串");
        }
        // 对path 进行分析, 将最后一个节点留下self，创建一个临时节点
        List<String> list = ZKPathUtil.analysisZkPath(path);
        String self = list.get(list.size() - 1);

        String parentPath = ZKPathUtil.createZkPath(list) + "/" + self;
        if (!zkClient.exists(parentPath)) {
            zkClient.createPersistent(parentPath, true);
        }
        // 创建顺序节点
        String sequentialPath = zkClient.createEphemeralSequential(parentPath + "/lock-", null);
        // 放入 threadlocal 里面
        local.set(sequentialPath);

        return internalLock((long) waitTime, parentPath, sequentialPath);

    }

    @Override
    public void unLock() {
        boolean res = true;
        try {
            String path = local.get();

            res = deleteWithParentPath(path);
//            if(!res){
//                System.err.println("-------------解锁失败:" + path);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public String getNode(){
        return local.get();
    }
}
