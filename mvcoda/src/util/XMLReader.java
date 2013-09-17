package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.AnimatedGFXElement;
import themes.GFXElement;
import themes.Theme;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * Reads Themes,GFXElement types and Playlists that have been saved by MV-CoDA previously
 * returns these as XMLSerialisables for the caller to cast to the type required
 * @author tony
 *
 */
public class XMLReader {

	public static XMLSerialisable readXML(Path themeDir, String itemName) throws IOException {

		/**
		 * Create a new xstream insance
		 */
		XStream xstream = new XStream();
		
		/**
		 * Holds the content of various MV-CoDA types as xmlSerialisable types
		 */
		XMLSerialisable xml = null;

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);

		FileInputStream fs = null;
		try {	
			try {
				System.out.println("\n ***********Reading xml " + itemName + "************\n");

				Path elementFileName = Paths.get(themeDir.toString(), itemName + ".xml");
				fs = new FileInputStream(elementFileName.toString());
				xml = (XMLSerialisable) xstream.fromXML(fs);
			} 
			finally { if (fs != null) { fs.close(); }
			}
		} 
		catch (FileNotFoundException e) { throw new FileNotFoundException("Could not find the XML file" );  }
		catch (IOException e) { throw new IOException("Could not access the XML file" );  }
		return xml;
	}


	public static XMLSerialisable readPlaylistXML(Path path) throws FileNotFoundException, IOException {

		/**
		 * Create a new xstream insance
		 */
		XStream xstream = new XStream();
		
		/**
		 * Holds the content of various MV-CoDA types as xmlSerialisable types
		 */
		XMLSerialisable xml = null;

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		xstream.processAnnotations(Playlist.class);
		xstream.processAnnotations(PlaylistEntry.class);


		System.out.println("\n ***********Reading xml playlist " + path + "************\n");

		//defensively we will check the path has xml appended. If not we will add it.
		String pathAsString = path.toString(); //http://stackoverflow.com/questions/10471396/appending-the-file-type-to-a-file-in-java-using-jfilechooser
		if (!pathAsString.matches("(?i).*\\.xml")) { //the ?i will ignore case on what follows, we can also do things like ("(?i).*\\.(jpg|jpeg)")
		    pathAsString += pathAsString.substring(0, pathAsString.lastIndexOf(".")).concat(".xml");
		}

		FileInputStream fs = new FileInputStream(pathAsString);
		try {
			try { xml = (XMLSerialisable) xstream.fromXML(fs); }
			finally { if (fs != null) {fs.close();}
			} //we want to decouple the controllers from XStream specific exceptions, so we throw a Java exception
		} catch (XStreamException e1) { throw new IOException( "Error reading playlist XML" ); }
		
		return xml;


	}
}
