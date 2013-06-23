package media.xuggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ShortBuffer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;

/**
 * Read and modify audio and video frames and use the {@link
 * IMediaWriter} to encode that media and write it out to a file.
 */
public class ModifyMusicVideo
{
  // the log
  private static final Logger log = LoggerFactory.getLogger(
    ModifyMusicVideo.class);
  { log.trace("<init>"); }

  public ModifyMusicVideo(String fileUNC, String outFileUNC) {
   
    File inputFile = new File(fileUNC);
    if (!inputFile.exists())
    {
      System.out.println("Input file does not exist: " + inputFile);
      System.exit(-1);
    }

    File outputFile = new File(outFileUNC);

    // create a media reader and configure it to generate BufferImages

    IMediaReader reader = ToolFactory.makeReader(inputFile.toString());
    reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

    // create a writer and configure it's parameters from the reader
    
    IMediaWriter writer = ToolFactory.makeWriter(outputFile.toString(), reader);

    // create a tool which paints video time stamp into frame

    IMediaTool addTimeStamp = new TimeStampTool();


    // create a tool chain:
    //   reader -> addTimeStamp -> reduceVolume -> writer

    reader.addListener(addTimeStamp);
    addTimeStamp.addListener(writer);

    // add a viewer to the writer, to see media modified media
    
    writer.addListener(ToolFactory.makeViewer());

    // read and decode packets from the source file and then encode and write out data to the output file
    
    while (reader.readPacket() == null)
      do {} while(false);
  }  

  /** 
   * Create a tool which adds a time stamp to a video image.
   */

  static class TimeStampTool extends MediaToolAdapter
  {
    /** {@inheritDoc} */

    @Override
      public void onVideoPicture(IVideoPictureEvent event)
    {
      // get the graphics for the image

      Graphics2D g = event.getImage().createGraphics();

      // establish the timestamp and how much space it will take

      String timeStampStr = event.getPicture().getFormattedTimeStamp();
      Rectangle2D bounds = g.getFont().getStringBounds(timeStampStr,
        g.getFontRenderContext());
      
      // compute the amount to inset the time stamp and translate the
      // image to that position

      double inset = bounds.getHeight() / 2;
      g.translate(inset, event.getImage().getHeight() - inset);
      
      // draw a white background and black timestamp text

      g.setColor(Color.WHITE);
      g.fill(bounds);
      g.setColor(Color.BLACK);
      g.drawString(timeStampStr, 0, 0);
      
      // call parent which will pass the video onto next tool in chain

      super.onVideoPicture(event);
    }
  }


 
}
