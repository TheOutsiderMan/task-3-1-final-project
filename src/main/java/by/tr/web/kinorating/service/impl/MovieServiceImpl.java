package by.tr.web.kinorating.service.impl;

import java.util.ArrayList;
import java.util.List;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.MovieService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.Validator;

public class MovieServiceImpl implements MovieService {
	
	@Override
	public boolean addMovie(Movie movie, String langName) throws ServiceException {
		if (!Validator.validateMovie(movie)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean added = false;
		try {
			added = movieDAO.createMovie(movie, langName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return false;
	}
	
	@Override
	public List<Movie> getAllMovies(String langName) throws ServiceException {
		List<Movie> movies;
		if (!Validator.validateLanguageName(langName)) {
			return new ArrayList<Movie>();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		try {
			movies = movieDAO.readAllMovies(langName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return movies;
	}

	@Override
	public List<Movie> getRandomMovies(int amount, String langName) throws ServiceException {
		List<Movie> movies;
		if (!Validator.validateLanguageName(langName)) {
			return new ArrayList<Movie>();
		}
		if (!Validator.validateAmountMovies(amount)) {
			return new ArrayList<Movie>();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		
		try {
			movies = movieDAO.readRandomMovies(amount, langName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return movies;
	}
	
	@Override
	public boolean editMovieTitle(Movie movie, String title) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editMovieDirector(Movie movie, String director) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editMovieGenre(Movie movie, String genre) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editMovieLength(Movie movie, int length) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editMovieReleaseYear(Movie movie, int year) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean deleteMovie(String title, String director) {
		// TODO Auto-generated method stub
		return false;
	}

}
