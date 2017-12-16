package by.tr.web.kinorating.service;

import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface UserService {

	User authenticateUserByLogin(String login, String password) throws ServiceException;

	User authenticateUserByEmail(String email, String password) throws ServiceException;

	boolean registerUser(String login, String email, String password) throws ServiceException;

	void banUser(User user) throws ServiceException;
	
	void unbanUser(User user) throws ServiceException;

	void changeRating(User user, double rating) throws ServiceException;
}
