package gstreamer;

import java.io.File;

import org.gstreamer.*;
import org.gstreamer.elements.PlayBin;


public class AudioPlayer {
    public static void main(String[] args) {
        args = Gst.init("AudioPlayer", args);
        PlayBin playbin = new PlayBin("AudioPlayer");
        playbin.setInputFile(new File(args[0]));
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        playbin.setState(State.PLAYING);
        Gst.main();
        playbin.setState(State.NULL);
    }
}