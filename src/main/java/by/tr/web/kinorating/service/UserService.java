package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface UserService {

	User authenticateUserByLogin(String login, String password) throws ServiceException;

	User authenticateUserByEmail(String email, String password) throws ServiceException;

	boolean registerUser(String login, String email, String password) throws ServiceException;

	boolean banUser(String login) throws ServiceException;
	
	boolean unbanUser(String login) throws ServiceException;

	boolean changeRating(String login, double rating) throws ServiceException;
	
	boolean changeUserMarkToMovie(String login, int movieID, int newMark) throws ServiceException;
	
	boolean deleteUserMarkToMovie(String login, int movieID) throws ServiceException;
	
	boolean deleteUser(String login) throws ServiceException;
	
	List<User> showAllUsers() throws ServiceException;
}
