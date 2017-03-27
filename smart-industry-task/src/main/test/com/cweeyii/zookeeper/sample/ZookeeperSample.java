package com.cweeyii.zookeeper.sample;

import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wenyi on 16/7/30.
 * Email:caowenyi@meituan.com
 */
public class ZookeeperSample implements Watcher {
    private static CountDownLatch downLatch=new CountDownLatch(1);
    private static ZooKeeper zooKeeper=null;
    @Before
    public void setUp() throws IOException {
        zooKeeper=new ZooKeeper("127.0.0.1:2181,127.0.0.1:3181,127.0.0.1:4181",5000,new ZookeeperSample());
    }

    @After
    public void destroy(){

    }

    @Test
    public void testCase() throws IOException, InterruptedException {
        System.out.println(zooKeeper.getState());
        downLatch.await();
        System.out.println("zookeeper session established!");
        System.out.println("sessionId=" + Long.toHexString(zooKeeper.getSessionId()));
        System.out.println("sessionPasswd="+ Arrays.toString(zooKeeper.getSessionPasswd()));
    }

    @Test
    public void testCreateDataNode() throws IOException, InterruptedException, KeeperException {

        System.out.println(zooKeeper.getState());
        downLatch.await();
        System.out.println("zookeeper session established!");
        //String path1=zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //System.out.println("success create data node=" + path1);
        //String path2=zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        //System.out.println("success create data node=" + path2);

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallback(), "I am context");
        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallback(), "I am context");

        System.out.println("all data node established!");
        Thread.sleep(1000);
        //String path1_duplicate=zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //System.out.println("success create data node=" + path1_duplicate);

    }

    @Test
    public void testChildrenNode() throws IOException, InterruptedException, KeeperException {
        System.out.println(zooKeeper.getState());
        downLatch.await();
        System.out.println("zookeeper session established!");
        String path="/zk-book";
        zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        zooKeeper.create(path+"/c1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        List<String> childrenNode=zooKeeper.getChildren(path,true);
        System.out.println(childrenNode);
        zooKeeper.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(5000);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:"+event);

        if(event.getState()== Event.KeeperState.SyncConnected){
            if(Event.EventType.None==event.getType()&&null==event.getPath()){
                downLatch.countDown();
            }else if(event.getType()== Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("ReGet Children="+zooKeeper.getChildren(event.getPath(),true));
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class IStringCallback implements AsyncCallback.StringCallback{

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("rc="+rc+",path="+path+",ctx="+ctx+",name="+name);
        }
    }
}
