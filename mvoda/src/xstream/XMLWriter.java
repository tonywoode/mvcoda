package xstream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XMLWriter {

	public static void writeXML(Path themeDir, XMLSerialisable xmlserialisable) {

		try {

			XStream xstream = new XStream();

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
