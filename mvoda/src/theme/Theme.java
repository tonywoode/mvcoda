package theme;

import gfxelement.GFXElement;
import lombok.Getter;

public abstract class Theme {
	
@Getter private String directory  = "Theme/";

public abstract GFXElement getLogo();

public abstract GFXElement getStrap();


}
