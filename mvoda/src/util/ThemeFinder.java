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
	
	Theme returnTheme(String theme) throws IOException;

	ArrayList<Theme> returnThemes() throws IOException;

	ArrayList<Path> returnDirectories(Path rootDir, String fileType, int numLevels) throws IOException;
}
