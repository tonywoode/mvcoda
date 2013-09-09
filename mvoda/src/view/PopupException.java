package view;

import javafx.stage.Stage;
import controllers.MainController;

public class PopupException extends Exception {

	public PopupException() {}

	public PopupException(String text) {
		//ViewController.popup(text);
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
