package by.tr.web.task_3_1.dao.mysql.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.tr.web.task_3_1.dao.MovieDAO;
import by.tr.web.task_3_1.dao.connection_pool.ConnectionPool;
import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.domain.Actor;
import by.tr.web.task_3_1.domain.Movie;

public class MySQLMovieDAOImpl implements MovieDAO{

	private static final String SELECT_ACTORS_ONE_MOVIE = "SELECT act_first_name, act_second_name, act_age FROM actors JOIN actors_translate ON actors.act_id=actors_translate.act_id WHERE mov_id=? AND lang_short_name=?";
	private static final String SELECT_ALL_MOVIES_ONE_TRANSLATION = "SELECT movies_translate.mov_id, mov_title, mov_director, mov_genre, mov_release_year, mov_length, mov_rating FROM movies_translate JOIN movies ON movies_translate.mov_id=movies.mov_id WHERE lang_short_name=?";
	private static final String CREATE_MOVIE_TRANSLATE_QUERY = "INSERT INTO movies_translate (mov_id, lang_short_name, mov_title, mov_director, mov_genre) VALUES (?, ?, ?, ?, ?)";
	private static final String CREATE_MOVIE_QUERY = "INSERT INTO movies (mov_release_year, mov_length, mov_rating, mov_addition_date) VALUES (?, ?, ?, ?)";
	private static final String EN_LOCALE = "en";
	private static final String RU_LOCALE = "ru";
	private static final String LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";

	@Override
	public boolean createMovie(Map<String, Movie> movie) throws DAOException {
		Movie movieRu = movie.get(RU_LOCALE);
		Movie movieEn = movie.get(EN_LOCALE);
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
			statement.setInt(1, movieEn.getYear());
			statement.setInt(2, movieEn.getLength());
			Date date = new Date(movieEn.getAdditionDate().getTime());
			statement.setDate(3, date);
			rowCount = statement.executeUpdate();
			if (rowCount == 0) {
				return false;
			}
			lastID = connection.createStatement();
			resultSet =	lastID.executeQuery(LAST_INSERT_ID);
			if (resultSet.next()) {
				movieID = resultSet.getInt(1);
			}
			if (movieID != 0) {
				statementTranslate = connection.prepareStatement(CREATE_MOVIE_TRANSLATE_QUERY);
				statementTranslate.setInt(1, movieID);
				statementTranslate.setString(2, EN_LOCALE);
				statementTranslate.setString(3, movieEn.getTitle());
				statementTranslate.setString(4, movieEn.getDirector());
				statementTranslate.setString(5, movieEn.getGenre());
				rowCount = statementTranslate.executeUpdate();
				if (rowCount == 0) {
					return false;
				}
				statementTranslate.setInt(1, movieID);
				statementTranslate.setString(2, RU_LOCALE);
				statementTranslate.setString(3, movieRu.getTitle());
				statementTranslate.setString(4, movieRu.getDirector());
				statementTranslate.setString(5, movieRu.getGenre());
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
		PreparedStatement statementMovie = null;
		PreparedStatement statementActor = null;
		ResultSet resultSetMovies = null;
		ResultSet resultSetActors = null;
		List<Movie> movies = new ArrayList<Movie>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statementMovie = connection.prepareStatement(SELECT_ALL_MOVIES_ONE_TRANSLATION);
			statementMovie.setString(1, locale);
			resultSetMovies = statementMovie.executeQuery();
			statementActor = connection.prepareStatement(SELECT_ACTORS_ONE_MOVIE);
			while (resultSetMovies.next()) {
				Movie movie = new Movie();
				int movieID = resultSetMovies.getInt(1);
				movie.setTitle(resultSetMovies.getString(2));
				movie.setDirector(resultSetMovies.getString(3));
				movie.setGenre(resultSetMovies.getString(4));
				movie.setYear(resultSetMovies.getInt(5));
				movie.setLength(resultSetMovies.getInt(6));
				movie.setMark(resultSetMovies.getDouble(7));
				statementActor.setInt(1, movieID);
				statementActor.setString(2, locale);
				resultSetActors = statementActor.executeQuery();
				List<Actor> actors =  new ArrayList<>();
				while (resultSetActors.next()) {
					Actor actor = new Actor();
					actor.setFirstName(resultSetActors.getString(1));
					actor.setSecondName(resultSetActors.getString(2));
					actor.setAge(resultSetActors.getInt(3));
					actors.add(actor);
				}
				movie.setActors(actors);
				movies.add(movie);
			}
		} catch (InterruptedException e) {
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			if (resultSetActors != null) {
				try {
					resultSetActors.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (resultSetMovies != null) {
				try {
					resultSetMovies.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statementActor != null) {
				try {
					statementActor.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statementMovie != null) {
				try {
					statementMovie.close();
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
	public List<Movie> readMovieByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Movie> readRandomMovies(int amount, String locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateMovieTitle(Movie movie, String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMovieLength(Movie movie, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMovieyear(Movie movie, int year) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMovieDirector(Movie movie, String director) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMovieGenre(Movie movie, String genre) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMovieMark(Movie movie, double mark) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMovie(Movie movie) {
		// TODO Auto-generated method stub
		
	}

	
}
