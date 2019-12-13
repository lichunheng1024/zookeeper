package org.apache.zookeeper.test.system.learn_source_test;

import org.apache.zookeeper.*;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author lch
 */
public class SyncCreateApiDemo implements Watcher {


    private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static final int sessionTimeout=900000;


    public static void main(String[] args) throws Exception{
        ZooKeeper zk=new ZooKeeper("localhost:2181,localhost:2182,localhost:2183",sessionTimeout,new SyncCreateApiDemo());
        connectedSemaphore.await();
        String uuid= UUID.randomUUID().toString();
        String finalPath="/learn-zk-source"+uuid;
        String createResult = zk.create(finalPath, "source learn".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        byte[] data = zk.getData(finalPath, false, null);
        System.out.println("---------createResult="+createResult);

        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected==event.getState()){
            connectedSemaphore.countDown();
        }
    }
}
