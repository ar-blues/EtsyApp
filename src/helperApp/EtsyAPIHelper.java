package helperApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EtsyAPIHelper {
	
	private final String apiKey = "hdl75lh8uw8egpv88glrjahf";
	private final String getShopIdURL = "http://openapi.etsy.com/v2/shops";
	private final String getShopListingURL = "http://openapi.etsy.com/v2/shops/";
	private final int MAXSHOPS = 50;
	private final int MAXTAGNAMES = 5;
	private final int RESULTFETCHLIMIT = 100;
	private final String outputFileName = "shopidtagname.csv";
	private long[] shopIdArray;
	private HashMap<Long, String> shopIdNameMap;
	private HashMap<Long, LinkedHashMap<String, Integer>> shopIdTagNameMap;
	
	public EtsyAPIHelper(){
		shopIdArray = new long[MAXSHOPS];
		shopIdNameMap = new HashMap<Long, String>();
		shopIdTagNameMap = new HashMap<Long, LinkedHashMap<String,Integer>>();
	}
	
	protected void processWait(int time){
		System.out.println("Process will wait for "+(time)+" milliseconds to support API Access Policy");
		try{
			Thread.sleep(time);
		} catch (Throwable t){
			throw new OutOfMemoryError("System went out of memory while waiting");
		}
	}
	
	protected String getJSONString(URL url) throws IOException{
		try{
			URLConnection urlConnection = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String jsonInputLine;
			String jsonString="";
			while((jsonInputLine=br.readLine())!=null){
				jsonString += jsonInputLine+"\n";
			}
			br.close();
			return jsonString;
		} catch(IOException ioe){
			return ioe.toString();
		}
	}
	
	protected JSONArray getJSONArrayFromString(String jsonString) throws ParseException{
		Object parseObj = new Object();
		JSONArray jsonResult = new JSONArray();
		try{
			JSONParser parser = new JSONParser();
			parseObj = parser.parse(new StringReader(jsonString));
			JSONObject jsonObj = (JSONObject) parseObj;
			jsonResult = (JSONArray) jsonObj.get("results");
			return jsonResult;
		} catch (ParseException pe){
			String returnString = "ParseException : " + pe.toString();
			System.out.println(returnString);
			return jsonResult;
		} catch (Exception e){
			String returnString = "Exception : " + e.toString();
			System.out.println(returnString);
			return jsonResult;
		}
		
	}
	
	protected LinkedHashMap<String,Integer> sortHashMapByValues(LinkedHashMap<String,Integer> passedMap) {
	    List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
	    Comparator<String> keyComparator = Collections.reverseOrder();
	    Comparator<Integer> valueComparator = Collections.reverseOrder();
	    Collections.sort(mapKeys,keyComparator);
	    Collections.sort(mapValues,valueComparator);
	    LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();	    
	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Object val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();
	        while(keyIt.hasNext()){
	            Object key = keyIt.next();
	            String comp1 = passedMap.get(key).toString();
	            String comp2 = val.toString(); 
	            if (comp1.equals(comp2)){
	                passedMap.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put((String)key, (Integer)val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
	public void writeResultsToFile() throws IOException{
		try{
			System.out.println("Creating output file to record shop names and related tags");
			FileWriter fstream = new FileWriter(outputFileName);
			BufferedWriter out = new BufferedWriter(fstream);
			int shopIdIter=0;
			while(shopIdIter<MAXSHOPS){
				Long shopIdKey = shopIdArray[shopIdIter];
				Set<String> tagNames = shopIdTagNameMap.get(shopIdKey).keySet();
				Iterator<String> tagNameIter = tagNames.iterator();
				while(tagNameIter.hasNext()){
					Object tagNameKey = tagNameIter.next();
					out.write(shopIdNameMap.get(shopIdKey)+","+tagNameKey+","+
							shopIdTagNameMap.get(shopIdKey).get(tagNameKey)+"\n");
				}
				shopIdIter++;
			}
			System.out.println("Created output file at ./"+outputFileName);
			out.close();
		} catch (IOException e){
			System.out.println("IOException : " + e.toString());
		} catch (Exception e){
			System.out.println("Exception : " + e.toString());
		}
	}
	
	public void printShopTagName(){
		System.out.println("Displaying shop name and top 5 associated tags:\n");
		int shopIdIter=0;
		while(shopIdIter<MAXSHOPS){
			long shopIdKey = shopIdArray[shopIdIter];
			System.out.print(shopIdNameMap.get(shopIdKey)+" : ");
			Set<String> tagNames = shopIdTagNameMap.get(shopIdKey).keySet();
			Iterator<String> tagNameIter = tagNames.iterator();
			int tagNameCounter = 0;
			while(tagNameIter.hasNext() && tagNameCounter<MAXTAGNAMES){
				Object tagNameKey = tagNameIter.next();
				System.out.print(tagNameKey);
				if(tagNameCounter!=(MAXTAGNAMES-1)){
					System.out.print(",");
				}
				tagNameCounter++;
			}
			System.out.println("\n");
			shopIdIter++;
		}
	}
	
	public String getShopIdFromAPI() throws MalformedURLException, IOException, ParseException{
		try{
			int offset = 0;
			int shopCounter = 0;
			while(shopCounter<=(MAXSHOPS-1)){
				System.out.println("Retrieving Records from URL : "+getShopIdURL+"?limit="+RESULTFETCHLIMIT+"&offset="+offset);
				URL shopIdURL = new URL(getShopIdURL+"?limit="+RESULTFETCHLIMIT+"&offset="+offset+"&api_key="+apiKey);
				String jsonString = getJSONString(shopIdURL);
				JSONArray jsonResult = getJSONArrayFromString(jsonString);
				for(int iter=0;iter<jsonResult.size();iter++){
					JSONObject shopIdObj = (JSONObject) jsonResult.get(iter);
					if(Integer.parseInt((shopIdObj.get("listing_active_count").toString()))!=0){
						shopIdArray[shopCounter++] = Long.parseLong((shopIdObj.get("shop_id").toString()));
						shopIdNameMap.put(Long.parseLong((shopIdObj.get("shop_id").toString())),(shopIdObj.get("shop_name").toString()));
						if(shopCounter>(MAXSHOPS-1)){
							break;
						}
					}
				}
				System.out.println("Shop IDs with active listing retrieved : "+shopCounter);
				offset = offset+RESULTFETCHLIMIT;
				processWait(1000);
			}
			return "Shop IDs fetched from the Etsy API using : "+getShopIdURL;	
		} catch(MalformedURLException mue){
			return mue.toString();
		} catch(IOException ioe){
			return ioe.toString();
		} catch (ParseException pe){
			return pe.toString();
		}
	}
	
	public String getTagByShopIdFromAPI() throws MalformedURLException, IOException, ParseException{
		try{
			for(int shopIdIter=0;shopIdIter<shopIdArray.length;shopIdIter++){
				URL getListingURL = new URL(getShopListingURL+shopIdArray[shopIdIter]+"/listings/active"+"?api_key="+apiKey);
				String jsonString = getJSONString(getListingURL);
				JSONArray jsonResult = getJSONArrayFromString(jsonString);
				LinkedHashMap<String, Integer> tagnamecount = new LinkedHashMap<String, Integer>();
				for(int jsonIter=0;jsonIter<jsonResult.size();jsonIter++){
					JSONObject shopListingObj = (JSONObject) jsonResult.get(jsonIter);
					JSONArray tagNameArray= (JSONArray) shopListingObj.get("tags");
					for(int tagNameIter=0;tagNameIter<tagNameArray.size();tagNameIter++){
						if(tagnamecount.get(tagNameArray.get(tagNameIter))==null){
							tagnamecount.put(tagNameArray.get(tagNameIter).toString().toLowerCase(), 1);
						} else {
							int tagcount = tagnamecount.get(tagNameArray.get(tagNameIter));
							tagnamecount.put(tagNameArray.get(tagNameIter).toString().toLowerCase(), tagcount+1);
						}
					}
				}
				shopIdTagNameMap.put(shopIdArray[shopIdIter], tagnamecount);
			}
			for(int shopIdIter=0;shopIdIter<shopIdArray.length;shopIdIter++){
				LinkedHashMap<String,Integer> mapToSort = new LinkedHashMap<String, Integer>();
				mapToSort = shopIdTagNameMap.get(shopIdArray[shopIdIter]);
				shopIdTagNameMap.put(shopIdArray[shopIdIter],sortHashMapByValues(mapToSort));
			}
			return "Tag Names for each Shop Id fetched";
		} catch(MalformedURLException mue){
			return mue.toString();
		} catch(IOException ioe){
			return ioe.toString();
		} catch (ParseException pe){
			return pe.toString();
		}
	}
}