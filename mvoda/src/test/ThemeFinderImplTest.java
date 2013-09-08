package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Test;

import themes.Theme;
import util.ThemeFinder;
import util.ThemeFinderImpl;

/**
 * This test is for the default 3 themes included with MV-CoDA - 0=classic 1=Pop 2=Urban
 * We check we can get the name string from the deserialised source files
 * @author Tony
 *
 */
public class ThemeFinderImplTest {

	@Test public final void testReturnThemes() {
		
		ThemeFinder themeFinder = new ThemeFinderImpl();	
		ArrayList<Theme> themeArray = new ArrayList<>();
				
		try {
			themeArray = themeFinder.returnThemes();
		} catch (IOException e) { //TODO: exception
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) { //TODO:exception
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals( "Testing Classic", "Classic", themeArray.get(0).getItemName() );
		assertEquals( "Testing Pop", "Pop", themeArray.get(1).getItemName() );
		assertEquals( "Testing Urban", "Urban", themeArray.get(2).getItemName() );
		
	}
	
	
	
	@Test public final void testReturnDirectories() throws Exception { //todo: exception
		ThemeFinder themeFinder = new ThemeFinderImpl();
		ArrayList<Path> foundPaths = new ArrayList<>();
		foundPaths = themeFinder.returnDirectories(Theme.getRootDir(), "xml", 2);
		//System.out.println(thePathsItFound);
		
		assertEquals( "Path number two in standard build should be Theme\\Pop\\Pop.xml", "Theme\\Pop\\Pop.xml", foundPaths.get(1).toString() );
		
	}
	

}
