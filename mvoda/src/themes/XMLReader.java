package themes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.management.modelmbean.XMLParseException;

import playlist.Playlist;
import playlist.PlaylistEntry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

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


	public static XMLSerialisable readPlaylistXML(Path path) throws FileNotFoundException, IOException, XMLParseException {

		XStream xstream = new XStream();
		XMLSerialisable xml = null;

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		xstream.processAnnotations(Playlist.class);
		xstream.processAnnotations(PlaylistEntry.class);


		System.out.println("\n ***********Reading xml playlist " + path + "************\n");


		FileInputStream fs = new FileInputStream(path.toString());
		try {
			try { xml = (XMLSerialisable) xstream.fromXML(fs); }
			finally { if (fs != null) {fs.close();}
			} //we want to decouple the controllers from XStream specific exceptions, so we throw a Java exception
		} catch (XStreamException e1) { throw new  XMLParseException("e1.printStackTrace()"); }
		
		return xml;


	}
}
