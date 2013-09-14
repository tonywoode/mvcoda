package util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Utility class for dealing with file-lookup requirements. All simple static methods
 * @author tony
 *
 */
public class FileUtil  {

	public final static Logger LOGGER = Logger.getLogger(FileUtil.class.getName()); //get a logger for this class

	/**
	 * When passed a path to a file will return its filetype as a string
	 * @param fileUNC the path to the file
	 * @return filetype as a string
	 */
	public static String getFiletype(String fileUNC) {

		String reverse = reverseString(fileUNC);
		int j = 0;
		while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period
								LOGGER.info("The first digit is at position: " + j); 
		String filetypeReversed = reverse.substring(0, j + 1);
		//we digress here to save out the filetype
								LOGGER.info("Here's the reversed filetype: " + filetypeReversed);
		String filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
		return filetype;
	}


	/**
	 * when passed a path to a file will return everything but the filetype and the period before it
	 * @param fileUNC
	 * @return path without filetype
	 */
	public static String removeFiletype(String fileUNC) {
		String reverse = reverseString(fileUNC);
		int k=0;
		while (!(reverse.charAt(k) == '.')) { k++; }
		String filetypeRemovedReversed = reverse.substring(k + 1);
		String filetypeRemoved = new StringBuilder(filetypeRemovedReversed).reverse().toString();
		return filetypeRemoved;
	}

	private static String reverseString(String fileUNC) {
		String reverse = new StringBuilder(fileUNC).reverse().toString(); //so we reverse
								LOGGER.info("reversed relative filename is: " + reverse);
		return reverse;
	}

	/**
	 * When passed a path to a directory, will return the number of directories in that directory
	 * This method is intended for future use - it's for when we want to make sure the user is not adding more chart entries than
	 * there are rendered numbers
	 * @param path a folder we wish to count folders in
	 * @return number of dirs in dir
	 * @throws IOException  if the folder could not be accessed
	 */
	public static int countFoldersInFolder(Path path) throws IOException {

		//first create a filter which will tell us if the file is a directory
		DirectoryStream.Filter filter = new DirectoryStream.Filter() {
			@Override public boolean accept(Object entry) throws IOException { return Files.isDirectory( (Path) entry); } 
		};
								LOGGER.info("Path to count" + path);
		//then iterate through the folders, incrementing the output number
		int i = 0;
		try (DirectoryStream<Path> dstream =  Files.newDirectoryStream(path, filter ) ) {
			for (Path p : dstream) { 
								LOGGER.info("Next filename " + p.getFileName()); 
				i++;  }
		} catch (IOException e) { throw new IOException("Could not count folders in path" + e.getMessage()); }
								LOGGER.info("found " + i + " folders");
		return i;

	}


}

