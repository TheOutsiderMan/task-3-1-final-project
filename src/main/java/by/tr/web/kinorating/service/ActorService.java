package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.Actor;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface ActorService {

	boolean addActor(Actor actor, int movieID, String langName) throws ServiceException;

	boolean addTranslation(Actor translation, String langName) throws ServiceException;
	
	List<Actor> getActorByName(String firstName, String secondName) throws ServiceException;

	List<Actor> getAllActors(String langName) throws ServiceException;

	boolean editFirstName(int actorID, String newName) throws ServiceException;

	boolean editSecondName(int actorID, String newName) throws ServiceException;

	boolean editAge(int actorID, int age) throws ServiceException;

	boolean deleteActor(int actorID) throws ServiceException;
}
