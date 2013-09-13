package drawing;

import java.awt.Font;

import lombok.Getter;
import lombok.Setter;

/**
 * Some Chart numbers need to be rendered in a bigger font than the other onscreen text. Rather than have the user specify this fontsize,
 * for this version of MV-CoDA, we simply double the usual font number and allow this custom text compositor to be called for these forms
 * @author tony
 *
 */
public class TextNumberCompositor extends TextCompositor{

	@Getter @Setter static int numberFontSize = 48; //set to double the default font size

	public TextNumberCompositor(String text, int textXPos, int textYPos) {
		super(text, textXPos, textYPos);
		numberFontSize = super.getFontSize() * 2;
	}

	/**
	 * Overrides the sentFont method of TextCompositor providing a double-size number for chart numbers
	 */
	@Override protected void setFont() {
		textFont = new Font(fontName, 1, numberFontSize);
	}

}
