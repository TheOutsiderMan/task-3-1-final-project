package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.tr.web.kinorating.dao.ActorDAO;
import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;

public class MySQLMovieDAOImpl implements MovieDAO{

	private static final String SELECT_MOVIE_BY_ID_QUERY = "SELECT * FROM movies_translate JOIN movies ON movies_translate.mov_id=movies.mov_id WHERE movies_translate.mov_id=?";
	private static final String DELETE_MOVIE_QUERY = "DELETE FROM movies WHERE mov_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String UPDATE_MOVIE_RATING_QUERY = "UPDATE movies SET mov_rating=? WHERE mov_id=?";
	private static final String SELECT_RATING_QUERY = "SELECT mov_id, mov_rating, mov_mark_ammount FROM movies WHERE mov_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String UPDATE_MOVIE_GENRE_QUERY = "UPDATE movies_translate SET mov_genre=? WHERE mov_title=? AND mov_director=?";
	private static final String UPDATE_MOVIE_DIRECTOR_QUERY = "UPDATE movies_translate SET mov_director=? WHERE mov_title=? AND mov_director=?";
	private static final String UPDATE_MOVIE_YEAR_QUERY = "UPDATE movies SET mov_release_year=? WHERE mov_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String UPDATE_MOVIE_LENGTH_QUERY = "UPDATE movies SET mov_length=? WHERE mov_id=(SELECT mov_id FROM movies_translate WHERE mov_title=? AND mov_director=?)";
	private static final String UPDATE_MOVIE_TITLE_QUERY = "UPDATE movies_translate SET mov_title=? WHERE mov_title=? AND mov_director=?";
	private static final String SELECT_MOVIE_BY_TITLE_QUERY = "SELECT mov_title, mov_director, mov_genre, mov_release_year, mov_length, mov_rating, mov_addition_date, movies_translate.mov_id, lang_short_name FROM movies_translate JOIN movies ON movies_translate.mov_id=movies.mov_id WHERE mov_title=?";
	private static final String SELECT_RANDOM_MOVIES_QUERY = "SELECT movies_translate.mov_id, mov_title, mov_director, mov_genre, mov_release_year, mov_length, mov_rating, mov_addition_date FROM movies_translate JOIN movies ON movies_translate.mov_id=movies.mov_id WHERE lang_short_name=? ORDER BY RAND() LIMIT ?";
	private static final String SELECT_ALL_MOVIES_ONE_TRANSLATION_QUERY = "SELECT movies_translate.mov_id, mov_title, mov_director, mov_genre, mov_release_year, mov_length, mov_rating, mov_addition_date FROM movies_translate JOIN movies ON movies_translate.mov_id=movies.mov_id WHERE lang_short_name=?";
	private static final String CREATE_MOVIE_TRANSLATE_QUERY = "INSERT INTO movies_translate (mov_id, lang_short_name, mov_title, mov_director, mov_genre) VALUES (?, ?, ?, ?, ?)";
	private static final String CREATE_MOVIE_QUERY = "INSERT INTO movies (mov_release_year, mov_length, mov_rating, mov_addition_date) VALUES (?, ?, ?, ?)";
	private static final String LAST_INSERT_ID_QUERY = "SELECT LAST_INSERT_ID()";

	@Override
	public boolean createMovie(Movie movie, String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statementTranslate = null;
		Statement lastID = null;
		ResultSet resultSet = null;
		int movieID = 0;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(CREATE_MOVIE_QUERY);
			statement.setInt(1, movie.getYear());
			statement.setInt(2, movie.getLength());
			statement.setDouble(3, movie.getRating());
			Date date = new Date(movie.getAdditionDate().getTime());
			statement.setDate(4, date);
			rowCount = statement.executeUpdate();
			if (rowCount == 0) {
				return false;
			}
			lastID = connection.createStatement();
			resultSet =	lastID.executeQuery(LAST_INSERT_ID_QUERY);
			if (resultSet.next()) {
				movieID = resultSet.getInt(1);
			}
			if (movieID != 0) {
				statementTranslate = connection.prepareStatement(CREATE_MOVIE_TRANSLATE_QUERY);
				statementTranslate.setInt(1, movieID);
				statementTranslate.setString(2, locale);
				statementTranslate.setString(3, movie.getTitle());
				statementTranslate.setString(4, movie.getDirector());
				statementTranslate.setString(5, movie.getGenre());
				rowCount = statementTranslate.executeUpdate();
				if (rowCount == 0) {
					return false;
				}
			} else {
				return false;
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			System.err.println(e);
			throw new DAOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (lastID != null) {
				try {
					lastID.close();
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
			if (statementTranslate != null) {
				try {
					statementTranslate.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return true;
	}

	@Override
	public List<Movie> readAllMovies(String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Movie> movies = new ArrayList<Movie>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ALL_MOVIES_ONE_TRANSLATION_QUERY);
			statement.setString(1, locale);
			resultSet = statement.executeQuery();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			ActorDAO actorDAO = factory.getActorDAO();
			while (resultSet.next()) {
				Movie movie = new Movie();
				int movieID = resultSet.getInt(1);
				movie.setTitle(resultSet.getString(2));
				movie.setDirector(resultSet.getString(3));
				movie.setGenre(resultSet.getString(4));
				movie.setYear(resultSet.getInt(5));
				movie.setLength(resultSet.getInt(6));
				movie.setRating(resultSet.getDouble(7));
				java.util.Date date =  new java.util.Date(resultSet.getDate(8).getTime());
				movie.setAdditionDate(date);
				List<Actor> actors =  actorDAO.readActorsFromOneMovie(movieID, locale);
				movie.setActors(actors);
				movies.add(movie);
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return movies;
	}

	@Override
	public List<Movie> readMovieByTitle(String title) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Movie> movies = new ArrayList<Movie>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_MOVIE_BY_TITLE_QUERY);
			resultSet = statement.executeQuery();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			ActorDAO actorDAO = factory.getActorDAO();
			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setTitle(resultSet.getString(1));
				movie.setDirector(resultSet.getString(2));
				movie.setGenre(resultSet.getString(3));
				movie.setYear(resultSet.getInt(4));
				movie.setLength(resultSet.getInt(5));
				movie.setRating(resultSet.getInt(6));
				java.util.Date date = new java.util.Date(resultSet.getDate(7).getTime());
				movie.setAdditionDate(date);
				int movieID = resultSet.getInt(8);
				String locale = resultSet.getString(9);
				List<Actor> actors = actorDAO.readActorsFromOneMovie(movieID, locale);
				movie.setActors(actors);
				movies.add(movie);
			}
		} catch (InterruptedException e) {
			throw new DAOException("Problem with connection to DB", e);
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return movies;
	}

	@Override
	public Movie readMovieById(int id) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Movie movie = new Movie();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_MOVIE_BY_ID_QUERY);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			ActorDAO actorDAO = factory.getActorDAO();
			while (resultSet.next()) {
				int movieID = resultSet.getInt(1);
				String locale = resultSet.getString(2);
				movie.setTitle(resultSet.getString(3));
				movie.setDirector(resultSet.getString(4));
				movie.setGenre(resultSet.getString(5));
				movie.setYear(resultSet.getInt(7));
				movie.setLength(resultSet.getInt(8));
				movie.setRating(resultSet.getDouble(9));
				java.util.Date date = new java.util.Date(resultSet.getDate(10).getTime());
				movie.setAdditionDate(date);
				List<Actor> actors = actorDAO.readActorsFromOneMovie(movieID, locale);
				movie.setActors(actors);
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return movie;
	}

	@Override
	public List<Movie> readRandomMovies(int amount, String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Movie> movies = new ArrayList<Movie>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_RANDOM_MOVIES_QUERY);
			statement.setString(1, locale);
			statement.setInt(2, amount);
			resultSet = statement.executeQuery();
			DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
			ActorDAO actorDAO = factory.getActorDAO();
			while (resultSet.next()) {
				Movie movie = new Movie();
				int movieID = resultSet.getInt(1);
				movie.setTitle(resultSet.getString(2));
				movie.setDirector(resultSet.getString(3));
				movie.setGenre(resultSet.getString(4));
				movie.setYear(resultSet.getInt(5));
				movie.setLength(resultSet.getInt(6));
				movie.setRating(resultSet.getDouble(7));
				java.util.Date date =  new java.util.Date(resultSet.getDate(8).getTime());
				movie.setAdditionDate(date);
				List<Actor> actors =  actorDAO.readActorsFromOneMovie(movieID, locale);
				movie.setActors(actors);
				movies.add(movie);
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return movies;
	}

	@Override
	public boolean updateMovieTitle(Movie movie, String newTitle) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_MOVIE_TITLE_QUERY);
			statement.setString(1, newTitle);
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.executeUpdate();
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateMovieLength(Movie movie, int newLength) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_MOVIE_LENGTH_QUERY);
			statement.setInt(1, newLength);
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.executeUpdate();
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateMovieyear(Movie movie, int newYear) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_MOVIE_YEAR_QUERY);
			statement.setInt(1, newYear);
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.executeUpdate();
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateMovieDirector(Movie movie, String newDirector) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_MOVIE_DIRECTOR_QUERY);
			statement.setString(1, newDirector);
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.executeUpdate();
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateMovieGenre(Movie movie, String newGenre) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_MOVIE_GENRE_QUERY);
			statement.setString(1, newGenre);
			statement.setString(2, movie.getTitle());
			statement.setString(3, movie.getDirector());
			statement.executeUpdate();
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean updateMovieMark(Movie movie, int newMark, int amountDifference) throws DAOException {
		Connection connection = null;
		PreparedStatement statementSelect = null;
		PreparedStatement statementInsert = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statementSelect = connection.prepareStatement(SELECT_RATING_QUERY);
			statementSelect.setString(1, movie.getTitle());
			statementSelect.setString(2, movie.getDirector());
			resultSet = statementSelect.executeQuery();
			double rating = 0;
			int amount = 0;
			int movieID = 0;
			while (resultSet.next()) {
				movieID = resultSet.getInt(1);
				rating = resultSet.getDouble(2);
				amount = resultSet.getInt(3);
			}
			double newRating = (rating*amount + newMark)/(amount + amountDifference);
			statementInsert = connection.prepareStatement(UPDATE_MOVIE_RATING_QUERY);
			statementInsert.setDouble(1, newRating);
			statementInsert.setInt(2, movieID);
			if (movieID != 0) {
				rowCount = statementInsert.executeUpdate();
			} else {
				return false;
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
			if (statementSelect != null) {
				try {
					statementSelect.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statementInsert != null) {
				try {
					statementInsert.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		if (rowCount != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteMovie(Movie movie) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_MOVIE_QUERY);
			statement.setString(1, movie.getTitle());
			statement.setString(2, movie.getDirector());
			rowCount = statement.executeUpdate();
			if (rowCount != 0) {
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);				
			}
		}
		return false;
	}

	
}
