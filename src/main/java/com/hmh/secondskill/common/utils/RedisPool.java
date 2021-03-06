package com.hmh.secondskill.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @auther hmh
 * @date 3/6 21:30
 */
@Component
public class RedisPool {

	private static JedisPool pool;

	private static Integer maxTotal = 300;

	private static Integer maxIdle = 100;

	private static Integer maxWait = 10000;

	private static Boolean testOnBorrow = true;

//	@Value("${spring.redis.host}")
	private static String redisIp = "localhost";

//	@Value("${spring.redis.port}")
	private static Integer redisPort = 6379;

	private static void initPool() {
		JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setTestOnBorrow(testOnBorrow);
		config.setBlockWhenExhausted(true);
		config.setMaxWaitMillis(maxWait);

		pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
	}

	// 类加载到 jvm 时直接初始化连接池
	static {
		initPool();
	}

	public static Jedis getJedis() {
		return pool.getResource();
	}

	public static void jedisPoolClose(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
}
