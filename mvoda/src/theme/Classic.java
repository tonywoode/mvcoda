package theme;

import gfxelement.GFXElement;
import gfxelement.chart.QChartHolder;
import gfxelement.logo.QLogo;
import gfxelement.strap.QStrap;
import gfxelement.transition.QTransition;
import lombok.Getter;

public class Classic extends Theme {

@Getter private String directory  = super.getDirectory() + this.getClass().getSimpleName() + "/";
//http://stackoverflow.com/questions/6271417/java-get-the-current-class-name

@Getter private GFXElement logo = new QLogo(this);

@Getter private GFXElement strap = new QStrap(this);

@Getter private GFXElement chart = new QChartHolder(this);

@Getter private GFXElement transition = new QTransition(this);

@Override
public GFXElement getNumbers() {return null;}




}
