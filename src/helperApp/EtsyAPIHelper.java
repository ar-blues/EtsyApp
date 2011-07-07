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

/*
 * the EtsyAPIHelper class is the helper class for the EtsyApp class. This class contains methods that: 
 * 	a) fetch 100 shopIDs from the Etsy API each of which has at least one active listing
 * 	b) fetch tag names associated with each of these shopIds from the Etsy API
 *  c) store the results in file where each record is of the format {shopName, Tagname, Count}
 *  d) print the records on the terminal where each record is of the format {shopName: tag1, tag2 .. tag5} [top 5 tags based on the count]  
 *  
 * there are also additional utility functions to perform the following actions:
 * 	a) halt the process for specified amount of time
 *  b) sort a Map<Key,Value> by the values
 *  c) write to a file
 *  d) Parse JSON response from the API into a String & Parse the string into a JSONArray and JSONObject format (from the simple json library used)
 *     
 */
public class EtsyAPIHelper {
	/*
	 *  Following are properties of the EtsyAPIHelper class that have been defined appropriately to be used 
	 *  later in the methods. The variable names have been named so in an effort to make the properties self-explanatory.
	 */
	private final String apiKey = "hdl75lh8uw8egpv88glrjahf";
	private final String getShopIdURL = "http://openapi.etsy.com/v2/shops";
	private final String getShopListingURL = "http://openapi.etsy.com/v2/shops/";
	protected int MAXSHOPS = 100;
	protected int MAXTAGNAMES = 5;
	protected int RESULTFETCHLIMIT = 100;
	protected String outputFileName = "output/shopidtagname.csv";
	protected long[] shopIdArray;
	protected HashMap<Long, String> shopIdNameMap;
	protected HashMap<Long, LinkedHashMap<String, Integer>> shopIdTagNameMap;
	/*
	 * the constructor method for the EtsyAPIHelper class initializes certain properties and structures of the class 
	 */
	public EtsyAPIHelper(){
		shopIdArray = new long[MAXSHOPS];
		shopIdNameMap = new HashMap<Long, String>();
		shopIdTagNameMap = new HashMap<Long, LinkedHashMap<String,Integer>>();
	}
	/*
	 * Method Name	:	processWait
	 * Input		: 	time (integer)
	 * Returns		: 	Nothing
	 * Output		:	Halts the process for 'time' milliseconds 
	 * 
	 */
	protected void processWait(int time){
		System.out.println("Process will wait for "+(time)+" milliseconds to support API Access Policy\n");
		try{
			Thread.sleep(time);
		} catch (Throwable t){
			throw new OutOfMemoryError("System went out of memory while waiting");
		}
	}
	/*
	 * Method Name	:	getJSONString
	 * Input		: 	url (URL)
	 * Returns		: 	String 
	 * Output		:	parses JSON response from the API(specified by 'url') and converts it into a string to be returned 
	 * 
	 */
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
	/*
	 * Method Name	:	getJSONArrayFromString
	 * Input		: 	jsonString (String)
	 * Returns		: 	JSONArray 
	 * Output		:	parses string and converts it into a JSONArray to be returned 
	 * 
	 */
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
	/*
	 * Method Name	:	sortHashMapByValues
	 * Input		: 	passedMap (LinkedHashMap<String,Integer>)
	 * Returns		: 	LinkedHashMap<String,Integer> 
	 * Output		:	sorts a LinkedHashMap<Key,Value> in Descending Order based on the values and returns the sorted LinkedhashMap. 
	 * 					The LinkedHashMap is used to harness its property of storing elements in the order of their entry which is not
	 * 					the case with the HashMap.
	 * Note			: 	The sort follows a stable mechanism wherein if there are records containing the same value, then they are inserted into
	 * 					the result based on their existing order in reverse (due to descending order).
	 * 					(Eg.) {<k1,3>,<k2,1>,<k3,4>,<k4,1>} is sorted as {<k3,4>,<k1,3>,<k4,1>,<k2,1>} 				  
	 * 
	 */
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
	/*
	 * Method Name	:	writeResultsToFile
	 * Input		: 	None
	 * Returns		: 	Nothing 
	 * Output		:	Writes records of the format {shopName, Tagname, Count} to the output file located at output/shopidtagname.csv
	 * 					The records are created from the properties/structures shopIdArray and shopIdTagNameMap.      
	 * 
	 */
	public void writeResultsToFile() throws IOException{
		try{
			System.out.println("Creating output file to record shop names and related tags\n");
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
			System.out.println("Created output file at ./"+outputFileName+"\n");
			out.close();
		} catch (IOException e){
			System.out.println("IOException : " + e.toString());
		} catch (Exception e){
			System.out.println("Exception : " + e.toString());
		}
	}
	/*
	 * Method Name	:	printShopTagName
	 * Input		: 	None
	 * Returns		: 	Nothing 
	 * Output		:	Prints on the terminal records of the format {shopName: tag1, tag2 .. tag5} [top 5 tags based on the count]
	 * 					The records are created from the properties/structures shopIdArray and shopIdTagNameMap.
	 * 
	 * NOTE			: 	If there are less than five tag names associated, it prints all the tag names       
	 * 
	 */
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
	/*
	 * Method Name	:	getShopIdFromAPI
	 * Input		: 	None
	 * Returns		: 	String 
	 * Output		:	Accesses the Etsy API to get 100 ShopIds each of which have at least one active listing associated with them
	 * NOTE			: 	The Etsy API requires that there be no more than 5 requests per second. The method waits for 1 second, before the 
	 * 					hitting the API again to allow for some buffer time.    
	 * 
	 */
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
			return "Shop IDs fetched from the Etsy API using : "+getShopIdURL+"\n";	
		} catch(MalformedURLException mue){
			return mue.toString();
		} catch(IOException ioe){
			return ioe.toString();
		} catch (ParseException pe){
			return pe.toString();
		}
	}
	/*
	 * Method Name	:	getTagByShopIdFromAPI
	 * Input		: 	None
	 * Returns		: 	String 
	 * Output		:	Accesses the Etsy API to get tag names associated with each of the 100 ShopIds fetched already. The results are stored in the MAP 
	 * 					containing {Key=ShopId,Value={Key=tagname,Value=Count}}. Having a Map<Key,Map<tagname,count>> Structure enables a O(2) lookup time for tagname count 
	 * 					per shopId. 
	 * NOTE			: 	The Etsy API requires that there be no more than 5 requests per second. The method waits for 1 second, before the 
	 * 					hitting the API again to allow for some buffer time.    
	 * 
	 */
	public String getTagByShopIdFromAPI() throws MalformedURLException, IOException, ParseException{
		try{
			System.out.println("Fetching TagNames for each of the shopIDs from "+getShopListingURL+"\n");
			for(int shopIdIter=0;shopIdIter<shopIdArray.length;shopIdIter++){
				URL getListingURL = new URL(getShopListingURL+shopIdArray[shopIdIter]+"/listings/active"+"?api_key="+apiKey);
				String jsonString = getJSONString(getListingURL);
				JSONArray jsonResult = getJSONArrayFromString(jsonString);
				LinkedHashMap<String, Integer> tagnamecountMap = new LinkedHashMap<String, Integer>();
				for(int jsonIter=0;jsonIter<jsonResult.size();jsonIter++){
					JSONObject shopListingObj = (JSONObject) jsonResult.get(jsonIter);
					JSONArray tagNameArray= (JSONArray) shopListingObj.get("tags");
					for(int tagNameIter=0;tagNameIter<tagNameArray.size();tagNameIter++){
						if(tagnamecountMap.get(tagNameArray.get(tagNameIter))==null){
							tagnamecountMap.put(tagNameArray.get(tagNameIter).toString().toLowerCase(), 1);
						} else {
							int tagcount = tagnamecountMap.get(tagNameArray.get(tagNameIter));
							tagnamecountMap.put(tagNameArray.get(tagNameIter).toString().toLowerCase(), tagcount+1);
						}
					}
				}
				shopIdTagNameMap.put(shopIdArray[shopIdIter], tagnamecountMap);
			}
			for(int shopIdIter=0;shopIdIter<shopIdArray.length;shopIdIter++){
				LinkedHashMap<String,Integer> mapToSort = new LinkedHashMap<String, Integer>();
				mapToSort = shopIdTagNameMap.get(shopIdArray[shopIdIter]);
				shopIdTagNameMap.put(shopIdArray[shopIdIter],sortHashMapByValues(mapToSort));
			}
			return "Tag Names for each Shop Id fetched\n";
		} catch(MalformedURLException mue){
			return mue.toString();
		} catch(IOException ioe){
			return ioe.toString();
		} catch (ParseException pe){
			return pe.toString();
		}
	}
}