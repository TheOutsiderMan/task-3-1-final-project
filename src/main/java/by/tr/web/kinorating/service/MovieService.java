package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface MovieService {

	boolean addMovie(Movie newMovie, String langName) throws ServiceException;
	
	boolean addTranslation(Movie translation, String langName) throws ServiceException;
	
	List<Movie> getAllMovies(String langName) throws ServiceException;

	List<Movie> getRandomMovies(int amount, String langName) throws ServiceException;
	
	boolean editMovieTitle(int movieID, String title) throws ServiceException;
	
	boolean editMovieDirector(int movieID, String director) throws ServiceException;
	
	boolean editMovieGenre(int movieID, String genre) throws ServiceException;
	
	boolean editMovieLength(int movieID, int length) throws ServiceException;
	
	boolean editMovieReleaseYear(int movieID, int year) throws ServiceException;
	
	boolean deleteMovie(int movieID) throws ServiceException;
}
