package by.tr.web.task_3_1.dao;

import java.util.List;
import java.util.Map;

import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.domain.Actor;
import by.tr.web.task_3_1.domain.Movie;

public interface ActorDAO {
	
	void createActor(Map<String, Actor> actor, Movie movie) throws DAOException;

	List<Actor> readActor(String firstName, String secondName);
	
	List<Actor> readActorsFromOneMovie(int movieID, String locale) throws DAOException;
	
	boolean isExistedActor(Actor actor);

	void updateActorName(Actor actor, String firstName, String secondName);
	
	void updateActorFirstName(Actor actor, String firstName);
	
	void updateActorSecondName(Actor actor, String secondName);

	void updateActorAge(Actor actor, int age);
	
	void deleteActor(Actor actor);
}
