package gfxelement.numbers;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;
import gfxelement.GFXElement;

public abstract class Numbers extends GFXElement {

	@Getter private Theme theme;
	@Getter private String directory;
	@Getter @Setter private int num;
	
	public Numbers(Theme theme, int num) {
		this.theme = theme;
		setNum(num);
		directory = theme.getDirectory() + "Numbers/";
	}
}