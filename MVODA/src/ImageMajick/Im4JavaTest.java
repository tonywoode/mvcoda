package ImageMajick;

import java.io.IOException;
import java.util.ArrayList;

import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

public class Im4JavaTest {

	/**
	 * @param args
	 * @throws IM4JavaException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args)  {
		
		
		//create what should be the GLOBAL searchpath (otherwise set IM4JAVA_TOOLPATH in windows path)
		String myPath="C://Users//tony//CODE//Software//ImageMagick-6.8.5-Q16";
		ProcessStarter.setGlobalSearchPath(myPath);
		
		
		
		// create command
		ConvertCmd cmd = new ConvertCmd();

		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage("C://Users//tony//CODE//Software//ImageMagick-6.8.5-Q16//images//bluebells_clipped.jpg");	
		op.alpha("copy"); 
		
		//op.resize(200,200);
		op.addImage("C://Users//tony//CODE//Software//ImageMagick-6.8.5-Q16//images//bluebells_clipped_ALPHA.jpg");
		
		//it works!
		
		/*
		 * Options for alpha are:
		 * Activate or On 	Enable the image's transparency channel. Note normally Set should be used instead of this, unless you specifically need to preserve existing (but specifically turned Off) transparency channel.
		 * Deactivate or Off 	Disables the image's transparency channel. Does not delete or change the existing data, just turns off the use of that data.
		 * Set 	Activates the alpha/matte channel. If it was previously turned off then it also resets the channel to opaque. If the image already had the alpha channel turned on, it will have no effect.
		 * Opaque 	Enables the alpha/matte channel and forces it to be fully opaque.
		 * Transparent 	Activates the alpha/matte channel and forces it to be fully transparent. This effectively creates a fully transparent image the same size as the original and with all its original RGB data still intact, but fully transparent.
		 * Extract 	Copies the alpha channel values into all the color channels and turns 'Off' the the image's transparency, so as to generate a gray-scale mask of the image's shape. The alpha channel data is left intact just deactivated. This is the inverse of 'Copy'.
		 * Copy 	Turns 'On' the alpha/matte channel, then copies the gray-scale intensity of the image, into the alpha channel, converting a gray-scale mask into a transparent shaped mask ready to be colored appropriately. The color channels are not modified.
		 * Shape 	As per 'Copy' but also colors the resulting shape mask with the current background color. That is the RGB color channels is replaced, with appropriate alpha shape.
		 * Remove 	Composite the image over the background color.
		 * Background 	Set any fully-transparent pixel to the background color, while leaving it fully-transparent. This can make some image file formats, such as PNG, smaller as the RGB values of transparent pixels are more uniform, and thus can compress better. 
		 */

		// execute the operation
		try {
		cmd.run(op);
		
	} catch (CommandException ce) {
        ce.printStackTrace();
        ArrayList<String> cmdError = ce.getErrorText();
        for (String line:cmdError) {
          System.err.println(line);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }


	}

}
