package util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;

import controllers.MainController;

import themes.Theme;

public class FileUtil  {

	public final static Logger LOGGER = Logger.getLogger(FileUtil.class.getName()); //get a logger for this class
	
	public static String getFiletype(String fileUNC) {

		String reverse = reverseString(fileUNC);
		int j = 0;
		while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period. That's not going to work on unix then TODO: maybe "if system=windows"
		LOGGER.info("The first digit is at position: " + j); 
		String filetypeReversed = reverse.substring(0, j + 1);
		//we digress here to save out the filetype
		LOGGER.info("Here's the reversed filetype: " + filetypeReversed);
		String filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
		return filetype;
	}


	public static String removeFiletype(String fileUNC) {
		String reverse = reverseString(fileUNC);
		int k=0;
		while (!(reverse.charAt(k) == '.')) { k++; }
		String filetypeRemovedReversed = reverse.substring(k + 1);
		String filetypeRemoved = new StringBuilder(filetypeRemovedReversed).reverse().toString();
		return filetypeRemoved;
	}

	private static String reverseString(String fileUNC) {
		//String i = fileUNC;
		//String path = i;
		//String base = fileUNC.toString();
		//first we need to get the relative path - we do this by effectively masking
		//String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
		//Logger.getGlobal().info("relative filename is: " + relative);
		//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
		String reverse = new StringBuilder(fileUNC).reverse().toString(); //so we reverse
		LOGGER.info("reversed relative filename is: " + reverse);
		return reverse;
	}

	public static int countFoldersInFolder(Path path) {
		
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
		} catch (IOException e) { e.printStackTrace(); }//todo
		LOGGER.info("found " + i + " folders");
		return i;

	}


}

