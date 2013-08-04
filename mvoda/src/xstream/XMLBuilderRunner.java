package xstream;


import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class XMLBuilderRunner {

	//This is the root directory for all the XML's. It is not expected to change or be alterable
	//private static final Path rootDir = Paths.get("Theme");

	//this is the changable directory for the theme being generated
	private Path themeDir;


	//these variables will be constant to all the XML's that are being created for version 1.0 so are fields here
	//private String path = "Total Rubbish"; //TODO: I don't know how this is being used now
	private String author = "BoxTV Design Team";
	private String version  = "1.0";


	public static void main(String[] args) throws FileNotFoundException {

		//Make an object from this static class so we can run the code to generate the theme 
		XMLBuilderRunner gfx = new XMLBuilderRunner();
		gfx.run();
	}

	public void run() {	
		makeUrbanTheme();
		makePopTheme();
		makeClassicTheme();
	}


	public void makeUrbanTheme() {
		String themeName = "Urban"; //which theme are we doing i.e.: what do we want it called in the xml, and which  folder do we want the xml to end up in?
		Theme theme = new Theme();

		theme.setItemName(themeName);

		//generate the elements
		XMLSerialisable logo = new AnimatedGFXElement(themeName, "KissBug1", "logo", author,version, new CoOrd(0,0), new FrameData(29, 43, 75), new AnimationData(false, false, 1) );
		XMLSerialisable chart = new GFXElement(themeName, "UrbanChart", "chart", author, version, new CoOrd(400,0)); //this chart is a static image
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "UK40BB", "strap", author,version, new CoOrd(0,0), new FrameData(26, 150, 176), new AnimationData(false, true, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "UK40BBNums", "numbers", author,version, new CoOrd(0,0), new FrameData(26, 151, 176), new AnimationData(false, true, 1) );
		XMLSerialisable transition = new AnimatedGFXElement(themeName, "UK40BBTransition", "transition", author,version, new CoOrd(0,0), new FrameData(-1, -1, 16), new AnimationData(false, false, 1) );

		//set the elements into the theme
		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		//set our path to write to, and write the xml for this theme
		themeDir = Paths.get(Theme.getRootDir().toString(),themeName);
		XMLWriter.writeXML(themeDir, theme);
	}
	
	public void makePopTheme() {
		String themeName = "Pop"; //which theme are we doing i.e.: what do we want it called in the xml, and which  folder do we want the xml to end up in?
		Theme theme = new Theme();

		theme.setItemName(themeName);

		//generate the elements
		XMLSerialisable logo = new AnimatedGFXElement(themeName, "4M1BugFrames", "logo", author, version, new CoOrd(0,0), new FrameData(51, 79, 126), new AnimationData(false, false, 1) );
		XMLSerialisable chart = new AnimatedGFXElement(themeName, "FMTop20Chart", "chart", author, version, new CoOrd(0,0), new FrameData(10, 43, 52), new AnimationData(false, false, 1) );
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "PeriscopeFrames", "strap", author,version, new CoOrd(0,0), new FrameData(20, 70, 101), new AnimationData(false, false, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "Animated/Num", "numbers", author,version, new CoOrd(-680,0), new FrameData(9, 63, 76), new AnimationData(false, true, 1) );
		//we don't have a transition for Pop
		XMLSerialisable transition = null;

		//set the elements into the theme
		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		//set our path to write to, and write the xml for this theme
		themeDir = Paths.get(Theme.getRootDir().toString(),themeName);
		XMLWriter.writeXML(themeDir, theme);
	}	
	

	public void makeClassicTheme() {
		String themeName = "Classic"; //which theme are we doing i.e.: what do we want it called in the xml, and which  folder do we want the xml to end up in?
		Theme theme = new Theme();

		theme.setItemName(themeName);

		//generate the elements
		XMLSerialisable logo = new AnimatedGFXElement(themeName, "QLogo", "logo", author, version, new CoOrd(65,0), new FrameData(84, 84, 86), new AnimationData(true, false, 2) );
		XMLSerialisable chart = new AnimatedGFXElement(themeName, "QChartBlack", "chart", author, version, new CoOrd(480,0), new FrameData(47, 47, 48), new AnimationData(true, false, 1) );
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "QStrap", "strap", author,version, new CoOrd(-33,230), new FrameData(66, 67, 131), new AnimationData(false, false, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "QRedBlockLarger", "numbers", author,version, new CoOrd(24,194), new FrameData(54, 55, 78), new AnimationData(false, false, 1) );
		XMLSerialisable transition = new AnimatedGFXElement(themeName, "QTransition", "transition", author,version, new CoOrd(0,200), new FrameData(-1, -1, 32), new AnimationData(false, false, 1) );


		//set the elements into the theme
		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		//set our path to write to, and write the xml for this theme
		themeDir = Paths.get(Theme.getRootDir().toString(),themeName);
		XMLWriter.writeXML(themeDir, theme);
	}
	
	
	
}

//Don't forget you can print out the theme constituents names
		//System.out.println(urbanChart);
		//System.out.println(kissBug1);
		//TODO: here's how to read an xml (from the old code where I was writing out and then reading elements!)
		//XMLSerialisable urbanChart = XMLReader.readXML(themeDir, "UrbanChart");
		//and something else I probably didn't try
		//urban.setLogo(xstream.fromXML())
