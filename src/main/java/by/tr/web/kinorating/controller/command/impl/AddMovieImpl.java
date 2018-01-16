package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.service.MovieService;
import by.tr.web.kinorating.service.ServiceFactory;
import by.tr.web.kinorating.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddMovieImpl implements Command {

	private static final String PROBLEM_WITH_ADDING_THE_MOVIE = "Problem with adding the movie";

	private static final Logger logger = LogManager.getLogger(AddMovieImpl.class);

	private static final String REDIRECT_URL = "app?action=init_view&page=movies";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = request.getParameter(ParameterName.TITLE);
		String director = request.getParameter(ParameterName.DIRECTOR);
		String genre = request.getParameter(ParameterName.GENRE);
		String yearParameter = request.getParameter(ParameterName.YEAR);
		String lengthParameter = request.getParameter(ParameterName.LENGTH);
		int year = 0;
		if (!yearParameter.isEmpty()) {
			year = Integer.valueOf(yearParameter);
		}
		int length = 0;
		if (!lengthParameter.isEmpty()) {
			length = Integer.valueOf(lengthParameter);
		}
		String langName = request.getParameter(ParameterName.LOCALE);

		Movie movie = new Movie();
		movie.setTitle(title);
		movie.setDirector(director);
		movie.setGenre(genre);
		movie.setYear(year);
		movie.setLength(length);
		movie.setAdditionDate(new Date());

		ServiceFactory factory = ServiceFactory.getInstance();
		MovieService movieService = factory.getMoviesService();
		boolean updated = false;
		try {
			updated = movieService.addMovie(movie, langName);
		} catch (ServiceException e) {
			logger.error(PROBLEM_WITH_ADDING_THE_MOVIE, e);
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		if (updated) {
			response.sendRedirect(REDIRECT_URL);
		} else {
			response.sendError(HttpServletResponse.SC_CONFLICT);
		}

	}

}
