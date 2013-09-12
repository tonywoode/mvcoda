package view;

public class GFXElementException extends Exception {

	public GFXElementException() {}

	public GFXElementException(String text) {
		super(text);
	}

	public GFXElementException(Throwable cause)
	{
		super(cause);
	}

	public GFXElementException(String text, Throwable cause)
	{
		super (text, cause);
	}


}
