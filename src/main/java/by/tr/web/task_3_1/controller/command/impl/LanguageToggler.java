package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.command.Command;

public class LanguageToggler implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		
		String lang = request.getParameter("local");
		
		request.getSession(true).setAttribute("local", lang);
		response.sendRedirect(request.getRequestURI());
	}

}
