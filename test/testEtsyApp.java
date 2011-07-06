import static org.junit.Assert.assertEquals;
import helperApp.EtsyAPIHelper;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class testEtsyApp extends EtsyAPIHelper{

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
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(testEtsyApp.class);
		if(result.getFailureCount()>0){
			for(Failure failure : result.getFailures()){
				System.out.println("Failure reported in sortHashMapByValues()");
				System.out.println(failure.toString());
			}
		} else {
			System.out.println("No Failures Reported");
		}
	}
}
