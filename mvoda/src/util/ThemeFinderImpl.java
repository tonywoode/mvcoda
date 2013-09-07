package util;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import controllers.MainController;

import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;

/**
 * Finds Theme XML's for MV-CoDA and deserialises them into Themes
 * @author tony
 *
 */
public class ThemeFinderImpl implements ThemeFinder {
	
	/*
	 * The path to the Theme root directory is specified in Theme
	 * //TODO: should the dir live in theme if we wish to decouple?
	 */
	private static final Path rootDir = Theme.getRootDir();

	public final static Logger LOGGER = Logger.getLogger(ThemeFinderImpl.class.getName()); //get a logger for this class
	
	/**
	 * 
	 * Walks the directory tree rooted at the root directory specified in Theme looking for .xml files representing Themes, 
	 * deserialises and returns those it finds in an array of Themes
	 * Does not recurse further than root folders of themes i.e.: any lower than Theme\themefolder\*.xml is not considered valid
	 * @return an array list of the paths to the found xml files
	 * @throws Exception
	 */
	public ArrayList<Theme> returnThemes() throws IOException, InterruptedException { //TODO: interrupted exception?
		
		
		
		ArrayList<Path> pathArray = new ArrayList<>();
		ArrayList<Theme> themeArray = new ArrayList<>();
		
		try {
			pathArray = returnDirectories(rootDir, "xml", 2);
		} catch (Exception e) {
			// TODO Exception
			e.printStackTrace();
		}
		
		for ( int i=0; i<pathArray.size(); i++) {
			Path themePath = pathArray.get(i);
			Path themeDir = themePath.getParent();
			String themeNameWithFiletype = themePath.getFileName().toString(); //TODO: is this really necessary?
			String themeName = FileUtil.removeFiletype(themeNameWithFiletype);//yes we need the name of the theme before we can load it, but do we have to get the filename to string and do all this?
			XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
			Theme theme = (Theme) themeAsSerialisable;
			themeArray.add(theme);
		}
		return themeArray;
	}
	
	
	/**
	 * Walks a file tree to the specified number of levels returning all instances of the file extension specified it finds
	 * @param rootdir the directory relative to the classpath to be searched
	 * @param filetype the filetype to search for - NOT including the dot
	 * @param numLevels - the number of levels to recurse down the file tree
	 * @return arraylist of paths representing the found instances of files containing the filetype
	 * @throws Exception //TODO
	 */
	public static ArrayList<Path> returnDirectories(Path rootdir, final String filetype, int numLevels) throws Exception { //TODO exception
		
		
		final ArrayList<Path> pathArray = new ArrayList<>();
		
		//walk file tree with NO options BUT set but a max depth of 2 levels - however, any xml in root or not in a subdir folder could still be picked up - TODO
		Files.walkFileTree(rootDir, EnumSet.noneOf(FileVisitOption.class), numLevels, new FileVisitor<Path>() {
			
	        //Compile regular expression pattern only one time - see http://stackoverflow.com/questions/2534632/list-all-files-from-a-directory-recursively-with-java
	        private Pattern pattern = Pattern.compile(".*(." + filetype + ")");
	        
	        @Override public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
	            boolean matches = pattern.matcher(path.toString().toLowerCase()).matches();  //note the lower case, may need to undo this later  
	            if (matches) { LOGGER.info("Theme Match found - name:  " + path);
	            pathArray.add(path);
	            }
	            
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
	            e.printStackTrace();
	            //Do not continue if root dir has failed TODO: this to gui....
	            return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE;
	        }
	    });
		return pathArray;
		
	}
	
	
	public static void main(String[] args) throws Exception { //TODO get rid of this PSVM
		//returnDirectories();
		ArrayList<Path> thePathsItFound = new ArrayList<>();
		thePathsItFound=returnDirectories(rootDir, "xml", 2);
		System.out.println(thePathsItFound);
	
		//ok great we can get the path, but you need to save the PATH in a THEME - a theme needs to know its own path eh!
		//testing again
	}
	
	
	}

