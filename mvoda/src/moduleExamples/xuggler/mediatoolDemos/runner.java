package moduleExamples.xuggler.mediatoolDemos;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;


public class runner {

	/**
	 * Written up from here: http://www.youtube.com/watch?v=JBXyE4_3ERI&noredirect=1
	 * But he didn't give the variables. It's not showing the video......
	 * @param args
	 */
	public static void main(String[] args) {

		int width = 800;
		int height = 400;
		int channelCount = 2;
		int sampleRate = 44100;
		int ballCount = 2;
		int sampleCount = 1000000;
		int duration = 10000;
		int totalSampleCount = 441;
		int frameRate = 15;
		int nextFrameTime = 0;
		TimeUnit MICROSECONDS = TimeUnit.MICROSECONDS;


		final IMediaWriter writer = ToolFactory.makeWriter("../../../MVODAOutputs/myballs.mov");
		writer.addVideoStream(0,0, width, height);
		writer.addAudioStream(1, 1, channelCount, sampleRate);

		Balls balls = new MovingBalls(ballCount, width, height, sampleCount);

		for (long clock = 0; clock < duration; clock = IAudioSamples.samplesToDefaultPts(totalSampleCount, sampleRate)) {
			while (nextFrameTime < clock) {
				BufferedImage frame = balls.getVideoFrame(frameRate);
				writer.encodeVideo(0,  frame, nextFrameTime, MICROSECONDS);
				nextFrameTime += frameRate;
			}
		}

		short[] samples = balls.getAudioFrame(sampleRate);
		writer.encodeAudio(1,  samples);
		totalSampleCount += sampleCount;

		writer.close();
	}

}
