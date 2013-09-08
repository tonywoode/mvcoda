package runner;

import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import media.xuggle.MusicVideoXuggle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FileUtil;
import util.ThemeFinderImpl;
import view.ViewController;
import controllers.MainController;
import drawing.ImageCompositor;
import drawing.TextCompositor;

public class JavaFXRunner extends Application {

	//private final static Logger logger = Logger.getLogger(JavaFXRunner.class.getName()); 

	@Override
	public void start(Stage stage) throws Exception {
		
		/**
		 * Configure logging in this class also in runner package
		 */
		HandleLoggers.allLoggers();
		
		
		stage.setTitle("MV-CODA");
		//Parent root = FXMLLoader.load(getClass().getResource("../view/GUI.fxml"));	
		FXMLLoader loader = new FXMLLoader();
		URL location = getClass().getResource("../view/GUI.fxml");
		loader.setLocation(location);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		Parent root = (Parent) loader.load(location.openStream());
		ViewController viewController = (ViewController)loader.getController();
		MainController controller = new MainController();
		MainController.setStage(stage);
		controller.setView(viewController);
		viewController.setStage(stage); //pass the stage to the view controller, this is a field we have set for this purpose

		viewController.setViewListener(controller);
		stage.setScene(new Scene(root));
		stage.show();
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
