package by.tr.web.task_3_1.controller.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
	
	void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
