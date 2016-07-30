package com.cweeyii.cache.redis;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.elasticsearch.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cweeyii on 11/6/16.
 */
public class RedisOperateTest extends TestCase {
    private ApplicationContext context;
    private ShardedJedis shardedJedis;

    @BeforeClass
    public void setUp() {
        String[] paths = {"classpath*:webmvc-config.xml", "classpath:applicationContext-datasource.xml", "classpath:applicationContext-task.xml",
                "classpath*:webmvc-basic-webapp.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-mq.xml",
                "classpath*:applicationContext-mq-sender.xml", "classpath*:applicationContext-mq-listener.xml",
                "classpath*:applicationContext-redis.xml"};
        context = new ClassPathXmlApplicationContext(paths);
        ShardedJedisPool shardedJedisPool = context.getBean("sharedJedisPool", ShardedJedisPool.class);
        shardedJedis = shardedJedisPool.getResource();
    }

    public <K, V> void set(K key, V value) {
        String strKey = JSON.toJSONString(key);
        String strValue = JSON.toJSONString(value);
        shardedJedis.set(strKey, strValue);
    }

    public <K> void append(K key, String value) {
        String strKey = JSON.toJSONString(key);
        String oldValue = get(key);
        String newValue = oldValue + value;
        String strValue = JSON.toJSONString(newValue);
        shardedJedis.set(strKey, strValue);
    }


    public <K, V> V get(K key) {
        String strKey = JSON.toJSONString(key);
        String strValue = shardedJedis.get(strKey);
        return (V) JSON.parse(strValue);
    }

    public <K> Long del(K key) {
        String strKey = JSON.toJSONString(key);
        return shardedJedis.del(strKey);
    }

    @Test
    public void testString() {
        set("name", "xinxin");
        System.out.println(get("name"));
        append("name", " is my lover");
        System.out.println(get("name"));
        System.out.println(del("name"));
        System.out.println(get("name"));
    }

    @Test
    public void testMap() {
        Map<String, User> map = new HashMap<>();
        User user = new User();
        user.setName("xinxin");
        user.setAge(22);
        user.setQq("123456");
        user.setPhones(Lists.newArrayList("110", "119"));
        User user2 = new User();
        user2.setName("xinxin");
        user2.setAge(22);
        user2.setQq("123456");
        user2.setPhones(Lists.newArrayList("110", "119"));
        map.put("user1", user);
        map.put("user2", user2);
        set("user", map);
        System.out.print(get("user"));
    }

    @Test
    public void testList() {
        User user = new User();
        user.setName("xinxin");
        user.setAge(22);
        user.setQq("123456");
        user.setPhones(Lists.newArrayList("110", "119"));
        User user2 = new User();
        user2.setName("xinxin");
        user2.setAge(22);
        user2.setQq("123456");
        user2.setPhones(Lists.newArrayList("110", "119"));
        List<User> list = Lists.newArrayList(user, user2);
        set("user", list);
        System.out.print(get("user"));
    }

    private static class User {
        private String name;
        private Integer age;
        private String qq;
        private List<String> phones;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public List<String> getPhones() {
            return phones;
        }

        public void setPhones(List<String> phones) {
            this.phones = phones;
        }
    }
}
