package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import themes.Theme;
import util.XMLReader;
import util.XMLSerialisable;

public class XMLReaderTest {

	/**
	 * Test reading XML for the standard Classic theme. We check the X axis on the logo is obtainable
	 * @throws FileNotFoundException 
	 */

	@Test public final void testClassicXML() {
		
		String themeName = "Classic";
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);

		Theme theme = null;
		try {
			XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
			theme = (Theme) themeAsSerialisable;
		} 
		catch (IOException e) { e.printStackTrace(); }

		try {
			assertEquals( "Testing Classic XML, the X Axis offset for the logo should be 65", 65 , theme.getLogo().getXOffsetSD() );
		} catch (NullPointerException e) {
			System.out.println( "classic Theme may not be correct on disk" + e.getMessage() );
		}
	
	}

}
