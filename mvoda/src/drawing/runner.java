package drawing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class runner {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String backFile = "../../../MVODAInputs/bru.png";
		String overlayFile = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";
		String outputFile = "../../../MVODAOutputs/CompositedOutput.png";
		//ImageCompositor overlayframes = new ImageCompositor(backFile, overlayFile, outputFile);
		//overlayframes.overlay();


		//great, now let's PASS you in the frames
		Image bk = ImageIO.read(new File(backFile));
		BufferedImage back = (BufferedImage) bk;

		Image over = ImageIO.read(new File(overlayFile));
		BufferedImage overlay = (BufferedImage) over;

		ImageCompositor overlayframes2 = new ImageCompositor(back, overlay);

		BufferedImage composite = overlayframes2.overlay();

		//hmm nothing wrong with the image viewer I just made, so why doesn't the media runner show me a video frame eh? See:
		ShowImageInFrame gui = new ShowImageInFrame(composite);
		//gui.createAndShowGui();



	}

}
