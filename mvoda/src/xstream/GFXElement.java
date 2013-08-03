package xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("GFXElement")
public class GFXElement implements XMLSerialisable {
	
	//@Getter @Setter private String themeName;
	@Getter @Setter private String itemName;
	@Getter @Setter private String rootPath;
	@Getter @Setter private String author;
	//@Getter @Setter private String type;
	@Getter @Setter private String version;
	@Getter @Setter public CoOrd coOrd;

	public GFXElement(String itemName, String rootPath, String author, String version, CoOrd coOrd) {
		this.itemName = itemName;
		this.rootPath = rootPath;
		this.author = author;
		this.version = version;
		this.coOrd = coOrd;
	}
	
	//have these getters so we don't have to call clasA.classB.getxoffset()
	public int getxOffsetSD() {	return coOrd.getXOffsetSD(); }
	public int getyOffsetSD() {	return coOrd.getYOffsetSD(); }
	
	@Override
	public String toString() {
		return itemName;		
	}
	
	

}

