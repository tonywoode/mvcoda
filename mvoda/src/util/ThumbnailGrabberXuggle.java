package util;

import java.awt.image.BufferedImage;

import view.MediaOpenException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import lombok.Getter;
import media.MusicVideo;
import media.xuggle.MusicVideoXuggle;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

public class ThumbnailGrabberXuggle {

	public static final double SECONDS_BETWEEN_FRAMES = 0;
	//private static int videoStreamIndex = -1;
	//private static long lastPtsWrite = 1;
	//public static final long MICRO_SECONDS_BETWEEN_FRAMES = 
			//(long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
	
	
	
	@Getter BufferedImage thumb;

	public void grabThumbs(String fileUNC)  {
		IMediaReader mediaReader = ToolFactory.makeReader(fileUNC);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		mediaReader.addListener(new ImageSnapListener());
		//MusicVideo video = new MusicVideoXuggle(fileUNC);
		//while ( video.getContainerDuration() < 1000000);
		while (mediaReader.readPacket() == null) ;		
	}

	private class ImageSnapListener extends MediaListenerAdapter {

		public void onVideoPicture(IVideoPictureEvent event) {
			 
			 // if uninitialized, back date mLastPtsWrite to get the very first frame
	            //if (lastPtsWrite == Global.NO_PTS)
	               // lastPtsWrite = 1;
	            
	            
	            
	            // if it's time to write the next frame
	           // if (event.getTimeStamp() - lastPtsWrite >= 
	             //       MICRO_SECONDS_BETWEEN_FRAMES) {
	            //if (event.getTimeStamp() == 1000000){	
	            thumb = event.getImage(); 
	            //}
	            
	            //lastPtsWrite = 1;
	            
	           // }
	            
	            
	            
	            
			 
		}
	}
}