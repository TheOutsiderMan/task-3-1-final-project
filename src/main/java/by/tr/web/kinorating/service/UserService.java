package by.tr.web.kinorating.service;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface UserService {

	User authenticateUserByLogin(String login, String password) throws ServiceException;

	User authenticateUserByEmail(String email, String password) throws ServiceException;

	boolean registerUser(String login, String email, String password) throws ServiceException;

	boolean banUser(User user) throws ServiceException;
	
	boolean unbanUser(User user) throws ServiceException;

	boolean changeRating(User user, double rating) throws ServiceException;
	
	boolean changeUserMarkToMovie(User user, Movie movie, int newMark) throws ServiceException;
	
	boolean deleteUser(User user) throws ServiceException;
}
