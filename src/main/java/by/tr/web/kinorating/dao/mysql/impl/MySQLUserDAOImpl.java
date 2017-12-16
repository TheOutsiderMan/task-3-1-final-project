package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.codec.digest.DigestUtils;

import by.tr.web.kinorating.dao.UserDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Role;
import by.tr.web.kinorating.domain.Status;
import by.tr.web.kinorating.domain.User;

public class MySQLUserDAOImpl implements UserDAO {
	
	private static final String UPDATE_USER_STATUS_QUERY = "UPDATE users SET us_status=? WHERE us_login=?";
	private static final String UPDATE_USER_RATING_QUERY = "UPDATE users SET us_rating=? WHERE us_login=?";
	private static final String UPDATE_USER_ROLE_QUERY = "UPDATE users SET us_role=? WHERE us_login=?";
	private static final String UPDATE_USER_EMAIL_QUERY = "UPDATE users SET us_email=? WHERE us_login=?";
	private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE users SET us_password=? WHERE us_login=?";
	private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE us_login=? OR us_email=?";
	private static final String EMPTY_STRING = "";
	private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE us_email=?";
	private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE us_login=?";
	private static final String CREATE_USER_QUERY = "INSERT INTO users (us_login, us_password, us_email, us_role, us_status, us_rating, us_registration_date) VALUES(?, ?, ?, ?, ?, ?, ?)";

	@Override
	public boolean createUser(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			if (readUserByLogin(user.getLogin()) != null) {
				return false;
			}
			if (readUserByEmail(user.getEmail()) != null) {
				return false;
			}
			preparedStatement = connection.prepareStatement(CREATE_USER_QUERY);
			preparedStatement.setString(1, user.getLogin());
			String password = DigestUtils.md5Hex(user.getPassword());
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getRole().toString());
			preparedStatement.setString(5, user.getStatus().toString());
			preparedStatement.setDouble(6, user.getRating());
			Date date = new Date(user.getRegistrationDate().getTime());
			preparedStatement.setDate(7, date);
			rowCount = preparedStatement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0 ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public User readUserByLogin(String login) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
			statement.setString(1, login);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setLogin(resultSet.getString(2));
				user.setPassword(EMPTY_STRING);
				user.setEmail(resultSet.getString(3));
				String role = resultSet.getString(5).toUpperCase();
				user.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				user.setStatus(Status.valueOf(status));
				user.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				user.setRegistrationDate(new Date(regDate.getTime()));
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return user;
	}

	@Override
	public User readUserByEmail(String email) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
			statement.setString(1, email);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setLogin(resultSet.getString(2));
				user.setPassword(EMPTY_STRING);
				user.setEmail(resultSet.getString(3));
				String role = resultSet.getString(5).toUpperCase();
				user.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				user.setStatus(Status.valueOf(status));
				user.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				user.setRegistrationDate(new Date(regDate.getTime()));
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return user;
	}
	
	
	
	@Override
	public User checkUserWithLogin(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		User userFromDB = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
			statement.setString(1, user.getLogin());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String savedPassword = resultSet.getString(3);
				if (!savedPassword.equals(DigestUtils.md5Hex(user.getPassword()))) {
					return userFromDB;
				}
				userFromDB = new User();
				userFromDB.setLogin(resultSet.getString(2));
				userFromDB.setPassword(EMPTY_STRING);
				userFromDB.setEmail(resultSet.getString(3));
				String role = resultSet.getString(5).toUpperCase();
				userFromDB.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				userFromDB.setStatus(Status.valueOf(status));
				userFromDB.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				userFromDB.setRegistrationDate(new Date(regDate.getTime()));
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return userFromDB;
	}


	@Override
	public User checkUserWithEmail(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		User userFromDB = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
			statement.setString(1, user.getEmail());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String savedPassword = resultSet.getString(3);
				if (!savedPassword.equals(DigestUtils.md5Hex(user.getPassword()))) {
					return userFromDB;
				}
				userFromDB = new User();
				userFromDB.setLogin(resultSet.getString(2));
				userFromDB.setPassword(EMPTY_STRING);
				userFromDB.setEmail(resultSet.getString(3));
				String role = resultSet.getString(5).toUpperCase();
				userFromDB.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				userFromDB.setStatus(Status.valueOf(status));
				userFromDB.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				userFromDB.setRegistrationDate(new Date(regDate.getTime()));
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return userFromDB;
	}


	@Override
	public boolean updateUserPassword(User user, String newPassword) throws DAOException {
		if (checkUserWithLogin(user) == null ) {
			return false;
		}
		if (checkUserWithEmail(user) == null) {
			return false;
		}
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_USER_PASSWORD_QUERY);
			String password = DigestUtils.md5Hex(newPassword);
			statement.setString(1, password);
			statement.setString(2, user.getLogin());
			rowCount = statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateUserEmail(User user, String newEmail) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_USER_EMAIL_QUERY);
			statement.setString(1, newEmail);
			statement.setString(2, user.getLogin());
			rowCount = statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean updateUserRole(User user, Role newRole) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_USER_ROLE_QUERY);
			statement.setString(1, newRole.toString());
			statement.setString(2, user.getLogin());
			rowCount = statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean updateUserStatus(User user, Status newStatus) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_USER_STATUS_QUERY);
			statement.setString(1, newStatus.toString());
			statement.setString(2, user.getLogin());
			rowCount = statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean updateUserRating(User user, double newRating) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_USER_RATING_QUERY);
			statement.setDouble(1, newRating);
			statement.setString(2, user.getLogin());
			rowCount = statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public void delete(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_USER_QUERY);
			String login = null;
			if (user.getLogin() != null) {
				login = user.getLogin();
			} else {
				login = EMPTY_STRING;
			}
			String email = null;
			if (user.getEmail() != null) {
				email = user.getEmail();
			} else {
				email = EMPTY_STRING;
			}
			statement.setString(1, login);
			statement.setString(2, email);
			statement.executeUpdate();
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
	}

}
