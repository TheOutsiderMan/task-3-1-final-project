package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.ActorDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;

public class MySQLActorDAOImpl implements ActorDAO {

	private static final String INSERT_ACTOR_TRANSLATION_QUERY = "INSERT INTO actors_translate (act_first_name, act_second_name) VALUES (?, ?) WHERE act_id=?";
	private static final String INSERT_ACTOR_NAME_QUERY = "INSERT INTO actors_translate (act_id, lang_short_name, act_first_name, act_second_name) VALUES (?, ?, ?, ?)";
	private static final String CREATE_ACTOR_AGE_QUERY = "INSERT INTO actors (mov_id, act_age) VALUES(?, ?)";
	private static final String SELECT_ACTOR_BY_ID = "SELECT actors_translate.act_id, act_first_name, act_second_name, act_age FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE lang_short_name=? AND actors_translate.act_id=?";
	private static final String SELECT_ACTORS_QUERY = "SELECT actors_translate.act_id, act_first_name, act_second_name, act_age FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE lang_short_name=?";
	private static final String SELECT_ACTORS_ONE_MOVIE = "SELECT actors_translate.act_id, act_first_name, act_second_name, act_age FROM actors JOIN actors_translate ON actors.act_id=actors_translate.act_id WHERE mov_id=? AND lang_short_name=?";
	private static final String SELECT_ACTOR_QUERY = "SELECT * FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE actors_translate.act_id=?";
	private static final String SELECT_ACTOR_BY_NAME_QUERY = "SELECT * FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE act_first_name=? OR act_second_name=?";
	private static final String DELETE_ACTOR_QUERY = "DELETE FROM actors WHERE act_id=?";
	private static final String UPDATE_ACTOR_AGE_QUERY = "UPDATE actors SET act_age=? WHERE act_id=?";
	private static final String UPDATE_ACTOR_SECOND_NAME_QUERY = "UPDATE actors_translate SET act_second_name=? WHERE act_id=?";
	private static final String UPDATE_ACTOR_FIRST_NAME_QUERY = "UPDATE actors_translate SET act_first_name=? WHERE act_id=?";
	private static final String UPDATE_ACTOR_NAME_QUERY = "UPDATE actors_translate SET act_first_name=?, act_second_name=? WHERE act_id=?";
	private static final String LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
	
	private static final String PROBLEM_WITH_READING_FROM_DB = "Problem with reading from DB";
	private static final String PROBLEM_WITH_CONNECTION_POOL = "Problem with connection pool";
	private static final String PROBLEM_TO_CLOSE_RESOURCE = "Problem to close resource";
	private static final String PROBLEM_WITH_EDITING_DB = "Problem with editing DB";

	private static final Logger logger = LogManager.getLogger(MySQLActorDAOImpl.class);

	@Override
	public boolean createActor(Actor actor, Movie movie, String locale) throws DAOException {
		if (isExistedActor(actor)) {
			return false;
		}
		Connection connection = null;
		PreparedStatement statementCreate = null;
		PreparedStatement statementTranslate = null;
		Statement lastID = null;
		ResultSet resultSet = null;
		int actorId = 0;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statementCreate = connection.prepareStatement(CREATE_ACTOR_AGE_QUERY);
			statementCreate.setInt(1, movie.getId());
			statementCreate.setInt(2, actor.getAge());
			statementCreate.executeUpdate();
			lastID = connection.createStatement();
			resultSet = lastID.executeQuery(LAST_INSERT_ID);
			if (resultSet.next()) {
				actorId = resultSet.getInt(1);
			}
			if (actorId != 0) {
				statementTranslate = connection.prepareStatement(INSERT_ACTOR_NAME_QUERY);
				statementTranslate.setInt(1, actorId);
				statementTranslate.setString(2, locale);
				statementTranslate.setString(3, actor.getFirstName());
				statementTranslate.setString(4, actor.getSecondName());
				rowCount = statementTranslate.executeUpdate();
			} 
		} catch (InterruptedException e) {
			logger.error(PROBLEM_WITH_CONNECTION_POOL, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_POOL, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_EDITING_DB, e);
			throw new DAOException(PROBLEM_WITH_EDITING_DB, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (lastID != null) {
				try {
					lastID.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statementCreate != null) {
				try {
					statementCreate.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
				}
			}
			if (statementTranslate != null) {
				try {
					statementCreate.close();
				} catch (SQLException e) {
					logger.error(PROBLEM_TO_CLOSE_RESOURCE, e);
					throw new DAOException(PROBLEM_TO_CLOSE_RESOURCE, e);
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
	public boolean addTranslation(Actor translation, String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(INSERT_ACTOR_TRANSLATION_QUERY);
			statement.setString(1, translation.getFirstName());
			statement.setString(2, translation.getSecondName());
			statement.setInt(3, translation.getId());
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
	public Actor readActorById(int id, String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Actor actor = new Actor();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ACTOR_BY_ID);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				actor.setId(resultSet.getInt(1));
				actor.setFirstName(resultSet.getString(2));
				actor.setSecondName(resultSet.getString(3));
				actor.setAge(resultSet.getInt(4));
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
		return actor;
	}

	@Override
	public List<Actor> readActorByName(String firstName, String secondName) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Actor> actors = new ArrayList<Actor>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ACTOR_BY_NAME_QUERY);
			statement.setString(1, firstName);
			statement.setString(2, secondName);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Actor actor = new Actor();
				actor.setId(resultSet.getInt(1));
				actor.setFirstName(resultSet.getString(3));
				actor.setSecondName(resultSet.getString(4));
				actor.setAge(resultSet.getInt(7));
				actors.add(actor);
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
		return actors;
	}

	@Override
	public List<Actor> readAllActors(String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Actor> actors = new ArrayList<Actor>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ACTORS_QUERY);
			statement.setString(1, locale);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Actor actor = new Actor();
				actor.setId(resultSet.getInt(1));
				actor.setFirstName(resultSet.getString(2));
				actor.setSecondName(resultSet.getString(3));
				actor.setAge(resultSet.getInt(4));
				actors.add(actor);
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
		return actors;
	}

	@Override
	public List<Actor> readActorsFromOneMovie(int movieID, String locale) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Actor> actors = new ArrayList<Actor>();
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ACTORS_ONE_MOVIE);
			statement.setInt(1, movieID);
			statement.setString(2, locale);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Actor actor = new Actor();
				actor.setId(resultSet.getInt(1));
				actor.setFirstName(resultSet.getString(2));
				actor.setSecondName(resultSet.getString(3));
				actor.setAge(resultSet.getInt(4));
				actors.add(actor);
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
		return actors;
	}

	@Override
	public boolean isExistedActor(Actor actor) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(SELECT_ACTOR_QUERY);
			statement.setString(1, actor.getFirstName());
			statement.setString(2, actor.getSecondName());
			statement.setInt(3, actor.getAge());
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
	public boolean updateActorName(Actor actor, String firstName, String secondName) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_ACTOR_NAME_QUERY);
			statement.setString(1, firstName);
			statement.setString(2, secondName);
			statement.setInt(3, actor.getId());
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
	public boolean updateActorFirstName(Actor actor, String firstName) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_ACTOR_FIRST_NAME_QUERY);
			statement.setString(1, firstName);
			statement.setInt(2, actor.getId());
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
	public boolean updateActorSecondName(Actor actor, String secondName) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_ACTOR_SECOND_NAME_QUERY);
			statement.setString(1, secondName);
			statement.setInt(2, actor.getId());
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
	public boolean updateActorAge(Actor actor, int age) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(UPDATE_ACTOR_AGE_QUERY);
			statement.setInt(1, age);
			statement.setInt(2, actor.getId());
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
	public boolean deleteActor(Actor actor) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement(DELETE_ACTOR_QUERY);
			statement.setInt(1, actor.getId());
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
