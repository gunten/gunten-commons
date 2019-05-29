package org.tp.zk.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.tp.zk.exception.ZKLockException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 抽象锁父类
 */
public abstract class AbstractLock {

    protected ZkClient zkClient;


    /**
     *
     * @param millisToWait 等待时间
     * @param parentPath 父节点
     * @param selfPath  本身路径
     * @return
     */
    protected boolean internalLock(Long millisToWait , String parentPath, String selfPath) {

        final long startMillis = System.currentTimeMillis();
        boolean hasTheLock = false;
        boolean doDelete = false;

        try {
            while (!hasTheLock) {

                List<String> subNodes = zkClient.getChildren(parentPath);
                Collections.sort(subNodes);
                int index = subNodes.indexOf(selfPath.substring((parentPath).length() + 1));
                switch (index) {
                    case -1: {
                        throw new ZKLockException("节点已不在了..., 节点路径：" + selfPath);
                    }
                    case 0: {
                        hasTheLock = true;
                    }
                    default: {
                    }
                }

                if (!hasTheLock) {
                    // 订阅比自己次小顺序节点的删除事件
                    String preNode = parentPath + "/" + subNodes.get(index - 1);

                    final CountDownLatch latch = new CountDownLatch(1);
                    final IZkDataListener listener = new IZkDataListener() {
                        @Override
                        public void handleDataChange(String dataPath, Object data) {
                            System.err.println("验证数据变换");
                        }

                        @Override
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown();
                        }

                    };
                    zkClient.subscribeDataChanges(preNode, listener);

                    try {
                        if (millisToWait != null && millisToWait != -1) {
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            if (millisToWait <= 0) {
                                doDelete = true;    // timed out - delete our node
//                                throw new ZKLockTimeoutException("获取锁超时了！");
                                break; //亦可不抛异常
                            }

                            latch.await(millisToWait, TimeUnit.MICROSECONDS); // 在latch上await
                        } else {
                            latch.await(); // 在latch上await
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        zkClient.unsubscribeDataChanges(preNode, listener);
                    }
                }
            } //end loop
        }catch ( Exception e ) {
            //发生异常需要删除节点
            doDelete = true;
            throw e;
        } finally {
            if (doDelete) {
//                System.out.println("-----------------超时 delete:" + selfPath);
                deleteWithParentPath(selfPath);
            }
        }

        return hasTheLock;
    }

    /**
     * 检查是否已经到可以执行的程度
     *
     * @param parentPath 父节点
     * @param selfPath   本身路径
     *
     * @return List<String> null:是最小节点  not null: subNodes
     */
    private List<String> checkMinPath(String parentPath, String selfPath) {
        List<String> subNodes = zkClient.getChildren(parentPath);
        Collections.sort(subNodes);
        int index = subNodes.indexOf(selfPath.substring((parentPath).length() + 1));
        switch (index) {
            case -1: {
                throw new ZKLockException("节点已不在了..., 节点路径："+ selfPath);
            }
            case 0: {
                return null;
            }
            default: {

                return subNodes;
            }
        }
    }


    /**
     * 与父节点同时删除，如果存在其他子节点，则只删除子节点
     * 父节点下不存在子节点也删除
     *
     * @param path 节点路径
     * @return
     */
    protected boolean deleteWithParentPath(String path){
        boolean res = false;
        String parentPath = path.substring(0, path.lastIndexOf(47));
        String selfNode = path.substring((parentPath).length() + 1);
        boolean delPar = false;
        if(zkClient.exists(parentPath)){
            List<String> subNodes = zkClient.getChildren(parentPath);
            if(subNodes.contains(selfNode) && subNodes.size() == 1){
                delPar = true;
            }else if(subNodes.isEmpty()){
                delPar = true;
            }else{
                subNodes.remove(selfNode);
            }
        }
        //需要先删除子节点
        res = deleteIfExists(path);
        if(delPar){
            deleteIfExists(parentPath);
        }
//        System.out.println("---------------------- delete :"+ path +" res: "+res);
        return res;
    }


    private boolean deleteIfExists(String path){
        if (zkClient.exists(path)){
            return zkClient.delete(path);
        }
        return false;
    }

}
