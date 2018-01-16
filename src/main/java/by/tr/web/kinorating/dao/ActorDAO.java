package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;

public interface ActorDAO {

	boolean createActor(Actor actor, Movie movie, String locale) throws DAOException;

	boolean addTranslation(Actor translation, String locale) throws DAOException;
	
	Actor readActorById(int id, String locale) throws DAOException;

	List<Actor> readActorByName(String firstName, String secondName) throws DAOException;
	
	List<Actor> readAllActors(String locale) throws DAOException;

	List<Actor> readActorsFromOneMovie(int movieID, String locale) throws DAOException;

	boolean isExistedActor(Actor actor) throws DAOException;

	boolean updateActorName(Actor actor, String firstName, String secondName) throws DAOException;

	boolean updateActorFirstName(Actor actor, String firstName) throws DAOException;

	boolean updateActorSecondName(Actor actor, String secondName) throws DAOException;

	boolean updateActorAge(Actor actor, int age) throws DAOException;

	boolean deleteActor(Actor actor) throws DAOException;
}
