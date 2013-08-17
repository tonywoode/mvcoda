package themes;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ThemeFinderImpl implements ThemeFinder {

	@Override
	public ArrayList<Theme> returnThemes() throws IOException, InterruptedException {
		
		Path rootDir = Theme.getRootDir();
		
		//let's just mock this for now
		String themeName = "Classic";
		Path themeDir = Paths.get(rootDir.toString(),themeName);
		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme1 = (Theme) themeAsSerialisable;
		
		
		String themeName2 = "Pop";
		Path themeDir2 = Paths.get(rootDir.toString(),themeName2);
		XMLSerialisable themeAsSerialisable2 = XMLReader.readXML(themeDir2, themeName2);
		Theme theme2 = (Theme) themeAsSerialisable2;
		
		String themeName3 = "Urban";
		Path themeDir3 = Paths.get(rootDir.toString(),themeName3);
		XMLSerialisable themeAsSerialisable3 = XMLReader.readXML(themeDir3, themeName3);
		Theme theme3 = (Theme) themeAsSerialisable3;
		
		ArrayList<Theme> themeArray = new ArrayList<>();
		themeArray.add(theme1);
		themeArray.add(theme2);
		themeArray.add(theme3);
		
		return themeArray;
		/*final Path rootDir = Theme.getRootDir(); //TODO: should the dir live in theme if the object of this is to decouple?
		Files.walkFileTree(rootDir, new FileVisitor<Path>() {
	        // First (minor) speed up. Compile regular expression pattern only one time.
	        private Pattern pattern = Pattern.compile("^(.xml)");
	        
	        @Override public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
	            boolean matches = pattern.matcher(path.toString()).matches();
	            System.out.println("directory to be visited next "+ path);
	            // TODO: Put here your business logic when matches equals true/false
	            return (matches)? FileVisitResult.CONTINUE:FileVisitResult.SKIP_SUBTREE;
	        }

	        @Override public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
	            boolean matches = pattern.matcher(path.toString()).matches();
	            // TODO: Put here your business logic when matches equals true/false
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
	            exc.printStackTrace();
	            // If the root directory has failed it makes no sense to continue
	            return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE;
	        }
	    });*/
		
		
	}
	
	
	}

