package util;

import java.awt.image.BufferedImage;

import lombok.Getter;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

public class ThumbnailGrabberXuggle implements ThumbnailGrabber {

	public static final double SECONDS_BETWEEN_FRAMES = 10;
	private static int videoStreamIndex = -1;
	private static long lastPtsWrite = Global.NO_PTS;
	public static final long MICRO_SECONDS_BETWEEN_FRAMES = 
			(long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
	
	
	
	@Getter static BufferedImage thumb;

	public BufferedImage grabThumbs(String fileUNC) {
		
		IMediaReader mediaReader = ToolFactory.makeReader(fileUNC);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		mediaReader.addListener(new MediaListenerAdapter() {

		public void onVideoPicture(IVideoPictureEvent event) {
			 if (event.getStreamIndex() != videoStreamIndex) {
	                if (videoStreamIndex == -1)
	                    videoStreamIndex = event.getStreamIndex();
	                // no need to show frames from this video stream
	                else
	                    return;
	            }
			 
			 // if uninitialized, back date mLastPtsWrite to get the very first frame
	            if (lastPtsWrite == Global.NO_PTS)
	                lastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
	            
	            
	            
	            // if it's time to write the next frame
	           // if (event.getTimeStamp() - lastPtsWrite >= 
	             //       MICRO_SECONDS_BETWEEN_FRAMES) {
	            if (event.getTimeStamp() - lastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES)
	            {
	            thumb = event.getImage();
	            
	            
	            lastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
	            return;
	            }
	            
	            
	            
	            
			 
		}
	});
	while (mediaReader.readPacket() == null) ;		
	mediaReader.close();
	
	lastPtsWrite = Global.NO_PTS;
	
	
	return thumb;
	
	}
}