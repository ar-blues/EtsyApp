package mainApp;

import helperApp.EtsyAPIHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

public class EtsyApp {
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
