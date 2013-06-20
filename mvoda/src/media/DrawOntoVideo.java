package media;

import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;


public class DrawOntoVideo {

	private static final int VIDEO_STREAM_INDEX = 0;
	private static final int AUDIO_STREAM_INDEX = 1;	
	private static final int VIDEO_STREAM_ID = 0;
	private static final int AUDIO_STREAM_ID = 0;
	private static final ICodec.ID VIDEO_CODEC = ICodec.ID.CODEC_ID_MPEG4;
	private MusicVideo video;
	
	public DrawOntoVideo(final String filename) {
		
		render(filename);
	}
	
	

	private void render(String filename) {
		
		IMediaWriter writer = null;
		try {
			video = new MusicVideo(filename);
			writer = getWriter("../../../MVODAOutputs/ModifyMedia.mp4");
			long frame = 0;
			long lastFrame = video.getNumVidFrames();
			while (video.hasNextPacket()) {
				if (video.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame
							
				IAudioSamples audioSamples = video.getAudioSamples();
				if (audioSamples != null) {
					writer.encodeAudio(AUDIO_STREAM_INDEX, audioSamples);
				}
				BufferedImage videoFrame = video.getVideoFrame();
				if (videoFrame != null) {
					
					System.out.println("at video timestamp: " + video.getFormattedTimestamp());
					/* for (Gauge gauge : gauges) {
						gauge.updateValue(trackPoint, extensions);
						gauge.draw(graphics);
					}
					long timeStamp = (long) (trackPoint.elapsedSeconds * 1000.0);*/
		
					writer.encodeVideo(0, videoFrame, video.getTimeStamp(), TimeUnit.MILLISECONDS);
					if (++frame >= lastFrame) break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) {
				}
			}
			if (video != null) video.close();
		}
	}


	private IMediaWriter getWriter(String filename) {
		IMediaWriter writer = ToolFactory.makeWriter(filename);
		addVideoStreamTo(writer);
		IStreamCoder audioCodec = video.getAudioCoder();
		if (audioCodec != null) {
			addAudioStreamTo(writer, audioCodec);
		}
		return writer;
	}

	private void addVideoStreamTo(IMediaWriter writer) {
		IRational frameRate = IRational.make(video.getFramesPerSecondAsDouble());
		int outputWidth = video.getWidth();
		int outputHeight = video.getHeight();
		writer.addVideoStream(VIDEO_STREAM_INDEX,VIDEO_STREAM_ID,VIDEO_CODEC,frameRate,outputWidth,outputHeight);
	}

	private void addAudioStreamTo(IMediaWriter writer, IStreamCoder audioCodec) {

		int numAudioChannels = audioCodec.getChannels();
		int audioSampleRate = audioCodec.getSampleRate();
		ICodec.ID codecId = audioCodec.getCodecID();

		writer.addAudioStream(AUDIO_STREAM_INDEX,AUDIO_STREAM_ID,codecId,numAudioChannels,audioSampleRate);
	}
}


