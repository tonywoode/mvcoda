package xstream;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;


public class GFXElementXMLBuilders {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		
		Path path1 = Paths.get("Elements");
		Path path2 = Paths.get(path1.toString(),"Urban");
		
		XStream xstream = new XStream();
		xstream.alias("gfxElement", GFXElement.class);
		
		GFXElement urbanChart = new GFXElement();
		urbanChart.setThemeName("Urban");
		urbanChart.setType("Static");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );
		
		 
		
		
		
		String xml = xstream.toXML(urbanChart);
		System.out.println("Making xml " + urbanChart.getClass().getClass().getName() + "\n");
		System.out.println(xml);
		
		 try {
			 Path path3 = Paths.get(path2.toString(), "UrbanChart.xml");
	            FileOutputStream fs = new FileOutputStream(path3.toString());
	            xstream.toXML(urbanChart, fs);
	        } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	        }
		
	
		
		
	}

}
