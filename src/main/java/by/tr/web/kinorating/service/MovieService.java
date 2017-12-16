package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.exception.ServiceException;

public interface MovieService {

	List<Movie> getAllMovies(String langName) throws ServiceException;

	List<Movie> getRandomMovies(int amount, String langName) throws ServiceException;
	
	boolean deleteMovie(String title, String director);
}
