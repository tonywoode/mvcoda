package xstream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XMLReader {

	public static XMLSerialisable readXML(Path themeDir, XMLSerialisable xmlserialisable) {
		
		XStream xstream = new XStream();
		XMLSerialisable xml = null;
		
		//setup aliasesfor XStream (so xml doesn't contain absolute class names)
				xstream.alias("GfxElement", GFXElement.class);
				xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class);
				xstream.alias("Theme", Theme.class);

		try {	

			//String xml = xstream.toXML(xmlserialisable);
			System.out.println("\n ***********Reading xml " + xmlserialisable.toString() + "************\n");
			//System.out.println(xml);

			Path elementFileName = Paths.get(themeDir.toString(), xmlserialisable.getItemName() + ".xml");
			FileInputStream fs = new FileInputStream(elementFileName.toString());
			
			xml = (XMLSerialisable) xstream.fromXML(fs.toString());
			
			
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: something better?
		
		return xml;
	}

}
