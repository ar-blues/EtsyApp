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

	Unit Test cases are present in the test/ folder

Installation:

	Download the EtsyApp.jar and the runEtsyApp.bat files into a folder

Running:
	Open a terminal(DOS) and navigate to folder the containg the .bat & .jar files.
	Type "runEtsyApp.bat" and press Enter

	Refer to the screenshot for an illustration.	
	


