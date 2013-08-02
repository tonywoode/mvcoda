package xstream;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;


public class XMLBuilderRunner {

	private static Path rootDir;
	private static Path themeDir;
	//static XStream xstream = new XStream(); //TODO: dependency

	public static void main(String[] args) throws FileNotFoundException {
		
		
		
		//which theme are we doing i.e.: which folder do we want the xml to end up in?
		String theme = "Urban";
		
		//setup paths - all themes and elements are going to go in a respective themes directory	
		rootDir = Paths.get("Elements");
		themeDir = Paths.get(rootDir.toString(),theme);

		//Make an object from this static class so we can run the code to generate the theme 
		XMLBuilderRunner gfx = new XMLBuilderRunner();
		gfx.run();
	}
	
	public void run() {	
		makeUrbanTheme();
	}
	
	public XMLSerialisable makeUrbanChart() {

		GFXElement urbanChart = new GFXElement();
		urbanChart.setItemName("UrbanChart");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );

		//XMLWriter.writeXML(themeDir, urbanChart);
		return urbanChart;
	}
	
	public XMLSerialisable makeKissBug1() {
		
		AnimatedGFXElement kissBug1 = new AnimatedGFXElement();
		kissBug1.setItemName("KissBug1");
		kissBug1.setVersion("1.0");
		kissBug1.setAuthor("BoxTV Design Team");
		kissBug1.setCoOrd( new CoOrd(0,0) );
		kissBug1.setFirstHoldFrame(29);
		kissBug1.setLastHoldFrame(43);
		kissBug1.setNumberOfFrames(75);
		kissBug1.setReverse(false);
		kissBug1.setLoop(false);
		kissBug1.setLoop(false);
		
		//XMLWriter.writeXML(themeDir, kissBug1);
		return kissBug1;
	}

//and then i build the rest, by hand. Quickly. Therefore its time for a theme

	public void makeUrbanTheme() {
		
		Theme urban = new Theme();
		urban.setItemName("Urban");
		
		XMLSerialisable urbanChart = makeUrbanChart();
		//XMLSerialisable urbanChart = XMLReader.readXML(themeDir, "UrbanChart");
		
		System.out.println(urbanChart);
		urban.setChart((GFXElement)urbanChart);
		
		
		XMLSerialisable kissBug1 = makeKissBug1();
		System.out.println(kissBug1);
		urban.setLogo((AnimatedGFXElement) kissBug1);
		
		XMLWriter.writeXML(themeDir, urban);
		
				
				//urban.setLogo(xstream.fromXML())
		
		
		
	}
}


