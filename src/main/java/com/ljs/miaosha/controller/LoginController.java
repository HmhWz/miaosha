package com.ljs.miaosha.controller;

import ch.qos.logback.classic.Logger;
import com.ljs.miaosha.redis.RedisService;
import com.ljs.miaosha.result.CodeMsg;
import com.ljs.miaosha.result.Result;
import com.ljs.miaosha.service.MiaoshaUserService;
import com.ljs.miaosha.service.UserService;
import com.ljs.miaosha.vo.LoginVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/login")
@Controller
public class LoginController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	@Autowired
	MiaoshaUserService miaoshaUserService;

	private static Logger log = (Logger) LoggerFactory.getLogger(Logger.class);

	@RequestMapping("/to_login")
	public String toLogin() {
		return "login";//返回页面login

	}

	@RequestMapping("/do_login_test")//作为异步操作
	@ResponseBody
	public Result<String> doLogintest(HttpServletResponse response, @Valid LoginVo loginVo) {
		String token = miaoshaUserService.loginString(response, loginVo);
		return Result.success(token);
	}


	//使用JSR303校验
	@RequestMapping("/do_login")//作为异步操作
	@ResponseBody
	public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
		CodeMsg cm = miaoshaUserService.login(response, loginVo);
		if (cm.getCode() == 0) {
			return Result.success(true);
		} else {
			return Result.error(cm);
		}
	}
}
