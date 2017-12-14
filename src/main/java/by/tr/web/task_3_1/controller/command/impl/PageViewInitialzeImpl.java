package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.domain.Movie;
import by.tr.web.task_3_1.service.MovieService;
import by.tr.web.task_3_1.service.ServiceFactory;
import by.tr.web.task_3_1.service.exception.ServiceException;

public class PageViewInitialzeImpl implements Command {

	private static final String DEFAULT_LANG_NAME = "ru";
	private static final String FORWARD_TO_MAIN_PAGE = "index.jsp";
	private static final String ITEM_ATTR = "item";
	private static final int ITEMS_FOR_MAIN_PAGE = 5;
	private static final String REVIEWS_PAGE = "reviews";
	private static final String MOVIES_PAGE = "movies";
	private static final String MAIN_PAGE = "main";
	

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String page = request.getParameter(ParameterName.PAGE);
		
		ServiceFactory factory = ServiceFactory.getInstance();
		Cookie[] cookies = request.getCookies();
		String langName = DEFAULT_LANG_NAME;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(ParameterName.LOCALE)) {
				langName = cookie.getValue();
			}
		}
		langName = langName.substring(0, 2);
		MovieService movieService = factory.getMoviesService();
		switch (page) {
		case MAIN_PAGE:
			List<Movie> randomMovies = null;
			try {
				randomMovies = movieService.getRandomMovies(ITEMS_FOR_MAIN_PAGE, langName);
			} catch (ServiceException e) {
				// log
				response.sendError(404);
				return;
			}
			request.setAttribute(ITEM_ATTR, randomMovies);
			request.getRequestDispatcher(FORWARD_TO_MAIN_PAGE).forward(request, response);
			break;
		case MOVIES_PAGE:
			List<Movie> movies;
			try {
				movies = movieService.getAllMovies(langName);
			} catch (ServiceException e) {
				// log
				response.sendError(404);
				return;
			}
			request.setAttribute(ITEM_ATTR, movies);
			request.getRequestDispatcher(MOVIES_PAGE).forward(request, response);
			break;
		case REVIEWS_PAGE:
			//
			break;	
		default:
			List<Movie> randomMoviesDef = null;
			try {
				randomMoviesDef = movieService.getRandomMovies(ITEMS_FOR_MAIN_PAGE, langName);
			} catch (ServiceException e) {
				// log
				response.sendError(404);
				return;
			}
			request.setAttribute(ITEM_ATTR, randomMoviesDef);
			request.getRequestDispatcher(FORWARD_TO_MAIN_PAGE).forward(request, response);
			break;
		}
	}

}
