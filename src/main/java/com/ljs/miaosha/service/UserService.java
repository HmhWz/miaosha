package com.ljs.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ljs.miaosha.dao.UserDao;
import com.ljs.miaosha.domain.User;

@Service
public class UserService {
	@Autowired
	UserDao userDao;

	public User getById(int id) {
		return userDao.getById(id);
	}


	@Transactional
	public boolean tx() {
		User user = new User();
		user.setName("ljs");
		userDao.insert(user);

		User user1 = new User();
		user1.setName("ljs2");
		userDao.insert(user1);

		return true;
	}
}
