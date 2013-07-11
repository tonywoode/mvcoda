package theme;

import gfxelement.GFXElement;
import gfxelement.chart.UK40BBChart;
import gfxelement.logo.FourMusic1;
import gfxelement.logo.KissBug1;
import gfxelement.numbers.TWFM;
import gfxelement.numbers.UKBB40NumHolder;
import gfxelement.strap.Periscope;
import gfxelement.strap.UK40BB;
import gfxelement.transition.UK40BBTransition;
import lombok.Getter;
import lombok.Setter;

public class Urban extends Theme {

@Getter private String directory  = super.getDirectory() + this.getClass().getSimpleName() + "/";
//http://stackoverflow.com/questions/6271417/java-get-the-current-class-name

@Getter private GFXElement logo = new KissBug1(this);

@Getter private GFXElement strap = new UK40BB(this);

@Getter private GFXElement transition = new UK40BBTransition(this);

@Getter private GFXElement chart = new UK40BBChart(this);

@Getter private GFXElement numbers = new UKBB40NumHolder(this, 0); //TODO: I'm passing a zero because there are no numbers....


}
