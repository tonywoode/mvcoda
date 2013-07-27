package xstream;

import theme.Pop;
import theme.Theme;
import gfxelement.GFXElement;
import gfxelement.chart.FMTop20Chart;

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
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );
		
		xstream.alias("gfxElement", gfxElementXML.class);
		String xml = xstream.toXML(urbanChart);
		System.out.println(xml);
		
		//lets see what happens if i go for an existing class
		XStream xstreamExisting = new XStream();
		xstreamExisting.alias("GFXElement", FMTop20Chart.class);
		GFXElement chart = new FMTop20Chart(new Pop());
		String xml2 = xstream.toXML(chart);
		System.out.println("and from the class \n" + xml2);
	}

}
