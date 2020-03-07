package com.ljs.miaosha.service;

import com.ljs.miaosha.dao.MiaoshaUserDao;
import com.ljs.miaosha.domain.MiaoshaUser;
import com.ljs.miaosha.exception.GlobalException;
import com.ljs.miaosha.redis.MiaoshaUserKey;
import com.ljs.miaosha.redis.RedisService;
import com.ljs.miaosha.result.CodeMsg;
import com.ljs.miaosha.util.MD5Util;
import com.ljs.miaosha.util.UUIDUtil;
import com.ljs.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
	public static final String COOKIE1_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;

	public MiaoshaUser getById(long id) {
		MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
		if (user != null) {
			return user;
		}
		user = miaoshaUserDao.getById(id);
		if (user != null) {
			redisService.set(MiaoshaUserKey.getById, "" + id, user);
		}
		return user;
	}

	/**
	 * 注意数据修改时候，保持缓存与数据库的一致性
	 * 需要传入token
	 */
	public boolean updatePassword(String token, long id, String passNew) {
		MiaoshaUser user = getById(id);
		if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOTEXIST);
		}
		MiaoshaUser toupdateuser = new MiaoshaUser();
		toupdateuser.setId(id);
		toupdateuser.setPwd(MD5Util.inputPassToDbPass(passNew, user.getSalt()));
		miaoshaUserDao.update(toupdateuser);
		// 对象缓存删除
		redisService.delete(MiaoshaUserKey.getById, "" + id);
		user.setPwd(toupdateuser.getPwd());
		// 更新token缓存
		redisService.set(MiaoshaUserKey.token, token, user);
		return true;
	}

	/**
	 * 从缓存里面取得值，取得value
	 */
	public MiaoshaUser getByToken(String token, HttpServletResponse response) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		// 延长有效期
		// 重新设置缓存里面的值，使用之前cookie里面的token
		if (user != null) {
			addCookie(user, token, response);
		}
		return user;
	}

	public String loginString(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			return CodeMsg.SERVER_ERROR.getMsg();
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if (user == null) {
			return CodeMsg.MOBILE_NOTEXIST.getMsg();
		}
		String dbPass = user.getPwd();
		String dbSalt = user.getSalt();
		String tmppass = MD5Util.formPassToDBPass(formPass, dbSalt);
		if (!tmppass.equals(dbPass)) {
			return CodeMsg.PASSWORD_ERROR.getMsg();
		}
		String token = UUIDUtil.uuid();
		addCookie(user, token, response);
		return token;
	}

	public CodeMsg login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			return CodeMsg.SERVER_ERROR;
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if (user == null) {
			return CodeMsg.MOBILE_NOTEXIST;
		}
		String dbPass = user.getPwd();
		String dbSalt = user.getSalt();
		String tmppass = MD5Util.formPassToDBPass(formPass, dbSalt);
		if (!tmppass.equals(dbPass)) {
			return CodeMsg.PASSWORD_ERROR;
		}
		String token = UUIDUtil.uuid();
		addCookie(user, token, response);
		return CodeMsg.SUCCESS;
	}

	public void addCookie(MiaoshaUser user, String token, HttpServletResponse response) {
		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE1_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
