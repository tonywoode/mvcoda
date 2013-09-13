package themes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import playlist.Playlist;
import playlist.PlaylistEntry;

import com.thoughtworks.xstream.XStream;

/**
 * Writes Themes,GFXElement types and Playlists for MV-CoDA
 * You pass these as XMLSerialisables as the associated classes implement XMLSerialisable
 * @author tony
 *
 */
public class XMLWriter {

	/*
	 * TODO: these two methods can be merged
	 */
	
	public static void writeXML(Path themeDir, XMLSerialisable xmlserialisable) throws IOException {

		XStream xstream = new XStream();

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class); //TODO: remove XStream class attribute using an XStream converter
		xstream.processAnnotations(Theme.class);


		String xml = xstream.toXML(xmlserialisable);
		System.out.println("\n ***********Generating xml " + xmlserialisable.getItemName() + "************\n");
		

		try {	
			Path elementFileName = Paths.get(themeDir.toString(), xmlserialisable.getItemName() + ".xml");
			FileOutputStream fs = null;
			try {
				fs = new FileOutputStream(elementFileName.toString());
				xstream.toXML(xmlserialisable, fs);
			} finally {
				if (fs != null) { fs.close(); }
			}
		} 
		catch (IOException e) {	throw new IOException("Could not write the XML file"); }
		
		System.out.println(xml); //we will print the xml
	}



	public static void writePlaylistXML(Boolean playlist, Path path, XMLSerialisable xmlserialisable) throws IOException {

		XStream xstream = new XStream();

		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		xstream.processAnnotations(Playlist.class);
		xstream.processAnnotations(PlaylistEntry.class);


		String xml = xstream.toXML(xmlserialisable);
		System.out.println("\n ***********Generating playlist xml " + xmlserialisable.getItemName() + "************\n");


		try { 
			FileOutputStream fs = new FileOutputStream(path.toString());
			try { xstream.toXML(xmlserialisable, fs);} 
			finally { if (fs != null) { fs.close(); }
			}
		} 
		catch (IOException e) {	throw new IOException("Could not write the XML file"); }
		
		System.out.println(xml); //we will then print the XML to the console for reference


	}

}
