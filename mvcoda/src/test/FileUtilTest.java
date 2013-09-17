package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;



import themes.Theme;
import util.FileUtil;

/**
 * JUnit tests for the static features of the FileUtil class
 * @author tony
 *
 */
public class FileUtilTest {

	@Test public final void testGetFiletype() { assertEquals("Testing filetype search", ".mp4", FileUtil.getFiletype("//testing//test.mp4")); }
	
	@Test public final void testCountFoldersInFolder() {
		Path path = Paths.get(Theme.getRootDir().toString(), "Urban/Numbers");
		try {
			assertEquals("Testing numbers in folder - there are 20 numbers in Urban Numbers folder", 20, FileUtil.countFoldersInFolder(path));
		} catch (IOException e) { e.printStackTrace(); }
	}

}
