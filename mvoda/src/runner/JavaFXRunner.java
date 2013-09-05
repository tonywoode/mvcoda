package runner;

import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import themes.GFXElement;
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
		controller.setView(viewController);
		viewController.setStage(stage); //pass the stage to the view controller, this is a field we have set for this purpose
		viewController.setViewListener(controller);
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	
	public void handleLoggers() {
		
		Logger.getGlobal().setLevel(Level.OFF); //Turn all loggers off
		
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
