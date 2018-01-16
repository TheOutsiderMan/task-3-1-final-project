package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Role;
import by.tr.web.kinorating.domain.Status;
import by.tr.web.kinorating.domain.User;

public interface UserDAO {

	boolean createUser(User user) throws DAOException;

	User readUserByLogin(String login) throws DAOException;

	User readUserByEmail(String email) throws DAOException;
	
	List<User> readAllUsers() throws DAOException;
	
	User checkUserWithLogin(User user) throws DAOException;
	
	User checkUserWithEmail(User user) throws DAOException;
	
	boolean updateUserPassword(User user, String newPassword) throws DAOException;
	
	boolean updateUserEmail(User user, String newEmail) throws DAOException;
	
	boolean updateUserRole(User user, Role newRole) throws DAOException;
	
	boolean updateUserStatus(User user, Status newStatus) throws DAOException;
	
	boolean updateUserRating(User user, double newRating) throws DAOException;
	
	boolean updateUserMarkToMovie(User user, Movie movie, int newMark) throws DAOException;
	
	boolean removeUserMark(User user, Movie movie) throws DAOException;

	boolean delete(User user) throws DAOException;
	
}
