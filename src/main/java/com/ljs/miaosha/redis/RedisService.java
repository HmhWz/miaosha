package com.ljs.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
	@Autowired
	JedisPool jedisPool;
	//RedisService引用JedisPool--JedisPool在RedisService，只有创建RedisService的实例才可以获取JedisPool的bean
	//所以需要单独拿出JedisPool的bean

	/**
	 * 获取单个对象
	 */
	public <T> T get(KeyPrefix prefix, String key, Class<T> data) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			String sval = jedis.get(realKey);
			T t = stringToBean(sval, data);
			return t;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 移除对象,删除
	 */
	public boolean delete(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			long ret = jedis.del(realKey);
			return ret > 0;//删除成功，返回大于0
			//return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 设置单个、多个对象
	 */
	public <T> boolean set(KeyPrefix prefix, String key, T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			String s = beanToString(value);
			if (s == null || s.length() <= 0) {
				return false;
			}
			int seconds = prefix.expireSeconds();
			if (seconds <= 0) {
				jedis.set(realKey, s);
			} else {
				jedis.setex(realKey, seconds, s);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 减少值
	 */
	public <T> Long decr(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 增加值
	 */
	public <T> Long incr(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 检查key是否存在
	 */
	public <T> boolean exitsKey(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 将字符串转换为Bean对象
	 * <p>
	 * parseInt()返回的是基本类型int 而valueOf()返回的是包装类Integer
	 * Integer是可以使用对象方法的  而int类型就不能和Object类型进行互相转换 。
	 * int a=Integer.parseInt(s);
	 * Integer b=Integer.valueOf(s);
	 */
	public static <T> T stringToBean(String s, Class<T> clazz) {
		if (s == null || s.length() == 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return ((T) Integer.valueOf(s));
		} else if (clazz == String.class) {
			return (T) s;
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(s);
		} else {
			JSONObject json = JSON.parseObject(s);
			return JSON.toJavaObject(json, clazz);
		}
	}

	/**
	 * 将Bean对象转换为字符串类型
	 */
	public static <T> String beanToString(T value) {
		if (value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return "" + value;
		} else if (clazz == String.class) {
			return "" + value;
		} else if (clazz == long.class || clazz == Long.class) {
			return "" + value;
		} else {
			return JSON.toJSONString(value);
		}
	}

	private void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public <T> boolean set(String key, T value) {
		Jedis jedis = null;
		//在JedisPool里面取得Jedis
		try {
			jedis = jedisPool.getResource();
			//将T类型转换为String类型
			String s = beanToString(value);
			if (s == null) {
				return false;
			}
			jedis.set(key, s);
			return true;
		} finally {
			returnToPool(jedis);
		}
	}

	public <T> T get(String key, Class<T> data) {
		Jedis jedis = null;
		//在JedisPool里面取得Jedis
		try {
			jedis = jedisPool.getResource();
			System.out.println("jedis:" + jedis);
			String sval = jedis.get(key);
			System.out.println("sval:" + sval);
			//将String转换为Bean入后传出
			T t = stringToBean(sval, data);
			return t;
		} finally {
			returnToPool(jedis);
		}
	}
}
