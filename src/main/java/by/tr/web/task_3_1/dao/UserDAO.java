package by.tr.web.task_3_1.dao;

import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.domain.Role;
import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;

public interface UserDAO {

	boolean createUser(User user) throws DAOException;

	User readUserByLogin(String login) throws DAOException;

	User readUserByEmail(String email) throws DAOException;
	
	User checkUserWithLogin(User user) throws DAOException;
	
	User checkUserWithEmail(User user) throws DAOException;
	
	boolean updateUserPassword(User user, String newPassword) throws DAOException;
	
	boolean updateUserEmail(User user, String newEmail) throws DAOException;
	
	boolean updateUserRole(User user, Role newRole) throws DAOException;
	
	boolean updateUserStatus(User user, Status newStatus) throws DAOException;
	
	boolean updateUserRating(User user, double newRating) throws DAOException;

	void delete(User user) throws DAOException;
}
