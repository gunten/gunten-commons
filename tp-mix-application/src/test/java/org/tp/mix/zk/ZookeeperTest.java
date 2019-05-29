package org.tp.mix.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.tp.mix.vo.User;

import java.util.List;

/**
 * 开源客户端ZKClient Demo
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/3/6
 */
public class ZookeeperTest {

    private static String zkServers = "47.98.101.251:2181,47.98.101.251:2182,47.98.101.251:2183";
    private static int sessionTimeout = 10000;
    private static int connectionTimeout = 10000;

    public static void main(String[] args) throws InterruptedException {
        ZkClient client = new ZkClient(zkServers, sessionTimeout, connectionTimeout, new SerializableSerializer());


        if (!client.exists("/jike5")) {
            User user = new User(1, "用户A", 18, "yizhihua@qq.com");
            String path = client.create("/jike5", user, CreateMode.PERSISTENT);
            System.err.println("created path:" + path);

            client.create("/jike5/ke5", null, CreateMode.EPHEMERAL);
        }

        Stat stat = new Stat();
        User u = client.readData("/jike5", stat);
        System.err.println(u.toString());
        System.err.println(stat);

        //获取子节点
        List<String> cList = client.getChildren("/jike5");
        System.err.println(cList.toString());

        u.setAge(28);
        client.writeData("/jike5", u);


//        System.out.println("del /jike5 " + zc.delete("/jike5"));
        System.err.println("循环del /jike5 " + client.deleteRecursive("/jike5"));


        // 除子节点变化外，节点本身创建和删除也会收到通知
        client.subscribeChildChanges("/jike20", new ZkChildListener());

        //订阅数据内容变化
        client.subscribeDataChanges("/jike20", new ZkDataListener());
        Thread.sleep(Integer.MAX_VALUE);

    }


    private static class ZkChildListener implements IZkChildListener {

        public void handleChildChange(String parentPath,
                                      List<String> currentChilds) throws Exception {

            System.err.println(parentPath);
            System.err.println(currentChilds.toString());
        }
    }

    private static class ZkDataListener implements IZkDataListener {

        public void handleDataChange(String dataPath, Object data)
                throws Exception {
            System.err.println(dataPath+":"+data.toString());
        }

        public void handleDataDeleted(String dataPath) throws Exception {
            System.err.println(dataPath);
        }

    }
}
