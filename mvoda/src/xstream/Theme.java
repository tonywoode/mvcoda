package xstream;

import lombok.Getter;
import lombok.Setter;

public class Theme {
	
	@Getter @Setter private String directory;

	@Getter @Setter public GFXElement Logo;

	@Getter @Setter public GFXElement Strap;

	@Getter @Setter public GFXElement Chart;

	@Getter @Setter public GFXElement Transition;

	@Getter @Setter public GFXElement Numbers;

}
