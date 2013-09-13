package drawing;

import java.awt.Font;

import lombok.Getter;
import lombok.Setter;

/**
 * Some Chart names need to be rendered in a smaller font than the other onscreen text. Rather than have the user specify this fontsize,
 * we halve the usual font number and allow this custom text compositor to be called for these forms of chart
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
	@Override protected void setFont() {
		textFont = new Font(fontName, 1, chartFontSize);
	}

}
