package com.ljs.miaosha.controller;

import com.ljs.miaosha.domain.User;
import com.ljs.miaosha.redis.RedisService;
import com.ljs.miaosha.redis.UserKey;
import com.ljs.miaosha.result.CodeMsg;
import com.ljs.miaosha.result.Result;
import com.ljs.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "hello world";
	}

	@RequestMapping("/hello")
	@ResponseBody
	public Result<String> hello() {
		return Result.success("hello sss");
	}

	@RequestMapping("/helloError")
	@ResponseBody
	public Result<String> helloError() {//0代表成功
		return Result.error(CodeMsg.SERVER_ERROR);
	}

	@RequestMapping("/db/get/{id}")
	@ResponseBody
	public Result<User> dbGet(@PathVariable int id) {
		User user = userService.getById(id);
		System.out.println("res:" + user.getName());
		return Result.success(user);
	}

	@RequestMapping("/db/tx")
	@ResponseBody
	public Result<Boolean> dbTx() {//0代表成功		

		System.out.println("res:" + userService.tx());
		return Result.success(userService.tx());
	}

	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<Long> redisGet() {//0代表成功		
		Long l1 = redisService.get("key1", Long.class);
		//redisService.get("key1",String.class);
		return Result.success(l1);
	}

	@RequestMapping("/redis/get1")
	@ResponseBody
	public Result<String> redisGet1() {//0代表成功		
		String res = redisService.get("key1", String.class);
		//redisService.get("key1",String.class);
		//System.out.println("res:"+userService.tx());
		return Result.success(res);
	}

	@RequestMapping("/redis/getbyid")
	@ResponseBody
	public Result<User> redisGetById() {//0代表成功		
		User res = redisService.get(UserKey.getById, "" + 1, User.class);
		//redisService.get("key1",String.class);
		//System.out.println("res:"+userService.tx());
		return Result.success(res);
	}

	/**
	 * 避免key被不同类的数据覆盖
	 * 使用Prefix前缀-->不同类别的缓存，用户、部门、
	 */
	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<Boolean> redisSet() {//0代表成功		
		User user = new User(1, "1111");
		boolean f = redisService.set(UserKey.getById, "" + 1, user);
		return Result.success(true);
	}

	@RequestMapping("/s")
	@ResponseBody
	public String say() {
		return "hello world";
	}

}
