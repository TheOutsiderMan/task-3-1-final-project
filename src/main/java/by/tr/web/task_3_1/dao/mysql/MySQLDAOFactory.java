package by.tr.web.task_3_1.dao.mysql;

import by.tr.web.task_3_1.dao.ActorDAO;
import by.tr.web.task_3_1.dao.DAOAbstractFactory;
import by.tr.web.task_3_1.dao.MovieDAO;
import by.tr.web.task_3_1.dao.ReviewDAO;
import by.tr.web.task_3_1.dao.UserDAO;
import by.tr.web.task_3_1.dao.mysql.impl.MySQLActorDAOImpl;
import by.tr.web.task_3_1.dao.mysql.impl.MySQLMovieDAOImpl;
import by.tr.web.task_3_1.dao.mysql.impl.MySQLReviewDAOImpl;
import by.tr.web.task_3_1.dao.mysql.impl.MySQLUserDAOImpl;

public class MySQLDAOFactory implements DAOAbstractFactory {
	private static final MySQLDAOFactory factory = new MySQLDAOFactory();
	private final UserDAO userDAO = new MySQLUserDAOImpl();
	private final MovieDAO movieDAO = new MySQLMovieDAOImpl();
	private final ReviewDAO reviewDAO = new MySQLReviewDAOImpl();
	private final ActorDAO actorDAO = new MySQLActorDAOImpl();

	public static MySQLDAOFactory getInstance() {
		return factory;
	}

	@Override
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Override
	public MovieDAO getMovieDAO() {
		return movieDAO;
	}

	@Override
	public ReviewDAO getReviewDAO() {
		return reviewDAO;
	}

	@Override
	public ActorDAO getActorDAO() {
		return actorDAO;
	}

}
