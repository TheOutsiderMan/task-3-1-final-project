package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.ReviewDAO;
import by.tr.web.kinorating.dao.UserDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Review;
import by.tr.web.kinorating.domain.User;

public class MySQLReviewDAOImpl implements ReviewDAO {

	private static final String DELETE_REVIEW_QUERY = "DELETE FROM reviews WHERE us_id IN (SELECT us_id FROM users WHERE us_login=?) AND mov_id=?";
	private static final String UPDATE_REVIEW_TEXT_QUERY = "UPDATE reviews SET rev_review=? WHERE us_id IN (SELECT us_id FROM users WHERE us_login=?) AND mov_id=?";
	private static final String SELECT_ALL_REVIEWS_QUERY = "SELECT mov_id, rev_review, rev_date, us_login, rev_id FROM reviews JOIN users ON reviews.us_id=users.us_id";
	private static final String SELECT_MOVIE_REVIEW_QUERY = "SELECT rev_id, rev_review, rev_date, us_login FROM reviews JOIN users ON reviews.us_id=users.us_id WHERE mov_id=?";
	private static final String SELECT_REVIEWS_BY_USER_QUERY = "SELECT * FROM reviews WHERE us_id IN (SELECT us_id FROM users WHERE us_id=?)";
	private static final String SELECT_REVIEW_QUERY = "SELECT rev_id, rev_review, rev_date FROM reviews WHERE us_id IN (SELECT us_id FROM users WHERE us_login=?) AND mov_id=?";
	private static final String CREATE_REVIEW_QUERY = "INSERT INTO reviews (us_id, mov_id, rev_review, rev_date) VALUES ((SELECT us_id FROM users WHERE us_login=?), ?, ?, ?)";
	private static final String SELECT_REVIEW_ID_QUERY = "SELECT rev_id FROM reviews WHERE us_id IN (SELECT us_id FROM users WHERE us_login=?) AND mov_id=?";
	
	private static final String PROBLEM_WITH_READING_FROM_DB = "Problem with reading from DB";
	private static final String PROBLEM_WITH_CONNECTION_POOL = "Problem with connection pool";
	private static final String PROBLEM_TO_CLOSE_RESOURCE = "Problem to close resource";
	private static final String PROBLEM_WITH_EDITING_DB = "Problem with editing DB";
	
	private static final Logger logger = LogManager.getLogger(MySQLReviewDAOImpl.class);
	
	@Override
	public boolean createReview(Review review) throws DAOException {
		User author = review.getAuthor();
		Movie movie = review.getReviewedMovie();
		if (isExistedReview(author, movie)) {
			return false;
		}
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(CREATE_REVIEW_QUERY);
			statement.setString(1, author.getLogin());
			statement.setInt(2, movie.getId());;
			statement.setString(3, review.getTextReview());
			Date date = new Date(review.getAdditionDate().getTime());
			statement.setDate(4, date);
			rowCount = statement.executeUpdate();
			if (rowCount != 0) {
				return true;
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_EDITING_DB, e);
			throw new DAOException(PROBLEM_WITH_EDITING_DB, e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return false;
	}
	
	private boolean isExistedReview(User user, Movie movie) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_REVIEW_ID_QUERY);
			statement.setString(1, user.getLogin());
			statement.setInt(2, movie.getId());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_READING_FROM_DB, e);
			throw new DAOException(PROBLEM_WITH_READING_FROM_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return false;
	}
	
	
	@Override
	public Review readReview(User user, Movie movie) throws DAOException {
		Review review = new Review();
		if (!isExistedReview(user, movie) ) {
			return review;
		}
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_REVIEW_QUERY);
			statement.setString(1, user.getLogin());
			statement.setInt(2, movie.getId());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				review.setId(resultSet.getInt(1));
				review.setTextReview(resultSet.getString(2));
				java.util.Date date = new java.util.Date(resultSet.getDate(3).getTime());
				review.setAdditionDate(date);
			}
			review.setAuthor(user);
			review.setReviewedMovie(movie);
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_READING_FROM_DB, e);
			throw new DAOException(PROBLEM_WITH_READING_FROM_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return review;
	}

	@Override
	public List<Review> readReviewsByUser(User user) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Review> reviews = new ArrayList<Review>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_REVIEWS_BY_USER_QUERY);
			statement.setString(1, user.getLogin());
			resultSet = statement.executeQuery();
			Review review = new Review();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			MovieDAO movieDAO = factory.getMovieDAO();
			while (resultSet.next()) {
				review.setId(resultSet.getInt(1));
				int movieID = resultSet.getInt(3);
				Movie movie = movieDAO.readMovieById(movieID);
				review.setReviewedMovie(movie);
				review.setAuthor(user);
				review.setTextReview(resultSet.getString(4));
				java.util.Date date = new java.util.Date(resultSet.getDate(5).getTime());
				review.setAdditionDate(date);
				reviews.add(review);
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_READING_FROM_DB, e);
			throw new DAOException(PROBLEM_WITH_READING_FROM_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return reviews;
	}

	@Override
	public List<Review> readMovieReviews(Movie movie) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Review> reviews = new ArrayList<Review>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_MOVIE_REVIEW_QUERY);
			statement.setInt(1, movie.getId());
			resultSet = statement.executeQuery();
			Review review = new Review();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			while (resultSet.next()) {
				User user = userDAO.readUserByLogin(resultSet.getString(4));
				review.setReviewedMovie(movie);
				review.setAuthor(user);
				review.setId(resultSet.getInt(1));
				review.setTextReview(resultSet.getString(2));
				java.util.Date date = new java.util.Date(resultSet.getDate(3).getTime());
				review.setAdditionDate(date);
				reviews.add(review);
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_READING_FROM_DB, e);
			throw new DAOException(PROBLEM_WITH_READING_FROM_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return reviews;
	}

	@Override
	public List<Review> readAllReviews() throws DAOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Review> reviews = new ArrayList<Review>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SELECT_ALL_REVIEWS_QUERY);
			Review review = new Review();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			UserDAO userDAO = factory.getUserDAO();
			MovieDAO movieDAO = factory.getMovieDAO();
			while (resultSet.next()) {
				int movieID = resultSet.getInt(1);
				Movie movie = movieDAO.readMovieById(movieID);
				review.setReviewedMovie(movie);
				review.setTextReview(resultSet.getString(2));
				java.util.Date date = new java.util.Date(resultSet.getDate(3).getTime());
				review.setAdditionDate(date);
				User user = userDAO.readUserByLogin(resultSet.getString(4));
				review.setId(resultSet.getInt(5));
				review.setAuthor(user);
				reviews.add(review);
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_READING_FROM_DB, e);
			throw new DAOException(PROBLEM_WITH_READING_FROM_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return reviews;
	}

	@Override
	public boolean updateReview(Review review, String newText) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		Movie movie = review.getReviewedMovie();
		User user = review.getAuthor();
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_REVIEW_TEXT_QUERY);
			statement.setString(1, newText);
			statement.setString(2, user.getLogin());
			statement.setInt(3, movie.getId());
			rowCount = statement.executeUpdate();
			if (rowCount != 0) {
				return true;
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_EDITING_DB, e);
			throw new DAOException(PROBLEM_WITH_EDITING_DB, e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return false;
	}

	@Override
	public boolean deleteReview(Review review) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		Movie movie = review.getReviewedMovie();
		User user = review.getAuthor();
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_REVIEW_QUERY);
			statement.setString(1, user.getLogin());
			statement.setInt(2, movie.getId());
			rowCount = statement.executeUpdate();
			if (rowCount != 0) {
				return true;
			}
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_EDITING_DB, e);
			throw new DAOException(PROBLEM_WITH_EDITING_DB, e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		return false;
	}
}
