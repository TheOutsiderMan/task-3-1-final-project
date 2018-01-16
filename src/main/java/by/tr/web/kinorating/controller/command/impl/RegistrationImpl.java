package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.service.ServiceFactory;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;

public class RegistrationImpl implements Command {
	
	private static final String PROBLEM_WITH_REGISTRATION = "Problem with registration";
	
	private static final String FORWARD_URL = "after-registration";
	private static final String ATTR_REGISTERED = "registered";
	private static final String INPUT_CHECKED = "on";
	
	private static final Logger logger = LogManager.getLogger(RegistrationImpl.class);
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter(ParameterName.LOGIN);
		String email = request.getParameter(ParameterName.EMAIL);
		String password = request.getParameter(ParameterName.PASSWORD);
		String passwordControl = request.getParameter(ParameterName.PASSWORD_CONTROL);
		String agreement = request.getParameter(ParameterName.AGREEMENT);
		
		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();
		Boolean registered = false;
		if (password.equals(passwordControl) && agreement.equals(INPUT_CHECKED)) {
			try {
				registered = userService.registerUser(login, email, password);
			} catch (ServiceException e) {
				logger.error(PROBLEM_WITH_REGISTRATION, e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}
		request.setAttribute(ATTR_REGISTERED, registered);
		request.getRequestDispatcher(FORWARD_URL).forward(request, response);
	}

}
