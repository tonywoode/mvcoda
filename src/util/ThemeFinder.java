/**
 * 
 */
package util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import themes.Theme;

/**
 * We will use this interface to return themes. The interface decouples the imp due to the fact
 * that today we are using xml serialisation, but tomorrow we may be looking these up from a database
 * @author Tony
 *
 */
public interface ThemeFinder {
	
	/**
	 * When passed the name of a theme will obtain it from the persitence store it and return it as a Theme
	 * @param theme the name of the Theme to return
	 * @return a Theme
	 * @throws IOException if the Theme cannot be accessed from the persistence store
	 */
	Theme returnTheme(String theme) throws IOException;

	/**
	 * Uses the theme root directory specified in theme, and returnDriectories() here, to find themes on the persistence store
	 * @return a list of the themes found
	 * @throws IOException if any Theme cannot be accessed from the persistence store
	 */
	ArrayList<Theme> returnThemes() throws IOException;

	/**
	 * When passed a root directory, filetype to search for, and a numbr of folder levels to iterate, will return the instances
	 * of that filetype found, as paths, that exist given those criteria
	 * @param rootDir starting directory
	 * @param fileType file type to search for (ie: postfix of filename)
	 * @param numLevels levels below starting directory to recurse
	 * @return a list of paths which contain the given filetype from the root directory to the specified number of levels
	 * @throws IOException if the location cannot be read
	 */
	ArrayList<Path> returnDirectories(Path rootDir, String fileType, int numLevels) throws IOException;
}
