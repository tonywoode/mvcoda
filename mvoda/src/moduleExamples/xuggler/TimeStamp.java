package moduleExamples.xuggler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ShortBuffer;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;

public class TimeStamp {

    private static final String inputFilename = "../../../MVODAInputs/Love/BrunoMarsJustTheWay.avi";
    private static final String outputFilename = "../../../MVODAOutputs/ModifyMedia.mov";

    public static void main(String[] args) {

        // create a media reader
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
        
        //here we can just add a viewer straight away to see our file
        //mediaReader.addListener(ToolFactory.makeViewer());
        //while(mediaReader.readPacket() == null);
        
        // configure it to generate BufferImages "have the reader create a buffered image that others can resuse"
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        IMediaWriter mediaWriter = ToolFactory.makeWriter(outputFilename, mediaReader);
        
        IMediaTool addTimeStamp = new TimeStampTool();
        IMediaTool audioVolumeMediaTool = new VolumeAdjustMediaTool(0.1);
        IMediaTool tonysMediaTool = new TonysMediaTool(inputFilename);
        
        // create a tool chain:
        // reader -> addStaticImage -> reduceVolume -> writer
        mediaReader.addListener(addTimeStamp);
        addTimeStamp.addListener(tonysMediaTool);
        tonysMediaTool.addListener(audioVolumeMediaTool);
        audioVolumeMediaTool.addListener(mediaWriter);
        
        while (mediaReader.readPacket() == null) ;

    }
    
    private static class TonysMediaTool extends MediaToolAdapter {
           
        public TonysMediaTool(String inputFilename) {
                 	IMediaReader mediaReader2 = ToolFactory.makeReader(inputFilename);   
        }  
    }
        
 
    private static class VolumeAdjustMediaTool extends MediaToolAdapter {
        
        // the amount to adjust the volume by
        private double mVolume;
        
        public VolumeAdjustMediaTool(double volume) {
            mVolume = volume;
        }

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            
            // get the raw audio bytes and adjust it's value
            ShortBuffer buffer = 
               event.getAudioSamples().getByteBuffer().asShortBuffer();
            
            for (int i = 0; i < buffer.limit(); ++i) {
                buffer.put(i, (short) (buffer.get(i) * mVolume));
            }

            // call parent which will pass the audio onto next tool in chain
            super.onAudioSamples(event);
            
        }
        
    }


    
    
    public static class TimeStampTool extends MediaToolAdapter
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


