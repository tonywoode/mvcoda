package gstreamer;

import java.io.File;

import org.gstreamer.*;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.elements.PlayBin2;


public class AudioPlayer {
    public static void main(String[] args) {
        args = Gst.init("AudioPlayer", args);
        PlayBin2 playbin = new PlayBin2("AudioPlayer");
        playbin.setInputFile(new File("../../../MVODAInputs/Love/BrunoMarsJustTheWay.avi"));
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        playbin.setState(State.PLAYING);
        Gst.main();
        playbin.setState(State.NULL);
    }
}