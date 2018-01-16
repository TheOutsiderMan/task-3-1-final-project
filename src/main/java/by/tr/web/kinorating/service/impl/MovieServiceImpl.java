package by.tr.web.kinorating.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.MovieDAO;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.MovieService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.MovieValidator;
import by.tr.web.kinorating.service.validation.CommonValidator;

public class MovieServiceImpl implements MovieService {

	private static final String PROBLEM_WITH_READING_MOVIES = "Problem with reading movies from DB";
	private static final String PROBLEM_WITH_DELETING_THE_MOVIE = "Problem with deleting the movie";
	private static final String PROBLEM_WITH_ADDING_NEW_MOVIE = "Problem with adding new movie";
	private static final String PROBLEM_WITH_UPDATING_MOVIE_DATA = "Problem with updating movie data";
	private static final String PROBLEM_WITH_ADDITION_MOVIE_TRANSLATION = "Problem with addition movie translation";
	
	private static final Logger logger = LogManager.getLogger(ReviewServiceImpl.class);

	@Override
	public boolean addMovie(Movie newMovie, String langName) throws ServiceException {
		if (!MovieValidator.validateNewMovie(newMovie)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean added = false;
		try {
			added = movieDAO.createMovie(newMovie, langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_ADDING_NEW_MOVIE, e);
			throw new ServiceException(PROBLEM_WITH_ADDING_NEW_MOVIE, e);
		}
		return added;
	}

	@Override
	public boolean addTranslation(Movie translation, String langName) throws ServiceException {
		if (!MovieValidator.validateMovieTranslation(translation) || !CommonValidator.validateLanguageName(langName)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean added = false;
		try {
			added = movieDAO.addTranslation(translation, langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_ADDITION_MOVIE_TRANSLATION, e);
			throw new ServiceException(PROBLEM_WITH_ADDITION_MOVIE_TRANSLATION, e);
		}
		return added;
	}

	@Override
	public List<Movie> getAllMovies(String langName) throws ServiceException {
		List<Movie> movies;
		if (!CommonValidator.validateLanguageName(langName)) {
			return Collections.emptyList();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		try {
			movies = movieDAO.readAllMovies(langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_MOVIES, e);
			throw new ServiceException(PROBLEM_WITH_READING_MOVIES, e);
		}
		return movies;
	}

	@Override
	public List<Movie> getRandomMovies(int amount, String langName) throws ServiceException {
		List<Movie> movies;
		if (!CommonValidator.validateLanguageName(langName)) {
			return Collections.emptyList();
		}
		if (!MovieValidator.validateAmountMovies(amount)) {
			return Collections.emptyList();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		try {
			movies = movieDAO.readRandomMovies(amount, langName);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_MOVIES, e);
			throw new ServiceException(PROBLEM_WITH_READING_MOVIES, e);
		}
		return movies;
	}

	@Override
	public boolean editMovieTitle(int movieID, String title) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID)) {
			return false;
		}
		if (!MovieValidator.validateMovieTitle(title)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean updated = false;
		try {
			updated = movieDAO.updateMovieTitle(movie, title);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editMovieDirector(int movieID, String director) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID) || !MovieValidator.validateMovieDirector(director)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean updated = false;
		try {
			updated = movieDAO.updateMovieDirector(movie, director);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editMovieGenre(int movieID, String genre) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID) || !MovieValidator.validateMovieGenre(genre)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean updated = false;
		try {
			updated = movieDAO.updateMovieGenre(movie, genre);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editMovieLength(int movieID, int length) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID) || !MovieValidator.validateMovieLength(length)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean updated = false;
		try {
			updated = movieDAO.updateMovieLength(movie, length);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean editMovieReleaseYear(int movieID, int year) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID) || !MovieValidator.validateMovieYear(year)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean updated = false;
		try {
			updated = movieDAO.updateMovieYear(movie, year);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
			throw new ServiceException(PROBLEM_WITH_UPDATING_MOVIE_DATA, e);
		}
		return updated;
	}

	@Override
	public boolean deleteMovie(int movieID) throws ServiceException {
		if (!MovieValidator.validateMovieId(movieID)) {
			return false;
		}
		Movie movie = new Movie();
		movie.setId(movieID);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		MovieDAO movieDAO = factory.getMovieDAO();
		boolean deleted = false;
		try {
			deleted = movieDAO.deleteMovie(movie);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_DELETING_THE_MOVIE, e);
			throw new ServiceException(PROBLEM_WITH_DELETING_THE_MOVIE, e);
		}
		return deleted;
	}

}
