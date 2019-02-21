package com.pantanal.data.cache.redis;


import com.pantanal.data.func.ExecFunc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;


public class RedisClient {

    private static Logger logger = LogManager.getLogger(RedisClient.class.getName());
    private JedisPool pool;

    public RedisClient(String host,String pwd) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxTotal(300);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        this.pool = new JedisPool(config, host, 6379, 3000, pwd);
    }

    public RedisClient(String host,int port,String pwd) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxTotal(300);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        this.pool = new JedisPool(config, host, port, 3000, pwd);
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public void close(Jedis jedis){
        jedis.close();
//		pool.returnResource(jedis);  //3.0
    }


    public <T> T exec(ExecFunc<T> exec){
        Jedis jedis = pool.getResource();
        T t = exec.callBack(jedis);
        jedis.close();
        return t;
    }

    public static void main(String[] args) {
        RedisClient rc = new RedisClient("127.0.0.1","leon");

        //Jedis jedis = rc.pool.getResource();
        //jedis.lpush("test", "m1","m2","m3");
        //System.out.println(jedis.lpop("test"));
        //jedis.del("test");

    }


}


