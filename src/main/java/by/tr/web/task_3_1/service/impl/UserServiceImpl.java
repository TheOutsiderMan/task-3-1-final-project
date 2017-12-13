package by.tr.web.task_3_1.service.impl;

import java.util.Date;

import by.tr.web.task_3_1.dao.DAOAbstractFactory;
import by.tr.web.task_3_1.dao.UserDAO;
import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.dao.mysql.MySQLDAOFactory;
import by.tr.web.task_3_1.domain.Role;
import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.UserService;
import by.tr.web.task_3_1.service.exception.ServiceException;
import by.tr.web.task_3_1.service.validation.Validator;

public class UserServiceImpl implements UserService {

	@Override
	public User authenticateUserByLogin(String login, String password) throws ServiceException {
		Validator validator = new Validator();
		if (validator.validateLogin(login) && validator.validatePassword(password)) {
			User user = new User();
			user.setLogin(login);
			user.setPassword(password);
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			User existedUser = null;
			try {
				existedUser = userDAO.checkUserWithLogin(user);
			} catch (DAOException e) {
				throw new ServiceException(e);
			}
			if (existedUser.getStatus().equals(Status.BANNED)) {
				return null;
			} else {
				return existedUser;
			}
		} else {
			return null;
		}
	}

	@Override
	public User authenticateUserByEmail(String email, String password) throws ServiceException {
		Validator validator = new Validator();
		if (validator.validateEmail(email) && validator.validatePassword(password)) {
			User user = new User();
			user.setEmail(email);
			user.setPassword(password);
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			User existedUser = null;
			try {
				existedUser = userDAO.checkUserWithEmail(user);
			} catch (DAOException e) {
				throw new ServiceException(e);
			}
			if (existedUser.getStatus().equals(Status.BANNED)) {
				return null;
			} else {
				return existedUser;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean registerUser(String login, String email, String password) throws ServiceException {
		Validator validator = new Validator();
		if (validator.validateLogin(login) && validator.validateEmail(email) && validator.validatePassword(password)) {
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
			boolean registered = false;
			try {
				registered = userDAO.createUser(user);
			} catch (DAOException e) {
				throw new ServiceException(e);
			}
			return registered;
		} else {
			return false;
		}
	}

	@Override
	public void banUser(User user) throws ServiceException {
		//validate
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		try {
			userDAO.updateUserStatus(user, Status.BANNED);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void unbanUser(User user) throws ServiceException {
		// validate
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		try {
			userDAO.updateUserStatus(user, Status.ALLOWED);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void changeRating(User user, double rating) throws ServiceException {
		//
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		try {
			userDAO.updateUserRating(user, rating);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
