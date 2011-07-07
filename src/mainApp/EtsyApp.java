package mainApp;

import helperApp.EtsyAPIHelper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

/*
 * The EtsyApp class contains the main method which executes the getShopIDFromAPI and getTagByShopIdFromAPI
 * from the EtsyAPIHelperClass
 * 
 */
public class EtsyApp {
	/*
	 * Method Name	:	main
	 * Input		: 	None
	 * Returns		: 	Nothing
	 * Output		:	Creates a file containing records of the format {shopName, Tagname, Count} in shopidtagname.csv. 
	 * 					Prints on the terminal records of the format {shopName: tag1, tag2 .. tag5} [top 5 tags based on the count]
	 * 
	 */
	public static void main(String args[]) throws IOException, MalformedURLException, ParseException{
		try{
			EtsyAPIHelper etsyExampleApp = new EtsyAPIHelper();
			System.out.println(etsyExampleApp.getShopIdFromAPI());
			System.out.println(etsyExampleApp.getTagByShopIdFromAPI());
			etsyExampleApp.writeResultsToFile();
			etsyExampleApp.printShopTagName();
		} catch(MalformedURLException mue){
			System.out.println(mue.toString());
		} catch(IOException ioe){
			System.out.println(ioe.toString());
		} catch (ParseException pe){
			System.out.println(pe.toString());
		} catch (Exception e){
			System.out.println("Exception : " + e.toString());
		}
	} 
}
