package by.tr.web.kinorating.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.controller.command.CommandProvider;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.util.ConnectionPoolInitializer;

public class FrontController extends HttpServlet {
	
	public static final String UTF_8 = "utf-8";

	private static final long serialVersionUID = 1L;
      
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
			//log
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			ConnectionPoolInitializer.getInitializer().closeAllConnections();
		} catch (ServiceException e) {
			//log
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(UTF_8);
		
		String commandName = request.getParameter(ParameterName.ACTION);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException e) {
			response.sendError(404);
			//log
		} catch (InstantiationException e) {
			response.sendError(404);
			//log
		} catch (IllegalAccessException e) {
			response.sendError(404);
			//log
		} catch (SAXException e) {
			response.sendError(404);
			//log
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(UTF_8);
		
		String commandName = request.getParameter(ParameterName.ACTION);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException e) {
			response.sendError(404);
			//log
		} catch (InstantiationException e) {
			response.sendError(404);
			//log
		} catch (IllegalAccessException e) {
			response.sendError(404);
			//log
		} catch (SAXException e) {
			response.sendError(404);
			//log
		}
	}

}
