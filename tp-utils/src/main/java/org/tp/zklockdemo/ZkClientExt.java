package org.tp.zklockdemo;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/3/19
 */
public class ZkClientExt extends ZkClient {

    public ZkClientExt(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
    }

    @Override
    public void watchForData(final String path) {
        retryUntilConnected(new Callable<Object>() {

            public Object call() throws Exception {
                Stat stat = new Stat();
                _connection.readData(path, stat, true);
                return null;
            }

        });
    }

}
