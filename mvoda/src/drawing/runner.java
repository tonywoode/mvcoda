package drawing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;



public class runner {


	static String backFile = "../../../MVODAInputs/bru.png";
	static String overlayFile = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";
	String outputFile = "../../../MVODAOutputs/CompositedOutput.png";
	static String filetype = "";
	static String filePrefix = "";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		/*String dir = "Theme/Pop/Logo/4MLogoFrames";
		File file = new File(dir);  
		File[] files = file.listFiles();
		ArrayList<String> fileNumbers = new ArrayList<>();
		
		for (int g = 0; g < files.length; g++) {
		
		String i = files[g].toString();
				System.out.println(i);
				String path = i;
				String base = dir;
				//first we need to get the relative path - we do this by effectively masking
				String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
				System.out.print("relative filename is: " + relative);
				//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
				String reverse = new StringBuilder(relative).reverse().toString(); //so we reverse
				System.out.println("reversed relative filename is: " + reverse);
				int j = 0;
				while (!Character.isDigit(reverse.charAt(j))) { j++; }	//then we go back through our reversed string till we find a digit
				System.out.println("The first digit is at position: " + j); //TODO: hope you never get a filetype with a digit in - need to look for a period really
				String filetypeReversed = reverse.substring(0, j);
				//we digress here to save out the filetype
				System.out.println("Here's the reversed filetype: " + filetypeReversed);
				filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
				System.out.println("Filetype is: " + filetype);
				//carry on with getting the number
				String filetypeRemoved = reverse.substring(j);	//then we remove the characters up to the first digit
				System.out.println("Chars removed up to first digit is : " + filetypeRemoved);	
				int k = 0;
				while (Character.isDigit(filetypeRemoved.charAt(k))) { k++; }	//then we continue through the numbers left until we find the first character
				System.out.println("The digits continue up to position:" + k);
				//digress again to get the file prefix name ie: what's before the digit
				String filePrefixReverse = filetypeRemoved.substring(k);
				System.out.println("File Prefix in reverse is: " + filePrefixReverse);
				filePrefix = new StringBuilder(filePrefixReverse).reverse().toString();
				System.out.println("So here's the file prefix:" + filePrefix);
				String numberExtracted = filetypeRemoved.substring(0,k); //then we remove anything past the last number
				System.out.println(numberExtracted);	
				String reverseBack = new StringBuilder(numberExtracted).reverse().toString(); //then we've got the digits out. We need to reverse the number back again
				System.out.println(reverseBack);
				fileNumbers.add(reverseBack);
				
		}
				
				
		//now let's use the comparator to sort the arrayList - we'll just write back to the same arraylist TODO: good idea?
			
			Collections.sort(fileNumbers, new NumberedFileComparator());
			for (int i = 0; i < fileNumbers.size(); i++) {
			System.out.println(fileNumbers.get(i));
			}
			
			
			//now we can reconstitute the file list IN ORDER
			
			for (int l = 0; l < fileNumbers.size(); l++) {
				fileNumbers.set(l, dir + "/" + filePrefix + fileNumbers.get(l) + filetype);
			}
		
			//and there we go 
			System.out.println(fileNumbers);*/
				
	

		
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
