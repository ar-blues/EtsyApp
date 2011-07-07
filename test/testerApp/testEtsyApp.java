package testerApp;
import static org.junit.Assert.assertEquals;
import helperApp.EtsyAPIHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class testEtsyApp extends EtsyAPIHelper{
	public testEtsyApp(){
		super.MAXSHOPS = 5;
		super.MAXTAGNAMES = 2;
		super.RESULTFETCHLIMIT = 50;
		super.outputFileName = "output/testOutput.csv";
	}
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
	@Test
	public void TestProcessWait(){
		long startTime = System.currentTimeMillis();
		super.processWait(1000);
		long endTime = System.currentTimeMillis();
		assertEquals(true,(endTime-startTime)>1000);
	}
	@Test
	public void TestGETJSONString() throws MalformedURLException, IOException{
		try{
			URL url = new URL("http://www.flickr.com/services/rest/?method=flickr.test.echo&format=json&api_key=6c8eee9f72b9ae67ac05569496ab9627");
			String sampleJSONString = "jsonFlickrApi({\"method\":{\"_content\":\"flickr.test.echo\"}, \"format\":{\"_content\":\"json\"}, \"api_key\":{\"_content\":\"6c8eee9f72b9ae67ac05569496ab9627\"}, \"stat\":\"ok\"})";
			String resultJSONString = super.getJSONString(url).trim();
			assertEquals(sampleJSONString, resultJSONString);
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} 
	}
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
	@Test
	public void TestGetShopIdFromAPI()  throws MalformedURLException, IOException, ParseException{
		try{
			System.out.println(super.getShopIdFromAPI());
			assertEquals(super.MAXSHOPS, super.shopIdArray.length);
		} catch(MalformedURLException mue){
			System.out.println("MalformedURLException: "+mue.toString());
		} catch(IOException ioe){
			System.out.println("IOException: "+ioe.toString());
		} catch(ParseException pe){
			System.out.println("ParseException: "+pe.toString());
		} 
	}
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(testEtsyApp.class);
		if(result.getFailureCount()>0){
			for(Failure failure : result.getFailures()){
				System.out.println("Failure reported: ");
				System.out.println(failure.toString());
			}
		} else {
			System.out.println("No Failures Reported");
		}
	}
}
	