package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.UserDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Role;
import by.tr.web.kinorating.domain.Status;
import by.tr.web.kinorating.domain.User;

public class MySQLUserDAOImpl implements UserDAO {
	
	private static final String GET_USER_MARKS = "SELECT * FROM user_marks WHERE user_id=?";
	private static final String UPDATE_USER_MARK_QUERY = "UPDATE user_marks SET user_mark=? WHERE movie_id=?";
	private static final String SELECT_OLD_USER_MARK_QUERY = "SELECT * FROM user_marks WHERE user_id=(SELECT us_id FROM users WHERE us_login=?) AND movie_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String DELETE_USER_MARK_QUERY = "DELETE FROM user_marks WHERE user_id=(SELECT us_id FROM users WHERE us_login=?) AND movie_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String ADD_USER_MARK_TO_MOVIE_QUERY = "INSERT INTO user marks (user_id, movie_id, user_mark) VALUES ((SELECT us_id FROM users WHERE us_login=?), (SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?), ?)";
	private static final String SELECT_USER_MARK_TO_MOVIE_QUERy = "SELECT * FROM us_marks WHERE user_id=(SELECT us_id FROM users WHERE login=?) AND movie_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String USER_EXISTS_QUERY = "SELECT us_id FROM users WHERE us_login=? OR us_email=?";
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
			String login = user.getLogin();
			String email = user.getEmail();
			if (isExistedUser(login)) {
				return false;
			}
			if (isExistedUser(email)) {
				return false;
			}
			preparedStatement = connection.prepareStatement(CREATE_USER_QUERY);
			preparedStatement.setString(1, login);
			String password = DigestUtils.md5Hex(user.getPassword());
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, user.getRole().toString());
			preparedStatement.setString(5, user.getStatus().toString());
			preparedStatement.setDouble(6, user.getRating());
			Date date = new Date(user.getRegistrationDate().getTime());
			preparedStatement.setDate(7, date);
			rowCount = preparedStatement.executeUpdate();
			if (rowCount != 0) {
				return true;
			}
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
		return false;
	}
	
	private boolean isExistedUser(String string) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(USER_EXISTS_QUERY);
			statement.setString(1, string);
			statement.setString(2, string);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return false;
	}
	
	@Override
	public User readUserByLogin(String login) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		User user = new User();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
			statement.setString(1, login);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int userID = resultSet.getInt(1);
				user.setLogin(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setEmail(resultSet.getString(4));
				String role = resultSet.getString(5).toUpperCase();
				user.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				user.setStatus(Status.valueOf(status));
				user.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				user.setRegistrationDate(new Date(regDate.getTime()));
				Map<Movie, Integer> marks = getUserMarks(userID);
				user.setMarks(marks);
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
		User user = new User();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
			statement.setString(1, email);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int userID = resultSet.getInt(1);
				user.setLogin(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setEmail(resultSet.getString(4));
				String role = resultSet.getString(5).toUpperCase();
				user.setRole(Role.valueOf(role));
				String status = resultSet.getString(6).toUpperCase();
				user.setStatus(Status.valueOf(status));
				user.setRating(resultSet.getDouble(7));
				Date regDate = resultSet.getDate(8);
				user.setRegistrationDate(new Date(regDate.getTime()));
				Map<Movie, Integer> marks = getUserMarks(userID);
				user.setMarks(marks);
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
	
	private Map<Movie, Integer> getUserMarks(int id) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		Map<Movie, Integer> marks = new HashMap<Movie, Integer>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(GET_USER_MARKS);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			MovieDAO movieDAO = factory.getMovieDAO();
			while (resultSet.next()) {
				Movie movie = movieDAO.readMovieById(resultSet.getInt(2));
				Integer mark = resultSet.getInt(3);
				marks.put(movie, mark);
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
		return marks;
	}
	
	@Override
	public User checkUserWithLogin(User user) throws DAOException{
		String login = user.getLogin();
		String userPassword = user.getPassword();
		User userFromDB = readUserByLogin(login);
		if (userFromDB.getLogin() == null) {
			return new User();
		}
		String userDBPassword = userFromDB.getPassword();
		if (!checkPassword(userPassword, userDBPassword)) {
			return new User();
		} 
		return userFromDB;
	}


	@Override
	public User checkUserWithEmail(User user) throws DAOException {
		String email = user.getEmail();
		String userPassword = user.getPassword();
		User userFromDB = readUserByEmail(email);
		if (userFromDB.getLogin() == null) {
			return new User();
		}
		String userDBPassword = userFromDB.getPassword();
		if (!checkPassword(userPassword, userDBPassword)) {
			return new User();
		}
		return userFromDB;
	}
	
	private static boolean checkPassword(String password, String passwordDB) {
		if (password.equals(DigestUtils.md5Hex(passwordDB))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUserPassword(User user, String newPassword) throws DAOException {
		if (checkUserWithLogin(user).getLogin() == null ) {
			return false;
		}
		if (checkUserWithEmail(user).getLogin() == null) {
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
			if (rowCount != 0) {
				return true;
			}

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
		return false;
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
			if (rowCount != 0) {
				return true;
			}
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
		return false;
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
			if (rowCount != 0) {
				return true;
			}

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
		return false;
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
			if (rowCount != 0) {
				return true;
			}
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
		return false;
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
	public boolean updateUserMarkToMovie(User user, Movie movie, int newMark) throws DAOException {
		DAOAbstractFactory factory =  MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		if (newMark != 0) {
			if(!isExistedMark(user, movie)) {
				if (addUserMarkToMovie(user, movie, newMark)) {
					movieDAO.updateMovieMark(movie, newMark, 1);
					return true;
				} 
				return false;
			} 
			int oldMark = changeUserMark(user, movie, newMark);
			if (oldMark > 0) {
				newMark -= oldMark;
				movieDAO.updateMovieMark(movie, newMark, 0);
				return true;
			} 
			return false;
		}
		if (removeUserMark(user, movie)) {
			movieDAO.updateMovieMark(movie, newMark, -1);
			return true;
		}
		return false;
	}
	
	
	private boolean isExistedMark(User user, Movie movie) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_USER_MARK_TO_MOVIE_QUERy);
			statement.setString(1, user.getLogin());
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (resultSet != null) {
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
		return false;
	}
	
	private boolean addUserMarkToMovie(User user, Movie movie, int newMark) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(ADD_USER_MARK_TO_MOVIE_QUERY);
			statement.setString(1, user.getLogin());
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.setInt(4, newMark);
			rowCount = statement.executeUpdate();
			if (rowCount > 0) {
				return true;
			}
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
		return false;
	}
	
	private boolean removeUserMark(User user, Movie movie) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_USER_MARK_QUERY);
			statement.setString(1, user.getLogin());
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			rowCount = statement.executeUpdate();
			if (rowCount > 0) {
				return true;
			}
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
		return false;
	}
	
	private int changeUserMark(User user, Movie movie, int newMark) throws DAOException {
		Connection connection = null;
		PreparedStatement selectStatement= null;
		PreparedStatement insertStatement= null;
		ResultSet resultSet = null;
		int oldMark = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			selectStatement = connection.prepareStatement(SELECT_OLD_USER_MARK_QUERY);
			selectStatement.setString(1, user.getLogin());
			selectStatement.setString(2, movie.getTitle());
			selectStatement.setString(3, movie.getDirector());
			resultSet = selectStatement.executeQuery();
			int movieID = 0;
			while (resultSet.next()) {
				movieID = resultSet.getInt(2);
				oldMark = resultSet.getInt(3);
			}
			insertStatement = connection.prepareStatement(UPDATE_USER_MARK_QUERY);
			insertStatement.setInt(1, newMark);
			insertStatement.setInt(2, movieID);
			int rowCount = insertStatement.executeUpdate();
			if (rowCount == 0) {
				return 0;
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (selectStatement != null) {
				try {
					selectStatement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (insertStatement != null) {
				try {
					insertStatement.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if(connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return oldMark;
	}
	
	@Override
	public boolean delete(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement statement= null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_USER_QUERY);
			String login = user.getLogin();;
			if (user.getLogin() == null) {
				login = EMPTY_STRING;
			}
			String email = user.getEmail();;
			if (user.getEmail() == null) {
				email = EMPTY_STRING;
			} 
			statement.setString(1, login);
			statement.setString(2, email);
			rowCount = statement.executeUpdate();
			if (rowCount != 0) {
				return true;
			}
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
		return false;
	}

}
