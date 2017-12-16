package by.tr.web.kinorating.dao;

public interface DAOAbstractFactory {

	UserDAO getUserDAO();

	MovieDAO getMovieDAO();

	ReviewDAO getReviewDAO();

	ActorDAO getActorDAO();
}
