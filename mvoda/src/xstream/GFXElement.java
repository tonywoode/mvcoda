package xstream;

import lombok.Getter;
import lombok.Setter;

public class GFXElement implements XMLSerialisable {
	
	//@Getter @Setter private String themeName;
	@Getter @Setter private String itemName;
	@Getter @Setter private String rootPath;
	@Getter @Setter private String author;
	//@Getter @Setter private String type;
	@Getter @Setter private String version;
	
	
	@Getter @Setter public CoOrd coOrd;


	@Override
	public String toString() {
		return itemName;		
	}
	
	

}

