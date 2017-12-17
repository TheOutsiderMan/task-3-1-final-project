package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.domain.Movie;

public interface ActorDAO {
	
	boolean createActor(Actor actor, Movie movie, String locale) throws DAOException;

	List<Actor> readActor(String firstName, String secondName) throws DAOException;
	
	List<Actor> readActorsFromOneMovie(int movieID, String locale) throws DAOException;
	
	boolean isExistedActor(Actor actor) throws DAOException;

	boolean updateActorName(Actor actor, String firstName, String secondName) throws DAOException;
	
	boolean updateActorFirstName(Actor actor, String firstName);
	
	boolean updateActorSecondName(Actor actor, String secondName);

	boolean updateActorAge(Actor actor, int age);
	
	boolean deleteActor(Actor actor);
}
