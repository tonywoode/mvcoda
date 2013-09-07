package test;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;



import themes.Theme;
import util.FileUtil;

public class FileUtilTest {

	@Test
	public final void testGetFiletype() {
		
	
		assertEquals("Testing filetype search", ".mp4", FileUtil.getFiletype("//testing//test.mp4"));
	}
	
	@Test
	public final void testCountFoldersInFolder() {
		Path path = Paths.get(Theme.getRootDir().toString(), "Urban/Numbers");
		assertEquals("Testing numbers in folder - there are 20 numbers in Urban Numbers folder", 20, FileUtil.countFoldersInFolder(path));
	}

}
