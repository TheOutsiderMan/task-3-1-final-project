package by.tr.web.task_3_1.dao;

import java.util.List;
import java.util.Map;

import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.domain.Movie;

public interface MovieDAO {

	boolean createMovie(Map<String, Movie> movie) throws DAOException;

	List<Movie> readAllMovies(String locale) throws DAOException;

	List<Movie> readMovieByTitle(String title);

	List<Movie> readRandomMovies(int amount, String locale);

	void updateMovieTitle(Movie movie, String title);

	void updateMovieLength(Movie movie, int length);

	void updateMovieyear(Movie movie, int year);

	void updateMovieDirector(Movie movie, String director);

	void updateMovieGenre(Movie movie, String genre);

	void updateMovieMark(Movie movie, double mark);

	void deleteMovie(Movie movie);

}
