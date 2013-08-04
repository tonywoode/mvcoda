package themes;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XStreamTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//XStream xstream = new XStream();
		String themeName = "Classic";
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);


		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme = (Theme) themeAsSerialisable;
		System.out.println(theme.getLogo().getXOffsetSD()); //TODO:classic case for JUnit this should be 65



		/*GFXElement urbanChart = new GFXElement();
		//urbanChart.setThemeName("Urban");
		urbanChart.setItemName("UrbanChart");
		//urbanChart.setType("Static");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );


		xstream.alias("gfxElement", GFXElement.class);
		String xml = xstream.toXML(urbanChart);
		System.out.println(xml);
		 */
		/*	//lets see what happens if i go for an existing class
		XStream xstreamExisting = new XStream();
		xstreamExisting.alias("GFXElement", FMTop20Chart.class);
		GFXElement chart = new FMTop20Chart(new Pop());
		String xml2 = xstream.toXML(chart);
		System.out.println("and from the class \n" + xml2);

		//hmmm it's printed the entire theme of Pop not just the FMTop20Chart - why?
		 */		

		//let's try making a GFX element as it is at the moment


	}

}
