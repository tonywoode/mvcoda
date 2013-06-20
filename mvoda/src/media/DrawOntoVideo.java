package media;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;


public class DrawOntoVideo {

	private static final int VIDEO_STREAM_INDEX = 0;
	private static final int AUDIO_STREAM_INDEX = 1;	//TODO: why are these things all hard coded?
	private static final int VIDEO_STREAM_ID = 0;
	private static final int AUDIO_STREAM_ID = 0;
	private static final ICodec.ID VIDEO_CODEC = ICodec.ID.CODEC_ID_MPEG4;
	private MusicVideo video;
	private String outFilename;

	public DrawOntoVideo(final String filename,String outFilename) {
		this.outFilename = outFilename;
		render(filename);
	}

	private void render(String filename) {

		IMediaWriter writer = null;
		try {
			video = new MusicVideo(filename);
			writer = getWriter(outFilename);
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
					//ShowImageInFrame im = new ShowImageInFrame(videoFrame); //yup we are getting images....
					writer.encodeVideo(0, videoFrame, video.getTimeStamp(), TimeUnit.MILLISECONDS);
					if ((frame +1) >= lastFrame) break;
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
		if (audioCodec != null) {addAudioStreamTo(writer, audioCodec);}
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


