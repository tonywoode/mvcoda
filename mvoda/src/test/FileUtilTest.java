package test;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileUtil;

public class FileUtilTest {

	@Test
	public final void testGetFiletype() {
		
	
		assertEquals("Testing filetype search", ".mp4", FileUtil.getFiletype("//testing//test.mp4"));
	}

}
