package runner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXRunner extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("MVCODA");
		Parent root = FXMLLoader.load(getClass().getResource("../view/GUI.fxml"));
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
