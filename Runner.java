import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Runner {
	public static boolean ASC = true;
	public static boolean DESC = false;
	private final String USER_AGENT = "Mozilla/5.0";
	
	public static void main (String args[]) throws IOException, InterruptedException{
		
		
		Runner stocks = new Runner();
		String line;
		ArrayList<String> tickers = new ArrayList<>();
		//String[] tickersBlah = new String[tickers.size()];
		//tickersBlah = tickers.toArray(tickersBlah);
		Map<String, Integer> tickerValues = new HashMap<String, Integer>();
		ArrayList<Double> values = new ArrayList<>();
		
		
		//ValueComparator bvc = new ValueComparator(tickerValues);
        //TreeMap sorted_map = new TreeMap(bvc);
		File file = new File("/Users/Danuzi/Documents/goodcompanies.txt");
		try (BufferedReader in = new BufferedReader(new FileReader(file))){
		
		while ((line = in.readLine()) != null){
			
			tickers.add(line.split("\\|")[0]);
			
		}
			
		}
	
		//System.out.println(tickers.get(0));
		
		
		for (int i = 0; i < tickers.size(); i++){
			
			try{
			tickerValues.put(tickers.get(i), (int)stocks.getPercentVolume(tickers.get(i)));
			System.out.println(tickers.get(i));
			}
			catch (Exception e){
				System.out.println(e);
			}
		}
		
		
		Map<String, Integer> sortedMapDesc = sortByComparator(tickerValues, DESC);
		//Map.Entry<String,Integer> entry=tickerValues.entrySet().iterator().next();
		//String key= entry.getKey();
		//int value = entry.getValue();
        printMap(sortedMapDesc);
		//System.out.println("Percent volume is: " + (int)stocks.getPercentVolume(args[0]) + "%");
		//System.out.println("Last trade price is: " + stocks.getLastTradePrice(args[0]));
		
	
	}
	
	
	public double getPercentVolume(String ticker) throws IOException{
	
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22" + ticker + "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	
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
		
		JSONObject allText = (JSONObject) JSONValue.parse(response.toString());
		JSONObject query = (JSONObject) allText.get("query");
		JSONObject results = (JSONObject) query.get("results");
		JSONObject quote = (JSONObject) results.get("quote");
		
		double percentVolume = (Double.parseDouble((quote.get("Volume")).toString()) / Integer.parseInt ((quote.get("AverageDailyVolume")).toString()))*100;
		double lastTradePrice = Double.parseDouble((quote.get("LastTradePriceOnly")).toString());
		
		return percentVolume;
	}
	
	public double getLastTradePrice(String ticker) throws IOException{
	
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22" + ticker + "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	
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
		
		JSONObject allText = (JSONObject) JSONValue.parse(response.toString());
		JSONObject query = (JSONObject) allText.get("query");
		JSONObject results = (JSONObject) query.get("results");
		JSONObject quote = (JSONObject) results.get("quote");
		
		double percentVolume = (Double.parseDouble((quote.get("Volume")).toString()) / Double.parseDouble ((quote.get("AverageDailyVolume")).toString()))*100;
		double lastTradePrice = Double.parseDouble(((quote.get("LastTradePriceOnly")).toString()));
		
		return lastTradePrice;
	}

	 private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	    {

	        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

	        // Sorting the list based on values
	        Collections.sort(list, new Comparator<Entry<String, Integer>>()
	        {
	            public int compare(Entry<String, Integer> o1,
	                    Entry<String, Integer> o2)
	            {
	                if (order)
	                {
	                    return o1.getValue().compareTo(o2.getValue());
	                }
	                else
	                {
	                    return o2.getValue().compareTo(o1.getValue());

	                }
	            }
	        });

	        // Maintaining insertion order with the help of LinkedList
	        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	        for (Entry<String, Integer> entry : list)
	        {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }

	        return sortedMap;
	    }

	    public static void printMap(Map<String, Integer> map)
	    {
	        for (Entry<String, Integer> entry : map.entrySet())
	        {
	            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
	        }
	    }
	}


   
 



