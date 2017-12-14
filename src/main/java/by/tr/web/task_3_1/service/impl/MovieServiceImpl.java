package by.tr.web.task_3_1.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;

import by.tr.web.task_3_1.dao.DAOAbstractFactory;
import by.tr.web.task_3_1.dao.MovieDAO;
import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.dao.mysql.MySQLDAOFactory;
import by.tr.web.task_3_1.domain.Movie;
import by.tr.web.task_3_1.service.MovieService;
import by.tr.web.task_3_1.service.exception.ServiceException;

public class MovieServiceImpl implements MovieService {

	@Override
	public List<Movie> getAllMovies(String langName) throws ServiceException {
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		List<Movie> movies;
		try {
			movies = movieDAO.readAllMovies(langName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return movies;
	}

	@Override
	public List<Movie> getRandomMovies(int amount, String langName) throws ServiceException {
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		List<Movie> movies;
		try {
			movies = movieDAO.readRandomMovies(amount, langName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return movies;
	}

	@Override
	public boolean deleteMovie(String title, String director) {
		// TODO Auto-generated method stub
		return false;
	}

}
