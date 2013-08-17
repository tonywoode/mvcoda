package runner;

import java.net.URL;

import view.ViewController;
import controllers.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXRunner extends Application {
	@Override
	public void start(Stage stage) throws Exception {
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
