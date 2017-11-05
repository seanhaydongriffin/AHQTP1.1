package FA.transportnsw;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import java.util.Hashtable;

public class Close 
{
	@And("^close transportnsw$")
	public static void cucumber() throws Exception
	{
		logInfo("And close transportnsw");
		testMain();
	}
	
	public static void testMain(
	) throws Exception
	{
		UI.transportnsw.Close.testMain();
	}
	
	public static void main(String[] args) throws Exception
	{
		testMain();
	}
	
}
