package xstream;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;


public class GFXElementXMLBuilders {

	private static Path rootDir;
	private static Path themeDir;
	static XStream xstream = new XStream();

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {

		GFXElementXMLBuilders gfx = new GFXElementXMLBuilders();
		gfx.makeUrbanChart();

	}
	
	public void makeUrbanChart(){

		rootDir = Paths.get("Elements");
		themeDir = Paths.get(rootDir.toString(),"Urban");


		xstream.alias("gfxElement", GFXElement.class);

		GFXElement urbanChart = new GFXElement();
		urbanChart.setThemeName("Urban");
		urbanChart.setElementName("UrbanChart");
		urbanChart.setType("Static");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );


		String xml = xstream.toXML(urbanChart);
		System.out.println("Making xml " + urbanChart.toString() + "\n");
		System.out.println(xml);
		writeXML(urbanChart);
	}


	public void writeXML(GFXElement gfxElement) {
		try {
			Path elementFileName = Paths.get(themeDir.toString(), gfxElement.getElementName() + ".xml");
			FileOutputStream fs = new FileOutputStream(elementFileName.toString());
			xstream.toXML(gfxElement, fs);
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); }
	}


}


