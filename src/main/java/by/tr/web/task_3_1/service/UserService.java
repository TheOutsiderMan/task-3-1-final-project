package by.tr.web.task_3_1.service;

import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.exception.ServiceException;

public interface UserService {

	User authenticateUserByLogin(String login, String password) throws ServiceException;

	User authenticateUserByEmail(String email, String password) throws ServiceException;

	boolean registerUser(String login, String email, String password) throws ServiceException;

	void banUser(User user) throws ServiceException;
	
	void unbanUser(User user) throws ServiceException;

	void changeRating(User user, double rating) throws ServiceException;
}
