package xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

@XStreamAlias("Theme")
public class Theme implements XMLSerialisable {
	
	
	//This is the root directory for all the XML's. It is not expected to change or be alterable
	@XStreamOmitField @Getter private static final Path rootDir = Paths.get("Theme");
	
	@XStreamOmitField private Path themeDir;
	
	@XStreamOmitField private String themeName;
	
	@Getter @Setter private String itemName;
	
	//@Getter @Setter private Path directory;

	//elements cited in order of importance which is maintained in other classes e.g.: xml generation classes
	
	@Getter @Setter public GFXElement Logo;

	@Getter @Setter public GFXElement Chart;
	
	@Getter @Setter public GFXElement Strap;

	@Getter @Setter public GFXElement Numbers;
	
	@Getter @Setter public GFXElement Transition;
	
	public Theme(String themeName) { //TODO: this never gets called because the xml doesn't call new
		this.themeName = themeName;
		themeDir = Paths.get(rootDir.toString(),themeName);
	}

}
