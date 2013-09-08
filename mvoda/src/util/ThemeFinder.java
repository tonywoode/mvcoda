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

	public abstract ArrayList<Theme> returnThemes() throws IOException, InterruptedException;

	public abstract ArrayList<Path> returnDirectories(Path rootDir, String string, int i) throws Exception;
}
