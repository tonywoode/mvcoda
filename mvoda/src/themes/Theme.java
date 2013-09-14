package themes;

import java.nio.file.Path;
import java.nio.file.Paths;

import util.XMLSerialisable;

import lombok.Getter;
import lombok.Setter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * A theme collects a series of GFXElements that were intended to be displayed together i.e.: for a particular chart.
 * The element types held are described in {@link GFXElement}
 * In addition the theme itself has a name, and a root directory which are held in this class
 * Themes are serialisable so end up with the theme elements on disk
 * @author tony
 *
 */
@XStreamAlias("Theme") public class Theme implements XMLSerialisable {
	
	/*
	 * 
	 * This is the root directory for all the XML's. It is not expected to change or be alterable
	 */
	@XStreamOmitField @Getter private static final Path rootDir = Paths.get("Theme");
	
	
	
	@XStreamOmitField @Getter private Path themeDir;
	
	@XStreamOmitField @Getter @Setter private int index; //Note: to omit this field means we must always remember to set this AFTER instantiating with a read
	
	@Getter private String itemName;
	
	@Getter @Setter private String directory;

	//elements cited in order of importance which is maintained in other classes e.g.: xml generation classes
	
	@Getter @Setter public GFXElement logo;

	@Getter @Setter public GFXElement chart;
	
	@Getter @Setter public GFXElement strap;

	@Getter @Setter public GFXElement numbers;
	
	@Getter @Setter public GFXElement transition;
	
	
	public Theme(String itemName) { this.itemName = itemName; }
	
	
	/**
	 * Returns the item name of the theme. This is currently used heavily by JAVAFX's combo box, which contains themes, not strings, so uses this to display name
	 */
	@Override public String toString() { return itemName; } //this will get used by the Theme select combo box in the JavaFX GUI
	

}
