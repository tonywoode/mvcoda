package runner;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import media.xuggle.MusicVideoXuggle;
import themes.GFXElement;
import util.FileUtil;
import util.ThemeFinderImpl;
import view.ViewController;
import controllers.MainController;
import drawing.ImageCompositor;
import drawing.TextCompositor;

/**
 * Uses Java's logger class to keep track of particularly tricky classes. We can turn the level on and off for these classes separately
 * @author tony
 *
 */
public class HandleLoggers {

	/**
	 * Allows the root logger and the sublogs to be individually set by the developer. There was considered no need for package-level logging
	 * or a logging config file, instead we can indidually set the levels here. The top logger should NOT be set to ALL or we will get a great
	 * deal of logging from JavaFX
	 */
	public static void allLoggers() {
		Logger topLogger = java.util.logging.Logger.getLogger(""); //root logger
		Handler consoleHandler = topLogger.getHandlers()[0]; //root's handler will be first in returned list - we have provided no way to make other handlers. 
		topLogger.setLevel(Level.OFF); //we don't want JAVAFX's logging
		consoleHandler.setLevel(Level.ALL);
			
		//now we have an opportunity to set levels on the pacakage or class level, we do this by having public static loggers in the classes
		// see //http://stackoverflow.com/questions/470430/java-util-logging-logger-doesnt-respect-java-util-logging-level
		
		MainController.LOGGER.setLevel(Level.OFF);
		ViewController.LOGGER.setLevel(Level.OFF);
		ThemeFinderImpl.LOGGER.setLevel(Level.OFF);
		MusicVideoXuggle.LOGGER.setLevel(Level.OFF);
		ImageCompositor.LOGGER.setLevel(Level.OFF);
		FileUtil.LOGGER.setLevel(Level.OFF);
		TextCompositor.LOGGER.setLevel(Level.OFF);
		GFXElement.LOGGER.setLevel(Level.OFF);
		
	}
	
}
