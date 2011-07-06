import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;



public class testScrape {

	@Test
	public void TestAdd() {
		scrapeJSON tester = new scrapeJSON();
		assertEquals("Result", 50, tester.add(25, 25));
	}
	
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(testScrape.class);
		if(result.getFailureCount()>0){
			for(Failure failure : result.getFailures()){
				System.out.println(failure.toString());
			}
		} else {
			System.out.println("No Failures Reported");
		}
	}
}
