package com.ljs.miaosha.config;

import com.ljs.miaosha.domain.MiaoshaUser;
import com.ljs.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	MiaoshaUserService miaoshaUserService;

	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest webRequest,
								  WebDataBinderFactory arg3) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		String paramToken = request.getParameter(MiaoshaUserService.COOKIE1_NAME_TOKEN);
		//获取cookie
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE1_NAME_TOKEN);
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		MiaoshaUser user = miaoshaUserService.getByToken(token, response);
		return user;
	}

	public String getCookieValue(HttpServletRequest request, String cookie1NameToken) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookie1NameToken)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//返回参数的类型
		Class<?> clazz = parameter.getParameterType();
		return clazz == MiaoshaUser.class;
	}

}
