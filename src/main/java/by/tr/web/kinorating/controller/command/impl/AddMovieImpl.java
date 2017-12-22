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

public class AddMovieImpl implements Command{

	private static final String REDIRECT_URL = "app?action=init_view&page=movies";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = request.getParameter(ParameterName.TITLE);
		String director = request.getParameter(ParameterName.DIRECTOR);
		String genre = request.getParameter(ParameterName.GENRE);
		int year = Integer.valueOf(request.getParameter(ParameterName.YEAR));
		int length = Integer.valueOf(request.getParameter(ParameterName.LENGTH));
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
		try {
			movieService.addMovie(movie, langName);
		} catch (ServiceException e) {
			// log
			response.sendError(404);
			return;
		}
		response.sendRedirect(REDIRECT_URL);
	}

}
