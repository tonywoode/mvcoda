package xstream;




import com.thoughtworks.xstream.XStream;

public class XStreamRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		XStream xstream = new XStream();
		
		GFXElement urbanChart = new GFXElement();
		urbanChart.setThemeName("Urban");
		urbanChart.setElementName("UrbanChart");
		urbanChart.setType("Static");
		urbanChart.setVersion("1.0");
		urbanChart.setAuthor("BoxTV Design Team");
		urbanChart.setCoOrd( new CoOrd(400,0) );
		
		
		
		
		
		
		xstream.alias("gfxElement", GFXElement.class);
		String xml = xstream.toXML(urbanChart);
		System.out.println(xml);
		
	/*	//lets see what happens if i go for an existing class
		XStream xstreamExisting = new XStream();
		xstreamExisting.alias("GFXElement", FMTop20Chart.class);
		GFXElement chart = new FMTop20Chart(new Pop());
		String xml2 = xstream.toXML(chart);
		System.out.println("and from the class \n" + xml2);
		
		//hmmm it's printed the entire theme of Pop not just the FMTop20Chart - why?
*/		
		
		//let's try making a GFX element as it is at the moment
		
		
	}

}
