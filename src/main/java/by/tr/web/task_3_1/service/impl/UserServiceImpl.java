package by.tr.web.task_3_1.service.impl;

import java.util.Date;

import by.tr.web.task_3_1.dao.DAOAbstractFactory;
import by.tr.web.task_3_1.dao.UserDAO;
import by.tr.web.task_3_1.dao.mysql.MySQLDAOFactory;
import by.tr.web.task_3_1.domain.Role;
import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.UserService;
import by.tr.web.task_3_1.service.validation.Validator;

public class UserServiceImpl implements UserService{

	@Override
	public User authenticateUserByLogin(String login, String password) {
		Validator validator = new Validator();
		if(validator.validateLogin(login) && validator.validatePassword(password)) {
			User user =  new User();
			user.setLogin(login);
			user.setPassword(password);
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			user = userDAO.readUserByLogin(login);
			return user;
		} else {
			return null;
		}
	}

	@Override
	public User authenticateUserByEmail(String email, String password) {
		Validator validator = new Validator();
		if (validator.validateEmail(email) && validator.validatePassword(password)) {
			User user =  new User();
			user.setEmail(email);
			user.setPassword(password);
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			user = userDAO.readUserByEmail(email);
			return user;
		} else {
			return null;
		}
	}

	@Override
	public boolean registerUser(String login, String email, String password) {
		Validator validator =  new Validator();
		if(validator.validateLogin(login) && validator.validateEmail(email) && validator.validatePassword(password)) {
			User user = new User();
			user.setEmail(email);
			user.setLogin(login);
			user.setPassword(password);
			user.setRating(0);
			Date registrationDate = new Date(); 
			user.setRegistrationDate(registrationDate);
			user.setStatus(Status.ALLOWED);
			user.setRole(Role.USER);
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			return userDAO.createUser(user);
		} else {
			return false;
		}
	}

	@Override
	public void banUser(User user, Status BANNED) {
		//
	}

	@Override
	public void changeRating(User user, double rating) {
		//
	}

}
