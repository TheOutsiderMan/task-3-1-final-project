package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface MovieService {

	boolean addMovie(Movie movie, String langName) throws ServiceException;
	
	List<Movie> getAllMovies(String langName) throws ServiceException;

	List<Movie> getRandomMovies(int amount, String langName) throws ServiceException;
	
	boolean editMovieTitle(Movie movie, String title) throws ServiceException;
	
	boolean editMovieDirector(Movie movie, String director) throws ServiceException;
	
	boolean editMovieGenre(Movie movie, String genre) throws ServiceException;
	
	boolean editMovieLength(Movie movie, int length) throws ServiceException;
	
	boolean editMovieReleaseYear(Movie movie, int year) throws ServiceException;
	
	boolean deleteMovie(String title, String director) throws ServiceException;
}
