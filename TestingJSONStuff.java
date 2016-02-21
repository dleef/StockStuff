import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestingJSONStuff {
	private final String USER_AGENT = "Mozilla/5.0";
	
	public static void main (String args[]) throws IOException, ParseException{
		
		TestingJSONStuff blah = new TestingJSONStuff();
		blah.getInfo(args[0], args[1], args[2], args[3]);
		
		
	}
	
	
	public void getInfo(String first, String second, String third, String fourth) throws IOException, ParseException{
		
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22" + first + "%22%2C%22" + second + "%22%2C%22" + third + "%22%2C%22" + fourth + "%22)%0A%09%09&format=json&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env&callback=";
	
		URL urlObj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null){
			response.append(inputLine);
		
		}
		in.close();
		//System.out.println(response);
		Hashtable<Double, Object> EPSStorage = new Hashtable<Double, Object>();
		Hashtable<Float, Object> ChangeStorage = new Hashtable<Float, Object>();
		
		JSONParser parser = new JSONParser();
		JSONObject allText = (JSONObject) parser.parse(response.toString());
		
		JSONObject query = (JSONObject) allText.get("query");
		JSONObject results = (JSONObject) query.get("results");
		
		JSONArray quote = (JSONArray)results.get("quote");
		
		JSONObject object1 = (JSONObject)quote.get(0);
		//EPS1 = earnings per share
		Object EPS1 = object1.get("EPSEstimateCurrentYear");
		double firstEPS = Double.parseDouble(EPS1.toString());
		JSONObject object2 = (JSONObject)quote.get(1);
		Object EPS2 = object2.get("EPSEstimateCurrentYear");
		double secondEPS = Double.parseDouble(EPS2.toString());
		JSONObject object3 = (JSONObject)quote.get(2);
		Object EPS3 = object3.get("EPSEstimateCurrentYear");
		double thirdEPS = Double.parseDouble(EPS3.toString());
		JSONObject object4 = (JSONObject)quote.get(3);
		Object EPS4 = object4.get("EPSEstimateCurrentYear");
		double fourthEPS = Double.parseDouble(EPS4.toString());
		
		EPSStorage.put(firstEPS, object1.get("symbol").toString());
		EPSStorage.put(secondEPS, object2.get("symbol").toString());
		EPSStorage.put(thirdEPS, object3.get("symbol").toString());
		EPSStorage.put(fourthEPS, object4.get("symbol").toString());
		double EPSMAX = Math.max(Math.max(firstEPS, secondEPS),Math.max(thirdEPS, fourthEPS));
		System.out.println("Company with highest EPS is " + EPSStorage.get(EPSMAX));
		System.out.println("Earnings Per Share of all companies: ");
		System.out.println(EPSStorage.get(firstEPS).toString() + " : " + firstEPS);
		System.out.println(EPSStorage.get(secondEPS).toString() + " : " + secondEPS);
		System.out.println(EPSStorage.get(thirdEPS).toString() + " : " + thirdEPS);
		System.out.println(EPSStorage.get(fourthEPS).toString() + " : " + fourthEPS);
		//FDC = fifty day change
		Object FDC1 = object1.get("PercentChangeFromFiftydayMovingAverage");
		float firstChange = Float.valueOf(FDC1.toString().substring(1, 5));
		if (FDC1.toString().startsWith("-")){
			firstChange = Float.valueOf(FDC1.toString().substring(0, 5));
		}
		
		
		Object FDC2 = object2.get("PercentChangeFromFiftydayMovingAverage");
		float secondChange = Float.valueOf(FDC2.toString().substring(1, 5));
		if (FDC2.toString().startsWith("-")){
			secondChange = Float.valueOf(FDC2.toString().substring(0, 5));
		}
		
		
		Object FDC3 = object3.get("PercentChangeFromFiftydayMovingAverage");
		float thirdChange = Float.valueOf(FDC3.toString().substring(1, 5));
		if (FDC3.toString().startsWith("-")){
			thirdChange = Float.valueOf(FDC3.toString().substring(0, 5));
		}
		//program is just using float after the if statement regardless of sign 
		
		
		Object FDC4 = object4.get("PercentChangeFromFiftydayMovingAverage");
		float fourthChange = Float.valueOf(FDC4.toString().substring(1, 5));
		if (FDC4.toString().startsWith("-")){
			fourthChange = Float.valueOf(FDC4.toString().substring(0, 5));
		}
		//NEED TO FIGURE OUT HOW TO MAKE NUMBERS NEGATIVE
		
		
		float changeMAX = Math.max(Math.max(firstChange, secondChange), Math.max(thirdChange,  fourthChange));
		
		ChangeStorage.put(firstChange, object1.get("symbol").toString());
		ChangeStorage.put(secondChange, object2.get("symbol").toString());
		ChangeStorage.put(thirdChange, object3.get("symbol").toString());
		ChangeStorage.put(fourthChange, object4.get("symbol").toString());
		//need to figure out how to have numbers be listed as negative
		//could try doing "fourthchange*(-1)
		//System.out.println(ChangeStorage.get(9.85));
	
		System.out.println("Best performing stock is " + ChangeStorage.get(changeMAX));
		System.out.println("Fifty day moving average % of all companies: ");
		System.out.println(ChangeStorage.get(firstChange).toString() + " : " + firstChange);
		System.out.println(ChangeStorage.get(secondChange).toString() + " : " + secondChange);
		System.out.println(ChangeStorage.get(thirdChange).toString() + " : " + thirdChange);
		System.out.println(ChangeStorage.get(fourthChange).toString() + " : " + fourthChange);
	
		//JSONObject EPSone = (JSONObject) one.get("EPSEstimateCurrentYear");
	
		
	}
	
	
	
	
	
	
}
