package themes;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.regex.Pattern;

public class ThemeFinderImpl implements ThemeFinder {

	
	@Override
	public ArrayList<Theme> returnThemes() throws IOException, InterruptedException {
		
		Path rootDir = Theme.getRootDir();
		
		ArrayList<Path> pathArray = new ArrayList<>();
		ArrayList<Theme> themeArray = new ArrayList<>();
		
		try {
			pathArray = returnDirectories();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for ( int i=0; i<pathArray.size(); i++) {
			Path themeDir = pathArray.get(i);
			XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
			Theme theme = (Theme) themeAsSerialisable;
			theme.setIndex(i);
		}
	}
	
	/*@Override
	public ArrayList<Theme> returnThemes() throws IOException, InterruptedException {
		
		Path rootDir = Theme.getRootDir();
		
		//let's just mock this for now
		String themeName = "Classic";
		Path themeDir = Paths.get(rootDir.toString(),themeName);
		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme1 = (Theme) themeAsSerialisable;
		theme1.setIndex(0); //TODO: this has no effect see encoder
		
		
		String themeName2 = "Pop";
		Path themeDir2 = Paths.get(rootDir.toString(),themeName2);
		XMLSerialisable themeAsSerialisable2 = XMLReader.readXML(themeDir2, themeName2);
		Theme theme2 = (Theme) themeAsSerialisable2;
		theme2.setIndex(1); //TODO: this has no effect see encoder
		
		String themeName3 = "Urban";
		Path themeDir3 = Paths.get(rootDir.toString(),themeName3);
		XMLSerialisable themeAsSerialisable3 = XMLReader.readXML(themeDir3, themeName3);
		Theme theme3 = (Theme) themeAsSerialisable3;
		theme3.setIndex(2); //TODO: this has no effect see encoder
		
		ArrayList<Theme> themeArray = new ArrayList<>();
		themeArray.add(theme1);
		themeArray.add(theme2);
		themeArray.add(theme3);
		
		return themeArray;
		
	}*/
	
	
	
	/**
	 * I give you a string, you tell me what folder are in it
	 * @return
	 */
	public static ArrayList<Path> returnDirectories() throws Exception{
		
		final ArrayList<Path> pathArray = new ArrayList<>();
		
		
		final Path rootDir = Theme.getRootDir(); //TODO: should the dir live in theme if the object of this is to decouple?
		//walk file tree with NO options BUT set but a max depth of 2 levels - so that means any xml further than in Theme\themefolder\*.xml won't be valid
		//however, this means any xml in root or not in a subdir folder could still be picked up - TODO
		Files.walkFileTree(rootDir, EnumSet.noneOf(FileVisitOption.class), 2, new FileVisitor<Path>() {
			
	        //Compile regular expression pattern only one time - see http://stackoverflow.com/questions/2534632/list-all-files-from-a-directory-recursively-with-java
	        private Pattern pattern = Pattern.compile(".*(.xml)");
	        
	        @Override public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
	            //boolean matches = pattern.matcher(path.toString()).matches();
	            //System.out.println("directory to be visited next "+ path);
	           
	            
	            //return (matches)? FileVisitResult.CONTINUE:FileVisitResult.SKIP_SUBTREE;
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
	            boolean matches = pattern.matcher(path.toString().toLowerCase()).matches();  //note the lower case, may need to undo this later  
	            if (matches) { System.out.println("yup that matches its " + path);
	            pathArray.add(path);
	            }
	            
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
	            return FileVisitResult.CONTINUE;
	        }

	        @Override public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
	            e.printStackTrace();
	            //Do not contune if root dir has failed TODO: this to gui....
	            return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE;
	        }
	    });
		return pathArray;
		
	}
	
	
	public static void main(String[] args) throws Exception {
		//returnDirectories();
		ArrayList<Path> thePathsItFound = new ArrayList<>();
		thePathsItFound=returnDirectories();
		System.out.println(thePathsItFound);
	
		//ok great we can get the path, but you need to save the PATH in a THEME - a theme needs to know its own path eh!
		//testing again
	}
	
	
	}

