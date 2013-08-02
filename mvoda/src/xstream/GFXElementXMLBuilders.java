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
		
		xstream.alias("GfxElement", GFXElement.class);
		xstream.alias("AnimatedGFXElement", AnimatedGFXElement.class);
		xstream.alias("Theme", Theme.class);

		GFXElementXMLBuilders gfx = new GFXElementXMLBuilders();
		gfx.makeUrbanChart();
		gfx.makeKissBug1();
		gfx.makeUrbanChart();

	}
	
	public void writeXML(XMLSerialisable gfxElement) {
		try {
			Path elementFileName = Paths.get(themeDir.toString(), gfxElement.getItemName() + ".xml");
			FileOutputStream fs = new FileOutputStream(elementFileName.toString());
			xstream.toXML(gfxElement, fs);
		} 
		catch (FileNotFoundException e) {	e.printStackTrace(); }
	}
	
	//public voidreadXML() //we need to write and read not just GFXElements here, I'm trying to load a theme
	//or do we just need to load a theme?

	
	
	public void makeUrbanChart(){

		rootDir = Paths.get("Elements");
		themeDir = Paths.get(rootDir.toString(),"Urban");


		

		GFXElement urbanChart = new GFXElement();
		//urbanChart.setThemeName("Urban");
		urbanChart.setItemName("UrbanChart");
		//urbanChart.setType("Static");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );


		String xml = xstream.toXML(urbanChart);
		System.out.println("Making xml " + urbanChart.toString() + "\n");
		System.out.println(xml);
		writeXML(urbanChart);
	}
	
	public void makeKissBug1() {
		AnimatedGFXElement kissBug1 = new AnimatedGFXElement();
		kissBug1.setItemName("KissBug1");
		//kissBug1.setType("Something");
		kissBug1.setVersion("1.0");
		kissBug1.setAuthor("BoxTV Design Team");
		kissBug1.setCoOrd( new CoOrd(0,0) );
		kissBug1.setFirstHoldFrame(29);
		kissBug1.setLastHoldFrame(43);
		kissBug1.setNumberOfFrames(75);
		kissBug1.setReverse(false);
		kissBug1.setLoop(false);
		kissBug1.setLoop(false);
		
		String xml = xstream.toXML(kissBug1);
		System.out.println("Making xml " + kissBug1.toString() + "\n");
		System.out.println(xml);
		writeXML(kissBug1);
		
		
	}

//and then i build the rest, by hand. Quickly. Therefore its time for a theme

	public void makeUrbanTheme() {
		
		Theme urban = new Theme();
		//urban.setLogo(xstream.fromXML(file))
		
		
		
	}
}


