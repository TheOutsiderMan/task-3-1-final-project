package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.service.ServiceFactory;
import by.tr.web.task_3_1.service.UserService;

public class RegistrationImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter(ParameterName.LOGIN);
		String email = request.getParameter(ParameterName.EMAIL);
		String password = request.getParameter(ParameterName.PASSWORD);
		String passwordControl = request.getParameter(ParameterName.PASSWORD_CONTROL);
		
		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();
		Boolean registered = false;
		if (password.equals(passwordControl)) {
			registered = userService.registerUser(login, email, password);
		}
		request.setAttribute("registered", registered);
		request.getRequestDispatcher("after-registration.jsp").forward(request, response);
	}

}
