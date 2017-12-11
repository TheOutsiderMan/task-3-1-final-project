package by.tr.web.task_3_1.dao.mysql.impl;

import by.tr.web.task_3_1.dao.UserDAO;
import by.tr.web.task_3_1.domain.User;

public class MySQLUserDAOImpl implements UserDAO {

	@Override
	public boolean createUser(User user) {

		return false;
	}

	@Override
	public User readUserByLogin(String login) {

		return null;
	}

	@Override
	public User readUserByEmail(String email) {

		return null;
	}

	@Override
	public void updateUser(User user) {
		
	}

	@Override
	public void delete(User user) {
		
	}

}
