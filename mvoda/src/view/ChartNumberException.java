package view;

public class ChartNumberException extends Exception {

	public ChartNumberException() {}

	public ChartNumberException(String text) {
		super(text);
	}

	public ChartNumberException(Throwable cause)
	{
		super(cause);
	}

	public ChartNumberException(String text, Throwable cause)
	{
		super (text, cause);
	}


}
