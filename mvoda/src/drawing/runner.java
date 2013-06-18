package drawing;

import java.io.IOException;



public class runner {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String input = "../../../Repo/mvoda/mvoda/temp/bru.png";
		String overlay = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";
		String output = "../../../MVODAOutputs/CompositedOutput.png";
		OverlayFrames overlayframes = new OverlayFrames(input, overlay, output);

	}

}
