package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tr.web.kinorating.controller.command.Command;

public class LogOffImpl implements Command {
	
	private static final String USER_ATTR = "user";
	private static final String FORWARD_URL = "index.jsp";
	private static final String AUTHENTICATED = "authenticated";
	private static final String AUTHENTICATED_FALSE = "no";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
		session.removeAttribute(USER_ATTR);
		request.getRequestDispatcher(FORWARD_URL).forward(request, response);
	}

}
