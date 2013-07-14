package theme;

import gfxelement.GFXElement;
import gfxelement.chart.QChartBlack;
import gfxelement.logo.QLogo;
import gfxelement.numbers.QRedBlock;
import gfxelement.strap.QStrap;
import gfxelement.transition.QTransition;
import lombok.Getter;

public class Classic extends Theme {

@Getter private String directory  = super.getDirectory() + this.getClass().getSimpleName() + "/";
//http://stackoverflow.com/questions/6271417/java-get-the-current-class-name

@Getter private GFXElement logo = new QLogo(this);

@Getter private GFXElement strap = new QStrap(this);

@Getter private GFXElement chart = new QChartBlack(this);

@Getter private GFXElement transition = new QTransition(this);

@Getter private GFXElement numbers = new QRedBlock(this, 0);





}
