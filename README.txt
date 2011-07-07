EtsyApp is a simple app which uses the Etsy API to do the following two activities.

1) Retrieve 100 ShopIds from the Etsy API each of which has at least one active listing.

2) For each of these IDs, retrieve tag names, associated with them

3) Write to a file records in the format {shopName, Tagname, Count}

4) print the records on the terminal where each record is of the format {shopName: tag1, tag2 .. tag5} [top 5 tags based on the count]


Requirements:
	JDK1.6

Language:

	Java

Frameworks/Libraries Used:

	JUnit, Simple JSON(http://code.google.com/p/json-simple/)

Source:

	The source code used is present in the src/ folder

Test:

	Unit Test cases are present in the test/ folder. The testEtsyApp.java is present as a standalone file. If the file has to be deployed as a solution it can be included in the build.xml for testing purposes.

Installation:

	(Windows Users) : Download the EtsyApp.jar and the runEtsyApp.bat files into a folder
        (Unix Users)    : Download the EtsyApp.jar and the runEtsyApp.sh files into a folder 

Running:
        (Windows Users) :
 
	Open a terminal(DOS) and navigate to folder the containg the .bat & .jar files.

	Type "runEtsyApp.bat" and press Enter

	Refer to the screenshot for an illustration.	

       (Unix Users)    :
        
       Open a terminal and navigate to the folder containing the .sh file

       Type "chmod 755 runEtsyApp.sh" to grant the shell script permission to execute

       Type "./runEtsyApp.sh" and Enter

       Refer to the screenshot for an illustration

TODO:
	Create a .sh file for *nix users


