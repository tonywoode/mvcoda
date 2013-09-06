package view;

import javafx.stage.Stage;
import controllers.MainScreenController;

public class PopupException extends Exception {

	public PopupException(String text) {
		MainScreenController.popup(text);
	}
}
