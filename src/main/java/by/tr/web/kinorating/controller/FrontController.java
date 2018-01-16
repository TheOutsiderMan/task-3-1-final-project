package by.tr.web.kinorating.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.controller.command.CommandProvider;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.util.ConnectionPoolInitializer;

public class FrontController extends HttpServlet {

	private static final String PROBLEM_WITH_INITIALISING_APPROPRIATE_COMMAND_CLASS = "Problem with initialising appropriate command class";
	private static final String PROBLEM_WITH_PARSING_COMMANDS = "Problem with parsing commands.xml";
	private static final String PROBLEM_WITH_CLOSING_CONNECTIONS_TO_DB = "Problem with closing connections to DB";
	private static final String PROBLEM_WITH_INITIALIZING_CONNECTION_POOL = "Problem with initializing connection pool";
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(FrontController.class);

	private CommandProvider provider = new CommandProvider();

	public FrontController() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			ConnectionPoolInitializer.getInitializer().initializeConnectionPool();
		} catch (ServiceException e) {
			logger.error(PROBLEM_WITH_INITIALIZING_CONNECTION_POOL, e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		try {
			ConnectionPoolInitializer.getInitializer().closeAllConnections();
		} catch (ServiceException e) {
			logger.error(PROBLEM_WITH_CLOSING_CONNECTIONS_TO_DB, e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String commandName = request.getParameter(ParameterName.ACTION);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.error(PROBLEM_WITH_INITIALISING_APPROPRIATE_COMMAND_CLASS, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (SAXException e) {
			logger.error(PROBLEM_WITH_PARSING_COMMANDS, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String commandName = request.getParameter(ParameterName.ACTION);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.error(PROBLEM_WITH_INITIALISING_APPROPRIATE_COMMAND_CLASS, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (SAXException e) {
			logger.error(PROBLEM_WITH_PARSING_COMMANDS, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
