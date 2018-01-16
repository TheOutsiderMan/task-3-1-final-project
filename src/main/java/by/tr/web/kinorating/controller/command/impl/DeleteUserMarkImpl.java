package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.ServiceFactory;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;

public class DeleteUserMarkImpl implements Command{
	
	private static final String SESSION_ATTR_USER = "user";

	private static final String PROBLEM_WITH_DELETING_USER_MARK_TO_MOVIE = "Problem with deleting user's mark to movie";
	
	private static final Logger logger = LogManager.getLogger(DeleteUserMarkImpl.class);
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String login = request.getParameter(ParameterName.LOGIN);
		String movieStr = request.getParameter(ParameterName.MOVIE_ID);
		String redirectLocation = request.getParameter(ParameterName.PAGE);
		int movieID = -1;
		if (movieStr != null) {
			movieID = Integer.valueOf(movieStr);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();
		boolean deleted = false;
		try {
			deleted = userService.deleteUserMarkToMovie(login, movieID);
		} catch (ServiceException e) {
			logger.error(PROBLEM_WITH_DELETING_USER_MARK_TO_MOVIE, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		if (deleted) {
			User user = (User) request.getSession().getAttribute(SESSION_ATTR_USER);
			user.getMarks().remove(movieID);
			request.getSession().setAttribute(SESSION_ATTR_USER, user);
			response.sendRedirect(redirectLocation);
		}
	}
	
}
