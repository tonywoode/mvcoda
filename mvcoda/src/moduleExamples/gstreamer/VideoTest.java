package moduleExamples.gstreamer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.gstreamer.*;
import org.gstreamer.swing.VideoComponent;

public class VideoTest {
    public VideoTest() {
    }
    private static Pipeline pipe;
    public static void main(String[] args) {
        args = Gst.init("VideoTest", args);
        pipe = new Pipeline("VideoTest");
        final Element videosrc = ElementFactory.make("videotestsrc", "source");
        final Element videofilter = ElementFactory.make("capsfilter", "filter");
        videofilter.setCaps(Caps.fromString("video/x-raw-yuv, width=720, height=576"
                + ", bpp=32, depth=32, framerate=25/1"));
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                VideoComponent videoComponent = new VideoComponent();
                Element videosink = videoComponent.getElement();
                pipe.addMany(videosrc, videofilter, videosink);
                Element.linkMany(videosrc, videofilter, videosink);
                
                // Now create a JFrame to display the video output
                JFrame frame = new JFrame("Swing Video Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(videoComponent, BorderLayout.CENTER);
                videoComponent.setPreferredSize(new Dimension(720, 576));
                frame.pack();
                frame.setVisible(true);
                
                // Start the pipeline processing
                pipe.setState(State.PLAYING);
            }
        });
    }
}