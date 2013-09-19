package runner;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ViewController;
import controllers.MainController;

/**
 * Main launcher for MV-CoDA, which uses JavaFX for its GUI. Will setup loggers, read the GUI from the FXML file, setup controller, and launch
 * @author tony
 *
 */
public class JavaFXRunner extends Application {


	@Override
	public void start(Stage stage) throws Exception {
		
		/**
		 * Configure logging in this class, the logging controller is also in runner package - named HandleLoggers
		 */
		HandleLoggers.allLoggers();
		
		
		stage.setTitle("MV-CODA");
		
		//load resources from the FXML file and set root to the result
		FXMLLoader loader = new FXMLLoader(); //this will load the JAVAFX FXML file from disk which describes the GUI
		URL location = getClass().getResource("../view/GUI.fxml");
		loader.setLocation(location); //set the location of the above FXML
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root = (Parent) loader.load(location.openStream());
		
		//then setup our controller classes
		ViewController viewController = (ViewController)loader.getController(); //specify the view controller class from the FXML
		MainController controller = new MainController(); //we will also use a main controller
		MainController.setStage(stage); //which we pass the javaFX stage to
		controller.setView(viewController);
		ViewController.setStage(stage); //pass the stage to the view controller, this is a field we have set for this purpose
		ViewController.setViewListener(controller); //now we have both controllers able to see each other
		stage.setScene(new Scene(root)); //start a new JAVAFX scene
		stage.show();
	}

	/**
	 * Launches MV-CoDA
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
