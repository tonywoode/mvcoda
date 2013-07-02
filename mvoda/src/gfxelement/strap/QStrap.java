package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class QStrap extends Strap {


	public QStrap(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QStrap";
}