package by.tr.web.task_3_1.service.impl;

import by.tr.web.task_3_1.domain.Role;
import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.UserService;
import by.tr.web.task_3_1.service.validation.Validator;

public class UserServiceImpl implements UserService{

	@Override
	public User authenticateUserByLogin(String login, String password) {
		Validator validator = new Validator();
		//test. not finished.
		User user =  new User();
		user.setLogin(login);
		user.setEmail("123123");
		user.setRating(4.67);
		user.setRole(Role.USER);
		return user;
	}

	@Override
	public User authenticateUserByEmail(String email, String password) {
		//test. not finished.
		User user =  new User();
		user.setLogin("123123");
		user.setEmail(email);
		user.setRating(4.67);
		user.setRole(Role.USER);
		return user;
	}

	@Override
	public boolean registerUser(String login, String email, String password) {
		return false;
		
	}

	@Override
	public void banUser(User user, Status BANNED) {
		
	}

	@Override
	public void changeRating(User user, double rating) {
		
	}

}
