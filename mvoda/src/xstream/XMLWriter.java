package xstream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XMLWriter {

	public static void writeXML(Path themeDir, XMLSerialisable xmlserialisable) {
		
		XStream xstream = new XStream();
		
		xstream.processAnnotations(GFXElement.class);
		xstream.processAnnotations(AnimatedGFXElement.class);
		xstream.processAnnotations(Theme.class);
		//If we don't do this we get animate elements with "Class=AnimatedGFXElement" in the xml - see http://stackoverflow.com/questions/2008043/xstream-removing-class-attribute
		xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class, GFXElement.class);
		
		//setup aliasesfor XStream (so xml doesn't contain absolute class names)
		//xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class);		
		//xstream.alias("GfxElement", GFXElement.class);	
		//xstream.alias("Theme", Theme.class);

		try {	

			String xml = xstream.toXML(xmlserialisable);
			System.out.println("\n ***********Generating xml " + xmlserialisable.toString() + "************\n");
			System.out.println(xml);

			Path elementFileName = Paths.get(themeDir.toString(), xmlserialisable.getItemName() + ".xml");
			FileOutputStream fs = new FileOutputStream(elementFileName.toString());
			
			xstream.toXML(xmlserialisable, fs);
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); } //TODO: something better?
	}

}
