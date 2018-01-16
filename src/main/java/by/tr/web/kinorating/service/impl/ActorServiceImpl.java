package by.tr.web.kinorating.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.ActorDAO;
import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.ActorService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.ActorValidator;
import by.tr.web.kinorating.service.validation.CommonValidator;
import by.tr.web.kinorating.service.validation.MovieValidator;

public class ActorServiceImpl implements ActorService {

	private static final String PROBLEM_WITH_ADDITION_ACTOR_TRANSLATION = "Problem with addition actor data translation";
	private static final String PROBLEM_WITH_DELETING_ACTOR = "Problem with deleting actor";
	private static final String PROBLEM_WITH_EDITING_ACTOR_DATA = "Problem with editing actor data";
	private static final String PROBLEM_WITH_READING_ACTORS = "Problem with reading actors";
	private static final String PROBLEM_WITH_ADDING_ACTOR = "Problem with adding actor";

	private static final Logger logger = LogManager.getLogger(ActorServiceImpl.class);

	@Override
	public boolean addActor(Actor actor, int movieID, String langName) throws ServiceException {
		if (!ActorValidator.validateActor(actor) || !MovieValidator.validateMovieId(movieID)) {
			return false;
		}
		if (!CommonValidator.validateLanguageName(langName)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean added = false;
		try {
			added = actorDAO.createActor(actor, movie, langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_ADDING_ACTOR, e);
			throw new ServiceException(PROBLEM_WITH_ADDING_ACTOR, e);
		}
		return added;
	}

	@Override
	public boolean addTranslation(Actor translation, String langName) throws ServiceException {
		if (!ActorValidator.validateActorTranslation(translation)) {
			return false;
		}
		if (!CommonValidator.validateLanguageName(langName)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean added = false;
		try {
			added = actorDAO.addTranslation(translation, langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_ADDITION_ACTOR_TRANSLATION, e);
			throw new ServiceException(PROBLEM_WITH_ADDITION_ACTOR_TRANSLATION, e);
		}
		return added;
	}

	@Override
	public List<Actor> getActorByName(String firstName, String secondName) throws ServiceException {
		if (!ActorValidator.validateActorFirstName(firstName) || !ActorValidator.validateActorSecondName(secondName)) {
			return Collections.emptyList();
		}
		List<Actor> actors = null;
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		try {
			actors = actorDAO.readActorByName(firstName, secondName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_ACTORS, e);
			throw new ServiceException(PROBLEM_WITH_READING_ACTORS, e);
		}
		return actors;
	}

	@Override
	public List<Actor> getAllActors(String langName) throws ServiceException {
		if (!CommonValidator.validateLanguageName(langName)) {
			return Collections.emptyList();
		}
		List<Actor> actors = null;
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		try {
			actors = actorDAO.readAllActors(langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_ACTORS, e);
			throw new ServiceException(PROBLEM_WITH_READING_ACTORS, e);
		}
		return actors;
	}

	@Override
	public boolean editFirstName(int actorID, String newName) throws ServiceException {
		if (!ActorValidator.validateActorID(actorID) || !ActorValidator.validateActorFirstName(newName)) {
			return false;
		}
		Actor actor = new Actor();
		actor.setId(actorID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean updated = false;
		try {
			updated = actorDAO.updateActorFirstName(actor, newName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
			throw new ServiceException(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editSecondName(int actorID, String newName) throws ServiceException {
		if (!ActorValidator.validateActorID(actorID) || !ActorValidator.validateActorSecondName(newName)) {
			return false;
		}
		Actor actor = new Actor();
		actor.setId(actorID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean updated = false;
		try {
			updated = actorDAO.updateActorFirstName(actor, newName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
			throw new ServiceException(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editAge(int actorID, int age) throws ServiceException {
		if (!ActorValidator.validateActorID(actorID) || !ActorValidator.validateActorAge(age)) {
			return false;
		}
		Actor actor = new Actor();
		actor.setId(actorID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean updated = false;
		try {
			updated = actorDAO.updateActorAge(actor, age);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
			throw new ServiceException(PROBLEM_WITH_EDITING_ACTOR_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean deleteActor(int actorID) throws ServiceException {
		if (!ActorValidator.validateActorID(actorID)) {
			return false;
		}
		Actor actor = new Actor();
		actor.setId(actorID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ActorDAO actorDAO = factory.getActorDAO();
		boolean deleted = false;
		try {
			deleted = actorDAO.deleteActor(actor);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_DELETING_ACTOR, e);
			throw new ServiceException(PROBLEM_WITH_DELETING_ACTOR, e);
		}
		return deleted;
	}
}
