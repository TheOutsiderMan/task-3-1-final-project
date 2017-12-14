package by.tr.web.task_3_1.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.tr.web.task_3_1.dao.ActorDAO;
import by.tr.web.task_3_1.dao.connection_pool.ConnectionPool;
import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.domain.Actor;
import by.tr.web.task_3_1.domain.Movie;

public class MySQLActorDAOImpl implements ActorDAO {

	private static final String SELECT_ACTORS_ONE_MOVIE = "SELECT act_first_name, act_second_name, act_age FROM actors JOIN actors_translate ON actors.act_id=actors_translate.act_id WHERE mov_id=? AND lang_short_name=?";
	private static final String INSERT_ACTOR_NAME = "INSERT INTO actors_translate (act_id, lang_short_name, act_first_name, act_second_name) VALUES (?, ?, ?, ?)";
	private static final String LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
	private static final String CREATE_ACTOR_AGE = "INSERT INTO actors (mov_id, act_age) VALUES((SELECT mov_id FROM movies_translate WHERE mov_title=?), ?)";
	private static final String EN_LOCALE = "en";
	private static final String RU_LOCALE = "ru";

	@Override
	public void createActor(Map<String, Actor> actor, Movie movie) throws DAOException {
		Actor actorRu = actor.get(RU_LOCALE);
		Actor actorEn = actor.get(EN_LOCALE);
		Connection connection = null;
		PreparedStatement statementCreate = null;
		PreparedStatement statementTranslate = null;
		Statement lastID = null;
		ResultSet resultSet = null;
		int actorId = 0;
		try {
			connection = ConnectionPool.getInstance().takeConnection();
			statementCreate = connection.prepareStatement(CREATE_ACTOR_AGE);
			statementCreate.setString(1, movie.getTitle());
			statementCreate.setInt(2, actorEn.getAge());
			statementCreate.executeUpdate();
			lastID = connection.createStatement();
			resultSet = lastID.executeQuery(LAST_INSERT_ID);
			if (resultSet.next()) {
				actorId = resultSet.getInt(1);
			}
			if (actorId != 0) {
				statementTranslate = connection.prepareStatement(INSERT_ACTOR_NAME);
				statementTranslate.setInt(1, actorId);
				statementTranslate.setString(2, EN_LOCALE);
				statementTranslate.setString(3, actorEn.getFirstName());
				statementTranslate.setString(4, actorEn.getSecondName());
				statementTranslate.executeUpdate();
				statementTranslate.setInt(1, actorId);
				statementTranslate.setString(2, RU_LOCALE);
				statementTranslate.setString(3, actorRu.getFirstName());
				statementTranslate.setString(4, actorRu.getSecondName());
				statementTranslate.executeUpdate();
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
	}

	@Override
	public List<Actor> readActor(String firstName, String secondName) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean isExistedActor(Actor actor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateActorName(Actor actor, String firstName, String secondName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateActorFirstName(Actor actor, String firstName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateActorSecondName(Actor actor, String secondName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateActorAge(Actor actor, int age) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteActor(Actor actor) {
		// TODO Auto-generated method stub

	}

}
