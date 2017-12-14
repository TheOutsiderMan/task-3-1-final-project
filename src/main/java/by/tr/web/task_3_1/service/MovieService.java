package by.tr.web.task_3_1.service;

import java.util.List;

import by.tr.web.task_3_1.domain.Movie;
import by.tr.web.task_3_1.service.exception.ServiceException;

public interface MovieService {

	List<Movie> getAllMovies(String langName) throws ServiceException;

	List<Movie> getRandomMovies(int amount, String langName) throws ServiceException;
	
	boolean deleteMovie(String title, String director);
}
