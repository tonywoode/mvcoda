package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;

public class XMLReaderTest {

	/**
	 * Test reading XML for the standar Classic theme. We check the X axis on the logo is obtainable
	 * @throws FileNotFoundException 
	 */

	@Test public final void testClassicXML() throws FileNotFoundException {
		
		String themeName = "Classic";
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);

		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme = (Theme) themeAsSerialisable;

		assertEquals( "Testing Classic XML, the X Axis offset for the logo should be 65", 65 , theme.getLogo().getXOffsetSD() );
	
	}

}
