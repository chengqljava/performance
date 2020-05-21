package com.chengql.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;

/**
 * @author
 * @ClassName: RedisTemplate
 * @Auther: chengql
 * @Date: 2019/03/24
 * @Description: 重写RedisTemplate,加入选库
 */
@SuppressWarnings("rawtypes")
public class RedisTemplate extends org.springframework.data.redis.core.RedisTemplate {

    public static ThreadLocal<Integer> indexdb = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        try {
            Integer dbIndex = indexdb.get();
            // 如果设置了dbIndex
            if (dbIndex != null) {
                if (connection instanceof JedisConnection) {
                    if (((JedisConnection) connection).getNativeConnection().getDB().intValue() != dbIndex) {
                        connection.select(dbIndex);
                    }
                } else {
                    connection.select(dbIndex);
                }
            } else {
                connection.select(0);
            }
        } finally {
            indexdb.remove();
        }
        return super.preProcessConnection(connection, existingConnection);
    }

}
