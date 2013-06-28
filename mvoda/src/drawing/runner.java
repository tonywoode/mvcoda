package drawing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;



public class runner {


	static String backFile = "../../../MVODAInputs/bru.png";
	static String overlayFile = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";
	String outputFile = "../../../MVODAOutputs/CompositedOutput.png";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String dir = "Theme/Pop/Logo/4MLogoFrames";
		File file = new File(dir);  
		File[] files = file.listFiles();
		String i = files[10].toString();
				System.out.println(i);
				/*Scanner in = new Scanner(i);
				in.useDelimiter("");
				while (in.hasNext())
				{
					char ch = in.next().charAt(0);
					
				}
				int j = 0;
				while (!Character.isDigit(i.charAt(j))) {
					j++;
				}
				System.out.println(i.substring(15));*/
		
				String path = i;
				String base = dir;
				//first we need to get the relative path - we do this by effectively masking
				String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
				System.out.println(relative);
				//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
				//so we revers
				String reverse = new StringBuilder(relative).reverse().toString();
				System.out.println(reverse);
				//then we go back through our reversed string till we find a digit
				int j = 0;
				while (!Character.isDigit(reverse.charAt(j))) {
					j++;
				}
				System.out.println(j);
				//then we remove the characters up to the first digit
				String filenameRemoved = reverse.substring(j);
				System.out.println(filenameRemoved);
				//then we continue through the numbers left until we find the first character
				int k = 0;
				while (Character.isDigit(filenameRemoved.charAt(k))) {
					k++;
				}
				//then we remove anything past the last number
				String numberExtracted = filenameRemoved.substring(0,k);
				System.out.println(numberExtracted);
				//then we've got the digits out. We need to reverse the number back again
				String reverseBack = new StringBuilder(numberExtracted).reverse().toString();
				System.out.println(reverseBack);
				
				
				//now i think you can use Append() method in String to produce the file as you know all the other paths, but actually
				//you probably want to get the filetype from the above in case its some other kind of alpha
				
		/*
				 File file = new File(dir);  
				 File[] files = file.listFiles();				 
				 Arrays.sort(files, new NumberedFileComparator());
				 for (int fileInList = 0; fileInList < files.length; fileInList++)  
				 {  
				     System.out.println(files[fileInList].toString());  
				 } 
		
	*/
		
		
		
		
		
		
		
		
		//ImageCompositor overlayframes = new ImageCompositor(backFile, overlayFile, outputFile);
		//overlayframes.overlay();
		
		//testOverlayImage();
	}


	static void testOverlayImage() throws IOException {
		//great, now let's PASS you in the frames
		Image bk = ImageIO.read(new File(backFile));
		BufferedImage back = (BufferedImage) bk;

		Image over = ImageIO.read(new File(overlayFile));
		BufferedImage overlay = (BufferedImage) over;

		ImageCompositor overlayframes2 = new ImageCompositor(back, overlay);

		BufferedImage composite = overlayframes2.overlayImage();

		//hmm nothing wrong with the image viewer I just made, so why doesn't the media runner show me a video frame eh? See:
		ShowImageInFrame gui = new ShowImageInFrame(composite);
		//gui.createAndShowGui();
	}

	



}
