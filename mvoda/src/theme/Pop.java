package theme;

import gfxelement.GFXElement;
import gfxelement.chart.FMTop20Chart;
import gfxelement.logo.FourMusic1;
import gfxelement.strap.Periscope;
import lombok.Getter;

public class Pop extends Theme {

@Getter private String directory  = super.getDirectory() + this.getClass().getSimpleName() + "/";
//http://stackoverflow.com/questions/6271417/java-get-the-current-class-name

@Getter private GFXElement logo = new FourMusic1(this);

@Getter private GFXElement strap = new Periscope(this);

@Getter private GFXElement chart = new FMTop20Chart(this);


public GFXElement getTransition() {return null;}



}
