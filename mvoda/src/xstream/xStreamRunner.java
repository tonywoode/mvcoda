package xstream;

import com.thoughtworks.xstream.XStream;

public class xStreamRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		XStream xstream = new XStream();
		
		gfxElementXML urbanChart = new gfxElementXML();
		urbanChart.setThemeName("Urban");
		urbanChart.setType("Static");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setXOffsetSD(400);
		urbanChart.setYOffsetSD(0);
		
		xstream.alias("gfxElement", gfxElementXML.class);
		String xml = xstream.toXML(urbanChart);
		System.out.println(xml);
		
		

	}

}
