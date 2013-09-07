package themes;

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
	
	@Getter private String itemName;
	
	//TODO: well the below didn't work very well now did it.....I think it can be removed atm
	@XStreamOmitField @Getter private Path themeDir;// = Paths.get(rootDir.toString(),getItemName());
	
	
	@Getter @Setter private String directory;

	//elements cited in order of importance which is maintained in other classes e.g.: xml generation classes
	
	@Getter @Setter public GFXElement logo;

	@Getter @Setter public GFXElement chart;
	
	@Getter @Setter public GFXElement strap;

	@Getter @Setter public GFXElement numbers;
	
	@Getter @Setter public GFXElement transition;
	
	@XStreamOmitField @Getter @Setter public int index; //TODO: omit means set this only after instantiating with a read
	
	public Theme(String itemName) { this.itemName = itemName; }
	
	@Override public String toString() { return itemName; }
	
	
	
	/*public Theme(String itemName) { //TODO: this never gets called because the xml doesn't call new
		
		this.itemName = itemName;
		this.themeDir = Paths.get(rootDir.toString(),itemName);
		System.out.println("Right now rootDir is " + rootDir + " and themeDir is " + themeDir);
		XMLSerialisable themeAsSerialisable = XMLReader.readXML(getThemeDir(), getItemName());
		Theme theme = (Theme) themeAsSerialisable;
		this.Logo = theme.getLogo();
		this.Chart = theme.getChart();
		this.Strap = theme.getStrap();
		this.Numbers = theme.getNumbers();
		this.Transition = theme.getTransition();
	}*/

}
