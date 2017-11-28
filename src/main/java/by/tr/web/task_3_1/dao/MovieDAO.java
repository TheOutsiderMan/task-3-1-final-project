package by.tr.web.task_3_1.dao;

import java.util.List;

import by.tr.web.task_3_1.domain.Movie;

public interface MovieDAO {
	
	void createMovie(Movie movie);
	List<Movie> readAllMovies();
	Movie readMovieByTitle(String title);
	void updateMovie(Movie movie);
	void deleteMovie(Movie movie);
	
}
