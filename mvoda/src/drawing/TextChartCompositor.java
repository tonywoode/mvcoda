package drawing;

import java.awt.Font;

import lombok.Getter;
import lombok.Setter;

/**
 * Some Chart names need to be rendered in a different font than the other onscreen text. Rather than have the user specify these font characteristics,
 * for this version of MV-CoDA we simply halve the usual font size and allow this custom text compositor to be called for these forms
 * @author tony
 *
 */
public class TextChartCompositor extends TextCompositor{

	@Getter @Setter static int chartFontSize = 12; //set to half the default font size

	public TextChartCompositor(String text, int textXPos, int textYPos) {
		super(text, textXPos, textYPos);
		if (super.getFontSize() > 0 ) { chartFontSize = super.getFontSize() / 2; }
		else { throw new IllegalArgumentException("Font Size is set to zero"); }
	}

	/**
	 * Overrides the sentFont method of TextCompositor providing a double-size number for chart numbers
	 */
	@Override protected void setFont() { textFont = new Font(fontName, 1, chartFontSize); }

}
