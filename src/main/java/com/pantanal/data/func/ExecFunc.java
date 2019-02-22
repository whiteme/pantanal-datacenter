package com.pantanal.data.func;
import redis.clients.jedis.Jedis;

@FunctionalInterface
public interface ExecFunc<T> {
    T callBack(Jedis jedis);
}
