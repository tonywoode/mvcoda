/**
 * 
 */
package themes;

/**
 * We will use this interface to return themes. The interface decouples the imp due to the fact
 * that today we are using xml serialisation, but tomorrow we may be looking these up from a database
 * @author Tony
 *
 */
public interface ThemeFinder {

	//ArrayList()<Theme> returnThemes();
}
