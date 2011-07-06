import static org.junit.Assert.*;

import java.util.HashMap;

import helperApp.EtsyAPIHelper;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.ParentRunner;


public class testEtsyApp extends EtsyAPIHelper{

	@Test
	public void TestSortHashMapByValues() {
		HashMap<String,Integer> unsortedMap = new HashMap<String, Integer>();
		HashMap<String,Integer> sortedMap = new HashMap<String, Integer>();
		unsortedMap.put("Apples", 25);
		unsortedMap.put("Pears", 3);
		unsortedMap.put("Bananas",14);
		unsortedMap.put("Guavas",6);
		unsortedMap.put("Mangoes",14);
		sortedMap.put("Pears", 3);
		sortedMap.put("Guavas",6);
		sortedMap.put("Bananas",14);
		sortedMap.put("Mangoes",14);
		sortedMap.put("Apples", 25);		
		HashMap<String,Integer> resMap = super.sortHashMapByValues(unsortedMap);
		System.out.println(resMap.keySet());
		System.out.println(resMap.values());
		assertEquals("[]",resMap);
	}
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(testEtsyApp.class);
		if(result.getFailureCount()>0){
			for(Failure failure : result.getFailures()){
				System.out.println(failure.toString());
			}
		} else {
			System.out.println("No Failures Reported");
		}
	}
}
