package runner;

import java.net.URL;
import java.util.Enumeration;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import themes.GFXElement;
import util.ThemeFinderImpl;
import view.ViewController;
import controllers.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXRunner extends Application {

	//private final static Logger logger = Logger.getLogger(JavaFXRunner.class.getName()); 

	@Override
	public void start(Stage stage) throws Exception {
		handleLoggers();
		stage.setTitle("MV-CODA");
		//Parent root = FXMLLoader.load(getClass().getResource("../view/GUI.fxml"));	
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("../view/GUI.fxml");
		loader.setLocation(location);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root = (Parent) loader.load(location.openStream());
		ViewController viewController = (ViewController)loader.getController();
		MainScreenController controller = new MainScreenController();
		MainScreenController.setStage(stage);
		controller.setView(viewController);
		viewController.setStage(stage); //pass the stage to the view controller, this is a field we have set for this purpose

		viewController.setViewListener(controller);
		stage.setScene(new Scene(root));
		stage.show();
	}


	public void handleLoggers() { //http://stackoverflow.com/questions/470430/java-util-logging-logger-doesnt-respect-java-util-logging-level

		Logger topLogger = java.util.logging.Logger.getLogger(""); // Handler for console (reuse it if it already exists)
		Handler consoleHandler = topLogger.getHandlers()[0]; //root's handler will be first in returned list - we have provided no way to make other handlers. 
		/*
		 * Here we set the Global Logging level
		 */
		consoleHandler.setLevel(Level.ALL);
		
		Logger themeFinder = ThemeFinderImpl.LOGGER;
		
		//ThemeFinderImpl theme = new ThemeFinderImpl();
		themeFinder = Logger.getLogger("util.ThemeFinderImpl");
		themeFinder.setLevel(Level.OFF);
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

	/*
	 * IF IT STOPS WORKING, TRY THIS:
	 * stage.setTitle("MVCODA");
		URL location = getClass().getResource("GUI.fxml");
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(location);
		fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root = (Parent) fxmlloader.load(location.openStream());
		stage.setScene(new Scene(root));
		stage.show();
	 */

	public static void main(String[] args) {
		launch(args);
	}
}
