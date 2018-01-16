package by.tr.web.kinorating.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.UserDAO;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Role;
import by.tr.web.kinorating.domain.Status;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.MovieValidator;
import by.tr.web.kinorating.service.validation.UserValidator;

public class UserServiceImpl implements UserService {

	private static final String PROBLEM_WITH_DELETING_USER = "Problem with deleting user";
	private static final String PROBLEM_WITH_UPDATING_USER_STATUS = "Problem with updating user's status";
	private static final String PROBLEM_WITH_REGISTRATION_USER = "Problem with registration user";
	private static final String PROBLEM_WITH_AUTHENTICATION_USER = "Problem with authentication user";
	private static final String PROBLEM_WITH_DELETING_USER_MARK_TO_MOVIE = "Problem with deleting user's mark to movie";
	private static final String PROBLEM_WITH_UPDATING_USER_MARK_TO_MOVIE = "Problem with updating user's mark to movie";
	private static final String PROBLEM_WITH_UPDATING_USER_RATING = "Problem with updating user's rating";
	private static final String PROBLEM_WITH_READING_MOVIE = "Problem with reading movie";
	private static final String PROBLEM_WITH_READING_USER = "Problem with reading user";
	private static final String PROBLEM_WITH_GETTING_ALL_USERS = "Problem with getting all users";
	
	private static final int STEP_TO_CHANGE_USER_RATING = 1;
	private static final int MAX_DIFF_RATING_AND_MARK = 3;
	private static final int DEFAULT_RATING = 0;
	
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Override
	public List<User> showAllUsers() throws ServiceException {
		List<User> users;
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		try {
			users = userDAO.readAllUsers();
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_GETTING_ALL_USERS, e);
			throw new ServiceException(PROBLEM_WITH_GETTING_ALL_USERS, e);
		}
		return users;
	}

	@Override
	public User authenticateUserByLogin(String login, String password) throws ServiceException {
		if (!UserValidator.validateLoginPassword(login, password)) {
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
			logger.error(PROBLEM_WITH_AUTHENTICATION_USER, e);
			throw new ServiceException(PROBLEM_WITH_AUTHENTICATION_USER, e);
		}
		Status status = existedUser.getStatus();
		if (isBannedUser(status)) {
			return new User();
		}
		return existedUser;
	}

	@Override
	public User authenticateUserByEmail(String email, String password) throws ServiceException {
		if (!UserValidator.validateEmailPassword(email, password)) {
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
			logger.error(PROBLEM_WITH_AUTHENTICATION_USER, e);
			throw new ServiceException(PROBLEM_WITH_AUTHENTICATION_USER, e);
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
		if (!UserValidator.validateLoginEmailPassword(login, email, password)) {
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
			logger.error(PROBLEM_WITH_REGISTRATION_USER, e);
			throw new ServiceException(PROBLEM_WITH_REGISTRATION_USER, e);
		}
		return registered;
	}

	@Override
	public boolean banUser(String login) throws ServiceException {
		if (!UserValidator.validateLogin(login)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean banned = false;
		User user = new User();
		user.setLogin(login);
		try {
			banned = userDAO.updateUserStatus(user, Status.BANNED);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_USER_STATUS, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_USER_STATUS, e);
		}
		return banned;
	}
	
	@Override
	public boolean unbanUser(String login) throws ServiceException {
		if (!UserValidator.validateLogin(login)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean unbanned = false;
		User user = new User();
		user.setLogin(login);
		try {
			unbanned = userDAO.updateUserStatus(user, Status.ALLOWED);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_USER_STATUS, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_USER_STATUS, e);
		}
		return unbanned;
	}

	@Override
	public boolean changeRating(String login, double rating) throws ServiceException {
		if (!UserValidator.validateLogin(login) || !UserValidator.validateRating(rating)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean updated = false;
		User user = new User();
		user.setLogin(login);
		try {
			updated = userDAO.updateUserRating(user, rating);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_USER_RATING, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_USER_RATING, e);
		}
		return updated;
	}

	@Override
	public boolean changeUserMarkToMovie(String login, int movieID, int newMark) throws ServiceException {
		if (!UserValidator.validateLogin(login)) {
			return false;
		}
		if (!MovieValidator.validateMovieId(movieID) || !UserValidator.validateMovieMark(newMark)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		User user = null;
		try {
			user = userDAO.readUserByLogin(login);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_USER, e);
			throw new ServiceException(PROBLEM_WITH_READING_USER, e);
		}
		MovieDAO movieDAO = factory.getMovieDAO();
		Movie movie = null;
		try {
			movie = movieDAO.readMovieById(movieID);
		} catch (DAOException e1) {
			logger.error(PROBLEM_WITH_READING_MOVIE, e1);
			throw new ServiceException(PROBLEM_WITH_READING_MOVIE, e1);
		}
		if ((user == null) || (movie == null)) {
			return false;
		}
		if ((user.getLogin() == null) || (movie.getTitle() == null)) {
			return false;
		}
		double movieRating = movie.getRating();
		double difference = Math.abs(movieRating - newMark);
		if (difference > MAX_DIFF_RATING_AND_MARK && movie.getVoteAmount() != 0) {
			try {
				userDAO.updateUserRating(user, user.getRating() - STEP_TO_CHANGE_USER_RATING);
			} catch (DAOException e2) {
				logger.error(PROBLEM_WITH_UPDATING_USER_RATING, e2);
				throw new ServiceException(PROBLEM_WITH_UPDATING_USER_RATING, e2);
			}
		}
		boolean changed = false;
		try {
			changed = userDAO.updateUserMarkToMovie(user, movie, newMark);
		} catch (DAOException e3) {
			logger.error(PROBLEM_WITH_UPDATING_USER_MARK_TO_MOVIE, e3);
			throw new ServiceException(PROBLEM_WITH_UPDATING_USER_MARK_TO_MOVIE, e3);
		}
		return changed;
	}

	@Override
	public boolean deleteUserMarkToMovie(String login, int movieID) throws ServiceException {
		if (!UserValidator.validateLogin(login) || !MovieValidator.validateMovieId(movieID)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		User user = new User();
		user.setLogin(login);
		Movie movie = new Movie();
		movie.setId(movieID);
		boolean deleted = false;
		try {
			deleted = userDAO.removeUserMark(user, movie);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_DELETING_USER_MARK_TO_MOVIE, e);
			throw new ServiceException(PROBLEM_WITH_DELETING_USER_MARK_TO_MOVIE, e);
		}
		return deleted;
	}

	@Override
	public boolean deleteUser(String login) throws ServiceException {
		if (!UserValidator.validateLogin(login)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		boolean deleted = false;
		User user = new User();
		user.setLogin(login);
		try {
			deleted = userDAO.delete(user);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_DELETING_USER, e);
			throw new ServiceException(PROBLEM_WITH_DELETING_USER, e);
		}
		return deleted;
	}
	
	
}
