package by.tr.web.task_3_1.controller.command.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import by.tr.web.task_3_1.controller.command.Command;

public class CommandInitializer {
	
	private static final String COMMANDS_XML = "commands.xml";

	public Map<String, Command> initializeCommands() {
		return null;
		
		
	}
	
	private void parseConfig() throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		InputStream stream = getClass().getClassLoader().getResourceAsStream(COMMANDS_XML);
		InputSource	inputSource = new InputSource(stream);
		parser.parse(inputSource);
		
		Document document = parser.getDocument();
		Element root =  document.getDocumentElement();
		
		
	}
}
