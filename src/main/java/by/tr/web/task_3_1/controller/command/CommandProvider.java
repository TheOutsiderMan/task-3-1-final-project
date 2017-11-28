package by.tr.web.task_3_1.controller.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import by.tr.web.task_3_1.controller.command.util.CommandInitializer;

public class CommandProvider {
	
	private Map<String, Command> commands = new HashMap<>();
	private CommandInitializer initializer =  new CommandInitializer();
	
	public CommandProvider() {}
	
	public Command takeCommand(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SAXException, IOException {
		if (commands.isEmpty()) {
			commands = initializer.initializeCommands();
		}
		String commandName = name.toUpperCase();
		return commands.get(commandName);
	}
	
}
