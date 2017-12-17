package by.tr.web.kinorating.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.tr.web.kinorating.dao.ActorDAO;
import by.tr.web.kinorating.dao.connection_pool.ConnectionPool;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;

public class MySQLActorDAOImpl implements ActorDAO {

	private static final String SELECT_ACTOR_QUERY = "SELECT actors_translate.act_id FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE act_first_name=? AND act_second_name=? AND act_age=?";
	private static final String SELECT_ACTOR_BY_NAME_QUERY = "SELECT * FROM actors_translate JOIN actors ON actors_translate.act_id=actors.act_id WHERE act_first_name=? AND act_second_name=?";
	private static final String SELECT_ACTORS_ONE_MOVIE = "SELECT act_first_name, act_second_name, act_age FROM actors JOIN actors_translate ON actors.act_id=actors_translate.act_id WHERE mov_id=? AND lang_short_name=?";
	private static final String INSERT_ACTOR_NAME = "INSERT INTO actors_translate (act_id, lang_short_name, act_first_name, act_second_name) VALUES (?, ?, ?, ?)";
	private static final String LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
	private static final String CREATE_ACTOR_AGE = "INSERT INTO actors (mov_id, act_age) VALUES((SELECT mov_id FROM movies_translate WHERE mov_title=?), ?)";

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
			statementCreate = connection.prepareStatement(CREATE_ACTOR_AGE);
			statementCreate.setString(1, movie.getTitle());
			statementCreate.setInt(2, actor.getAge());
			statementCreate.executeUpdate();
			lastID = connection.createStatement();
			resultSet = lastID.executeQuery(LAST_INSERT_ID);
			if (resultSet.next()) {
				actorId = resultSet.getInt(1);
			}
			if (actorId != 0) {
				statementTranslate = connection.prepareStatement(INSERT_ACTOR_NAME);
				statementTranslate.setInt(1, actorId);
				statementTranslate.setString(2, locale);
				statementTranslate.setString(3, actor.getFirstName());
				statementTranslate.setString(4, actor.getSecondName());
				rowCount = statementTranslate.executeUpdate();
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
			if (statementCreate != null) {
				try {
					statementCreate.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
			if (statementTranslate != null) {
				try {
					statementCreate.close();
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
	public List<Actor> readActor(String firstName, String secondName) throws DAOException {
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
				actor.setFirstName(resultSet.getString(3));
				actor.setSecondName(resultSet.getString(4));
				actor.setAge(resultSet.getInt(7));
				actors.add(actor);
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
				actor.setFirstName(resultSet.getString(1));
				actor.setSecondName(resultSet.getString(2));
				actor.setAge(resultSet.getInt(3));
				actors.add(actor);
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
			if(resultSet.next()) {
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

	@Override
	public boolean updateActorName(Actor actor, String firstName, String secondName) throws DAOException {
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statement = connection.prepareStatement("UPDATE actors_translate SET act_first_name=?, act_second_name=? WHERE act_id=(SELECT act) ");
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
			if (connection != null) {
				ConnectionPool.getInstance().releaseConnection(connection);
			}
		}
		
		return false;

	}

	@Override
	public boolean updateActorFirstName(Actor actor, String firstName) {
		return false;
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateActorSecondName(Actor actor, String secondName) {
		return false;
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateActorAge(Actor actor, int age) {
		return false;
		// TODO Auto-generated method stub

	}

	@Override
	public boolean deleteActor(Actor actor) {
		return false;
		// TODO Auto-generated method stub

	}

}
