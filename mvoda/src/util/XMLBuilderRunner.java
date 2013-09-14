package util;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import themes.AnimatedGFXElement;
import themes.AnimationData;
import themes.CoOrd;
import themes.FrameData;
import themes.GFXElement;
import themes.Theme;

/**
 * This class was used to create the initial Theme XML files for default themes included in MV-CoDA i.e.: it describes each GFXElement in the three included themes
 * @author Tony
 *
 */
public class XMLBuilderRunner {

	private Path rootDir = Theme.getRootDir(); //RootDir held by theme
	//this is the changable directory for the theme being generated
	private Path themeDir;


	//these variables will be constant to all the XML's that are being created for version 1.0 so are fields here
	private String author = "BoxTV Design Team";
	private String version  = "1.0";

	private void run() {	
		makeUrbanTheme();
		makePopTheme();
		makeClassicTheme();
	}

	/**
	 * Creates the variables for each element in this theme and gives the theme a name
	 */
	private void makeUrbanTheme() {
		String themeName = "Urban"; //which theme are we doing i.e.: what do we want it called in the xml, and which folder do we want the xml to end up in
		Theme theme = new Theme(themeName);

		//generate the elements
		XMLSerialisable logo = new AnimatedGFXElement(themeName, "Kiss1Bug", "logo", author,version, new CoOrd(0,0), new FrameData(29, 43, 75), new AnimationData(false, false, false, 1) );
		XMLSerialisable chart = new GFXElement(themeName, "UK40BB", "chart", author, version, new CoOrd(400,0)); //this chart is a static image
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "UK40BB", "strap", author,version, new CoOrd(0,0), new FrameData(26, 150, 176), new AnimationData(false, true, false, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "UK40BBNums", "numbers", author,version, new CoOrd(0,0), new FrameData(26, 151, 176), new AnimationData(false, true, true, 1) );
		XMLSerialisable transition = new AnimatedGFXElement(themeName, "UK40BBTransition", "transition", author,version, new CoOrd(0,0), new FrameData(-1, -1, 16), new AnimationData(false, false, false, 1) );

		//set the elements into the theme
		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		//set our path to write to, and write the xml for this theme
		writeThemeXML(theme);
	}

	/**
	 * Creates the variables for each element in this theme and gives the theme a name
	 */
	private void makePopTheme() {
		String themeName = "Pop";
		Theme theme = new Theme(themeName);

		XMLSerialisable logo = new AnimatedGFXElement(themeName, "4M1BugFrames", "logo", author, version, new CoOrd(0,0), new FrameData(51, 79, 126), new AnimationData(false, false, false, 1) );
		XMLSerialisable chart = new AnimatedGFXElement(themeName, "FMTop20Chart", "chart", author, version, new CoOrd(0,0), new FrameData(10, 43, 52), new AnimationData(false, false, false, 1) );
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "PeriscopeFrames", "strap", author,version, new CoOrd(0,0), new FrameData(20, 70, 101), new AnimationData(false, false, false, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "Num", "numbers", author,version, new CoOrd(-680,0), new FrameData(9, 63, 76), new AnimationData(false, true, true, 1) );
		//TODO: we don't have a transition for Pop

		XMLSerialisable transition = null;

		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		writeThemeXML(theme);
	}	


	/**
	 * Creates the variables for each element in this theme and gives the theme a name
	 */
	private void makeClassicTheme() {
		String themeName = "Classic"; //which theme are we doing i.e.: what do we want it called in the xml, and which  folder do we want the xml to end up in?
		Theme theme = new Theme(themeName);

		XMLSerialisable logo = new AnimatedGFXElement(themeName, "QLogo", "logo", author, version, new CoOrd(65,0), new FrameData(84, 84, 86), new AnimationData(true, false, false, 2) );
		XMLSerialisable chart = new AnimatedGFXElement(themeName, "QChartBlack", "chart", author, version, new CoOrd(480,0), new FrameData(47, 47, 48), new AnimationData(true, false, false, 1) );
		XMLSerialisable strap = new AnimatedGFXElement(themeName, "QStrap", "strap", author,version, new CoOrd(-33,230), new FrameData(66, 67, 131), new AnimationData(false, false, false, 1) );
		XMLSerialisable numbers = new AnimatedGFXElement(themeName, "QRedBlockLarger", "numbers", author,version, new CoOrd(24,194), new FrameData(54, 55, 78), new AnimationData(false, false, false, 1) );
		XMLSerialisable transition = new AnimatedGFXElement(themeName, "QTransition", "transition", author,version, new CoOrd(0,200), new FrameData(-1, -1, 32), new AnimationData(false, false, false, 1) );

		theme.setLogo((AnimatedGFXElement) logo);
		theme.setChart((GFXElement) chart);
		theme.setStrap((GFXElement) strap);
		theme.setNumbers((GFXElement) numbers);
		theme.setTransition((GFXElement) transition);

		writeThemeXML(theme);
	}


	/**
	 * Serialises a theme and writes to disk at the root directory in a folder named after the theme
	 * @param theme the theme to serialise
	 */
	private void writeThemeXML(Theme theme) {
		themeDir = Paths.get(rootDir.toString(),theme.getItemName());
		try { XMLWriter.writeXML(themeDir, theme); } 
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * When run, will generate the three default MV-CoDA themes in the default Themes directory
	 * @throws FileNotFoundException //TODO
	 */
	public static void main(String[] args)  {
		XMLBuilderRunner gfx = new XMLBuilderRunner(); //Make an object from this static class so we can run the code to generate the theme 
		gfx.run();
	}

}

