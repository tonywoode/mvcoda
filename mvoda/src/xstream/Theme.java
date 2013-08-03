package xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("Theme")
public class Theme implements XMLSerialisable {
	
	@Getter @Setter private String itemName;
	
	@Getter @Setter private String directory;

	//elements cited in order of importance which is maintained in other classes e.g.: xml generation classes
	
	@Getter @Setter public GFXElement Logo;

	@Getter @Setter public GFXElement Chart;
	
	@Getter @Setter public GFXElement Strap;

	@Getter @Setter public GFXElement Numbers;
	
	@Getter @Setter public GFXElement Transition;

}
