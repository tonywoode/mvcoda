package theme;

import gfxelement.GFXElement;
import gfxelement.chart.FMTop20Chart;
import gfxelement.logo.FourMusic1;
import gfxelement.numbers.TWFM1;
import gfxelement.strap.Periscope;
import lombok.Getter;
import lombok.Setter;

public class Pop extends Theme {
	
	@Setter static private int num; //TODO: very silly

@Getter private String directory  = super.getDirectory() + this.getClass().getSimpleName() + "/";
//http://stackoverflow.com/questions/6271417/java-get-the-current-class-name

@Getter private GFXElement logo = new FourMusic1(this);

@Getter private GFXElement strap = new Periscope(this);

@Getter private GFXElement chart = new FMTop20Chart(this);

@Getter private GFXElement numbers = new TWFM1(this, num);


public GFXElement getTransition() {return null;}




}
