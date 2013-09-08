package runner;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import media.xuggle.MusicVideoXuggle;
import themes.GFXElement;
import util.FileUtil;
import util.ThemeFinderImpl;
import controllers.MainController;
import drawing.ImageCompositor;
import drawing.TextCompositor;

public class HandleLoggers {

	//http://stackoverflow.com/questions/470430/java-util-logging-logger-doesnt-respect-java-util-logging-level

	public static void allLoggers() {
		Logger topLogger = java.util.logging.Logger.getLogger(""); //root logger
		Handler consoleHandler = topLogger.getHandlers()[0]; //root's handler will be first in returned list - we have provided no way to make other handlers. 
		topLogger.setLevel(Level.OFF); //we don't want JAVAFX's logging
		consoleHandler.setLevel(Level.OFF);
		
		
		//now we have an opportunity to set levels on the pacakage or class level, we do this by having public static loggers in the classes
		
		MainController.LOGGER.setLevel(Level.ALL);
		ThemeFinderImpl.LOGGER.setLevel(Level.ALL);
		MusicVideoXuggle.LOGGER.setLevel(Level.ALL);
		ImageCompositor.LOGGER.setLevel(Level.ALL);
		FileUtil.LOGGER.setLevel(Level.ALL);
		TextCompositor.LOGGER.setLevel(Level.ALL);
		GFXElement.LOGGER.setLevel(Level.ALL);
		
		
		//ThemeFinderImpl theme = new ThemeFinderImpl();
		//themeFinder = Logger.getLogger("util.ThemeFinderImpl");
		//themeFinder.setLevel(Level.OFF);
		

		//System.out.println("My Name is " + themeFinder.getName());
		//Handler ch = new ConsoleHandler();
        //themeFinder.addHandler(ch);
       // ch.setLevel(Level.ALL);
		
		
		
		//themeFinder.setLevel(Level.OFF);
		//themeFinder.setUseParentHandlers(false);
		//boolean isInfoLoggable = themeFinder.isLoggable(Level.OFF);
		//System.out.println(isInfoLoggable);
		//System.out.println(Logger.getLogger("util.ThemeFinderImpl").getLevel());

		//logger.setLevel(Level.OFF);
		//Logger.getLogger("themes.GFXElement");
		//Logger GFXLogger = logger.getLogger("themes.GFXElement");
		//System.out.println(GFXLogger.getName());
		//GFXLogger.setLevel(Level.FINE);
		//LogManager.getLogManager().getLogger("themes.GFXElement").setLevel(Level.OFF);
		//Enumeration<String> names = LogManager.getLogManager().getLoggerNames();

		//System.out.println(names.nextElement());
		//System.out.println(names.nextElement());
	}
	
}
