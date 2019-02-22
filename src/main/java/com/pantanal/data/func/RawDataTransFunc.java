package com.pantanal.data.func;

import redis.clients.jedis.Jedis;
@FunctionalInterface
public interface RawDataTransFunc<S , T> {
    T trans(S raw );
}
