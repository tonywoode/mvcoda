package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import themes.Theme;
import themes.ThemeFinder;
import themes.ThemeFinderImpl;
import util.FileUtil;

/**
 * This test is for the default 3 themes included with MV-CoDA - 0=classic 1=Pop 2=Urban
 * We check we can get the name string from the deserialised source files
 * @author Tony
 *
 */
public class ThemeFinderImplTest {

	@Test
	public final void testReturnThemes() {
		
		ThemeFinder themeFinder = new ThemeFinderImpl();	
		ArrayList<Theme> themeArray = new ArrayList<>();
				
		try {
			themeArray = themeFinder.returnThemes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals( "Testing Classic", "Classic", themeArray.get(0).getItemName() );
		assertEquals( "Testing Pop", "Pop", themeArray.get(1).getItemName() );
		assertEquals( "Testing Urban", "Urban", themeArray.get(2).getItemName() );
		
	}

}
