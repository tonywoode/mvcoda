package themes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XMLReader {

	public static XMLSerialisable readXML(Path themeDir, String itemName) {
		
		XStream xstream = new XStream();
		XMLSerialisable xml = null;
		
		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		//TODO - If we do this we don't get animate elements with "Class=AnimatedGFXElement" in the xml - see http://stackoverflow.com/questions/2008043/xstream-removing-class-attribute
		//but then of course it doesn't work - need a custom converter
		//xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class, GFXElement.class);

		try {	

			//String xml = xstream.toXML(xmlserialisable);
			System.out.println("\n ***********Reading xml " + itemName + "************\n");
			//System.out.println(xml);

			Path elementFileName = Paths.get(themeDir.toString(), itemName + ".xml");
			FileInputStream fs = new FileInputStream(elementFileName.toString());
			
			xml = (XMLSerialisable) xstream.fromXML(fs);
			
			
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: something better?
		
		return xml;
	}
	
	
public static XMLSerialisable readPlyalistXML(Path themeDir) {
		
		XStream xstream = new XStream();
		XMLSerialisable xml = null;
		
		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		//TODO - If we do this we don't get animate elements with "Class=AnimatedGFXElement" in the xml - see http://stackoverflow.com/questions/2008043/xstream-removing-class-attribute
		//but then of course it doesn't work - need a custom converter
		//xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class, GFXElement.class);

		try {	

			//String xml = xstream.toXML(xmlserialisable);
			System.out.println("\n ***********Reading xml playlist " + themeDir + "************\n");
			//System.out.println(xml);

			Path elementFileName = themeDir;
			FileInputStream fs = new FileInputStream(elementFileName.toString());
			
			xml = (XMLSerialisable) xstream.fromXML(fs);
			
			
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: something better?
		
		return xml;
	}

}
