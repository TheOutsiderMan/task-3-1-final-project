package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.command.Command;

public class LanguageToggler implements Command {

	private static final String REGEX_REPLACE_OUT = "";
	private static final String REGEX_REPLACE_IN = ".+/";
	private static final String URL_PARAM = "url";
	private static final String LOCALE = "locale";
	private static final String UTF_8 = "utf-8";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding(UTF_8);
		
		String lang = request.getParameter(LOCALE);
		String url = request.getParameter(URL_PARAM);
		String forwardUrl = url.replaceFirst(REGEX_REPLACE_IN, REGEX_REPLACE_OUT);
		
		request.getSession(true).setAttribute(LOCALE, lang);
		request.getRequestDispatcher(forwardUrl).forward(request, response);
	}

}
