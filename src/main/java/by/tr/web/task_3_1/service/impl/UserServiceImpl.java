package by.tr.web.task_3_1.service.impl;

import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public boolean authenticateUserByLogin(String login, String password) {

		return false;
	}

	@Override
	public boolean authenticateUserByEmail(String email, String password) {

		return false;
	}

	@Override
	public void registerUser(User user) {
		
	}

	@Override
	public void banUser(User user, Status BANNED) {
		
	}

	@Override
	public void changeRating(User user, double rating) {
		
	}

}
