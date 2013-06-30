package theme;

import gfxelement.GFXElement;
import gfxelement.logo.FourMusic1;
import lombok.Getter;

public class Pop extends Theme {

@Getter private String directory  = super.getDirectory() + "Pop/";

@Getter private GFXElement logo = new FourMusic1(this);

}
