package moduleExamples.gstreamer;

import org.gstreamer.*;
import com.sun.jna.*;


public class SimplePipeline {
    public static void main(String[] args) {
    	
    	
    	//System.load("C:/moduleExamples.gstreamer-sdk/0.10/x86_64/bin/libgstreamer-0.10-0.dll");
    	
    	//System.setProperty("jna.library.path", "C:/moduleExamples.gstreamer-sdk/0.10/x86_64/bin");
    	//System.setProperty("jna.library.path", "C:/moduleExamples.gstreamer-sdk/0.10/x86_64/lib"); 
    	//System.out.println( System.getProperty("jna.library.path"));
    	
        args = Gst.init("SimplePipeline", args);
        Pipeline pipe = new Pipeline("SimplePipeline");
        Element src = ElementFactory.make("fakesrc", "Source");
        Element sink = ElementFactory.make("fakesink", "Destination");
        pipe.addMany(src, sink);
        src.link(sink);
        pipe.setState(State.PLAYING);
        Gst.main();
        pipe.setState(State.NULL);
    }
}