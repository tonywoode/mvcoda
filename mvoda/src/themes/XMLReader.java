package themes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import playlist.Playlist;
import playlist.PlaylistEntry;

import com.thoughtworks.xstream.XStream;

/**
 * Reads Themes,GFXElement types and Playlists that have been saved by MV-CoDA previously
 * returns these as XMLSerialisables for the caller to cast to the type required
 * @author tony
 *
 */
public class XMLReader {

	public static XMLSerialisable readXML(Path themeDir, String itemName) {

		XStream xstream = new XStream();
		XMLSerialisable xml = null;

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		//TODO - If we do the below we don't get animate elements with "Class=AnimatedGFXElement" in the xml - see http://stackoverflow.com/questions/2008043/xstream-removing-class-attribute
		//but then we'd need a custom converter
		//xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class, GFXElement.class);

		try {	
			System.out.println("\n ***********Reading xml " + itemName + "************\n");

			Path elementFileName = Paths.get(themeDir.toString(), itemName + ".xml");
			FileInputStream fs = new FileInputStream(elementFileName.toString());

			xml = (XMLSerialisable) xstream.fromXML(fs);
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: Exception
		return xml;
	}


	public static XMLSerialisable readPlaylistXML(Path themeDir) {

		XStream xstream = new XStream();
		XMLSerialisable xml = null;

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		xstream.processAnnotations(Playlist.class);
		xstream.processAnnotations(PlaylistEntry.class);

		try {	
			System.out.println("\n ***********Reading xml playlist " + themeDir + "************\n");

			Path elementFileName = themeDir;
			FileInputStream fs = new FileInputStream(elementFileName.toString());

			xml = (XMLSerialisable) xstream.fromXML(fs);

		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: Exception

		return xml;
	}

}
