package by.tr.web.kinorating.service.impl;

import java.util.Date;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.UserDAO;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Role;
import by.tr.web.kinorating.domain.Status;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.Validator;

public class UserServiceImpl implements UserService {

	private static final int STEP_TO_CHANGE_USER_RATING = 1;
	private static final int MAX_DIFF_RATING_AND_MARK = 3;
	private static final int DEFAULT_RATING = 0;

	@Override
	public User authenticateUserByLogin(String login, String password) throws ServiceException {
		if (!Validator.validateLoginPassword(login, password)) {
			return new User();
		}
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
		Status status = existedUser.getStatus();
		if (isBannedUser(status)) {
			return new User();
		}
		return existedUser;
	}

	@Override
	public User authenticateUserByEmail(String email, String password) throws ServiceException {
		if (!Validator.validateEmailPassword(email, password)) {
			return new User();
		}
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
		Status status = existedUser.getStatus();
		if (isBannedUser(status)) {
			return new User();
		}
		return existedUser;
	}

	private boolean isBannedUser(Status status) {
		if (status == null || status.equals(Status.BANNED)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean registerUser(String login, String email, String password) throws ServiceException {
		if (!Validator.validateLoginEmailPassword(login, email, password)) {
			return false;
		}
		User user = new User();
		user.setEmail(email);
		user.setLogin(login);
		user.setPassword(password);
		user.setRating(DEFAULT_RATING);
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
	}

	@Override
	public boolean banUser(User user) throws ServiceException {
		if (!Validator.validateUser(user)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean banned = false;
		try {
			banned = userDAO.updateUserStatus(user, Status.BANNED);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return banned;
	}
	
	@Override
	public boolean unbanUser(User user) throws ServiceException {
		if (!Validator.validateUser(user)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean unbanned = false;
		try {
			unbanned = userDAO.updateUserStatus(user, Status.ALLOWED);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return unbanned;
	}

	@Override
	public boolean changeRating(User user, double rating) throws ServiceException {
		if (!Validator.validateRating(rating)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean updated = false;
		try {
			updated = userDAO.updateUserRating(user, rating);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return updated;
	}

	@Override
	public boolean changeUserMarkToMovie(User user, Movie movie, int newMark) throws ServiceException {
		if (!Validator.validateUser(user)) {
			return false;
		}
		if (!Validator.validateMovie(movie)) {
			return false;
		}
		if (!Validator.validateMovieMark(newMark)) {
			return false;
		}
		double movieRating = movie.getRating();
		double difference = Math.abs(movieRating - newMark);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		if (difference > MAX_DIFF_RATING_AND_MARK) {
			try {
				userDAO.updateUserRating(user, user.getRating() - STEP_TO_CHANGE_USER_RATING);
			} catch (DAOException e) {
				throw new ServiceException(e);
			}
		}
		boolean changed = false;
		try {
			changed = userDAO.updateUserMarkToMovie(user, movie, newMark);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return changed;
	}

	@Override
	public boolean deleteUser(User user) throws ServiceException {
		if (!Validator.validateUser(user)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean deleted = false;
		try {
			deleted = userDAO.delete(user);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return deleted;
	}
	
	
}
