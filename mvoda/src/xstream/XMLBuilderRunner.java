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
	static XStream xstream = new XStream(); //TODO: dependency

	public static void main(String[] args) throws FileNotFoundException {
		
		//which theme are we doing i.e.: which folder do we want the xml to end up in?
		String theme = "Urban";
		
		//setup paths - all themes and elements are going to go in a respective themes directory	
		rootDir = Paths.get("Elements");
		themeDir = Paths.get(rootDir.toString(),theme);

		//Make an object from this static class so we can run the code to generate the theme 
		XMLBuilderRunner gfx = new XMLBuilderRunner();
		gfx.makeUrbanChart();
		gfx.makeKissBug1();

	}
	
	
	




	
	public  void makeUrbanChart() {

		GFXElement urbanChart = new GFXElement();
		urbanChart.setItemName("UrbanChart");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );

		XMLWriter.writeXML(themeDir, urbanChart);		
	}
	
	public void makeKissBug1() {
		
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
		
		XMLWriter.writeXML(themeDir, kissBug1);
		
	}

//and then i build the rest, by hand. Quickly. Therefore its time for a theme

	public void makeUrbanTheme() {
		
		Theme urban = new Theme();
		FileInputStream fs = 
		GFXElement urbanChart = (GFXElement)xstream.fromXML(file)
		
				
				//urban.setLogo(xstream.fromXML())
		
		
		
	}
}


