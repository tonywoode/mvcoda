package view;

import javafx.stage.Stage;
import controllers.MainScreenController;

public class PopupException extends Exception {

	public PopupException() {}

	public PopupException(String text) {
		MainScreenController.popup(text);
	}

	public PopupException(Throwable cause)
	{
		super(cause);
	}

	public PopupException(String text, Throwable cause)
	{
		super (text, cause);
	}


}
