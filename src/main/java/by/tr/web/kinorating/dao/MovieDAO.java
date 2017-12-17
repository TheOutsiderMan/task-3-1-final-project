package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Movie;

public interface MovieDAO {
	
	boolean createMovie(Movie movie, String locale) throws DAOException;

	List<Movie> readAllMovies(String locale) throws DAOException;

	List<Movie> readMovieByTitle(String title) throws DAOException;
	
	Movie readMovieById(int id) throws DAOException;

	List<Movie> readRandomMovies(int amount, String locale) throws DAOException;

	boolean updateMovieTitle(Movie movie, String newTitle) throws DAOException;

	boolean updateMovieLength(Movie movie, int newLength) throws DAOException;

	boolean updateMovieyear(Movie movie, int newYear) throws DAOException;

	boolean updateMovieDirector(Movie movie, String newDirector) throws DAOException;

	boolean updateMovieGenre(Movie movie, String newGenre) throws DAOException;

	boolean updateMovieMark(Movie movie, int newMark, int amountDifference) throws DAOException;

	boolean deleteMovie(Movie movie) throws DAOException;

}
