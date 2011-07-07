package testerApp;
import static org.junit.Assert.assertEquals;
import helperApp.EtsyAPIHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*
 * The testEtsyAPP class implements unit test cases for EtsyAPP using the JUnit framework.
 * 
 * Inherits	: EtsyAPIHelper
 * Returns 	: Nothing
 * Output	: Prints on the terminal, the test results of the important methods used in the EtsyAPI Helper
 * 			
 */
public class testEtsyApp extends EtsyAPIHelper{
	public testEtsyApp(){
		super.MAXSHOPS = 5;
		super.MAXTAGNAMES = 2;
		super.RESULTFETCHLIMIT = 50;
		super.outputFileName = "output/testOutput.csv";
		super.shopIdArray = new long[MAXSHOPS];
	}
	/*
	 * Method Name	:	TestSortHashMapByValues
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Checks the sorted order of the result LinkedHashMap. If it doesn't match the sorted order,
	 * 					prints "failure reported" on the terminal along with comparisons between expected and actual results
	 */
	@Test
	public void TestSortHashMapByValues() {
		LinkedHashMap<String,Integer> unsortedMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String, Integer>();
		unsortedMap.put("Apples", 25);
		unsortedMap.put("Pears", 3);
		unsortedMap.put("Bananas",14);
		unsortedMap.put("Guavas",6);
		unsortedMap.put("Mangoes",14);
		sortedMap.put("Apples", 25);
		sortedMap.put("Mangoes",14);
		sortedMap.put("Bananas",14);
		sortedMap.put("Guavas",6);
		sortedMap.put("Pears", 3);
		LinkedHashMap<String,Integer> resMap = super.sortHashMapByValues(unsortedMap);
		assertEquals("[Apples, Mangoes, Bananas, Guavas, Pears]",resMap.keySet().toString());
		assertEquals("[25, 14, 14, 6, 3]",resMap.values().toString());
	}
	/*
	 * Method Name	:	TestProcessWait
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Checks the time difference between start and end time. If it doesn't match the expected wait time,
	 * 					prints "failure reported" on the terminal along with comparisons between expected and actual results
	 */
	@Test
	public void TestProcessWait(){
		long startTime = System.currentTimeMillis();
		super.processWait(1500);
		long endTime = System.currentTimeMillis();
		assertEquals(true,(endTime-startTime)>1000);
	}
	/*
	 * Method Name	:	TestGETJSONString
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Parses a sample JSON Response from the Flickr API into a string. If the result doesn't match the 
	 * 					expected output,prints "failure reported" on the terminal along with comparisons between expected
	 *					and actual results
	 */
	@Test
	public void TestGETJSONString() throws MalformedURLException, IOException{
		try{
			URL url = new URL("http://www.flickr.com/services/rest/?method=flickr.test.echo&format=json&api_key=6c8eee9f72b9ae67ac05569496ab9627");
			String actualJSONString = "jsonFlickrApi({\"method\":{\"_content\":\"flickr.test.echo\"}, \"format\":{\"_content\":\"json\"}, \"api_key\":{\"_content\":\"6c8eee9f72b9ae67ac05569496ab9627\"}, \"stat\":\"ok\"})";
			String resultJSONString = super.getJSONString(url).trim();
			assertEquals(actualJSONString, resultJSONString);
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} 
	}
	/*
	 * Method Name	:	TestGetJSONArrayFromString
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Parses a string into the JSONArray Object from the Simple JSON library. If the result doesn't match the 
	 * 					expected output,prints "failure reported" on the terminal along with comparisons between expected
	 *					and actual results
	 */
	@Test
	public void TestGetJSONArrayFromString() throws MalformedURLException, IOException, ParseException{
		try{
			URL url = new URL("http://openapi.etsy.com/v2/listings/active?limit=1&offset=0&api_key=hdl75lh8uw8egpv88glrjahf");
			String resultJSONString = super.getJSONString(url);
			JSONArray testJsonArr = super.getJSONArrayFromString(resultJSONString);
			JSONObject testJsonObj = (JSONObject) testJsonArr.get(0);
			assertEquals(true,(testJsonObj.get("listing_id").toString()!=null));
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} catch(ParseException pe){
			System.out.println("ParseException: "+pe.toString());
		}
	}
	/*
	 * Method Name	:	TestGetShopIdFromAPI
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Attempts to retrieve 5 shopIDs from the Etsy API which have non zero active listings. If the 
	 * 					result doesn't match the expected output,prints "failure reported" on the terminal along with 
	 * 					comparisons between expected and actual results
	 */
	@Test
	public void TestGetShopIdFromAPI()  throws MalformedURLException, IOException, ParseException{
		try{
			System.out.println(super.getShopIdFromAPI());
			assertEquals(true, (super.shopIdArray[MAXSHOPS-1] != 0));
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} catch(ParseException pe){
			System.out.println("ParseException: "+pe.toString());
		} 
	}
	/*
	 * Method Name	:	TestGetShopIdFromAPI
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Attempts to retrieve 5 shopIDs from the Etsy API which have non zero active listings. Then tag names
	 * 					associated with each of these shopIDs are retrieved and stored in a HashMap structure. If the 
	 * 					result doesn't match the expected output,prints "failure reported" on the terminal along with 
	 * 					comparisons between expected and actual results
	 */
	@Test
	public void TestGetTagByShopIdFromAPI() throws MalformedURLException, IOException, ParseException{
		try{
			TestGetShopIdFromAPI();
			System.out.println(super.getTagByShopIdFromAPI());
			assertEquals(true,super.shopIdTagNameMap.keySet().size()==super.MAXSHOPS);
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} catch(ParseException pe){
			System.out.println("ParseException: "+pe.toString());
		}
	}
	/*
	 * Method Name	:	main
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	All the unit test methods in this class are implemented in a sequential manner. The results of each of 
	 * 					these methods are printed on the terminal. If all the unit test cases run without problems, "No failures Reported"
	 * 					is printed on the terminal
	 */
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(testEtsyApp.class);
		if(result.getFailureCount()>0){
			for(Failure failure : result.getFailures()){
				System.out.println("Failure reported: ");
				System.out.println(failure.toString()+"\n");
			}
		} else {
			System.out.println("No Failures Reported");
		}
	}
}
	