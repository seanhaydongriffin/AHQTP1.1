package FA.transportnsw.Trip_Request.Submit;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import java.util.Hashtable;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;

public class from_Windows 
{
	@When("\"([^\"]*)\" executes a trip plan$")
	public static void cucumberMain(String comment1_text) throws Exception
	{
		logInfo("When " + comment1_text + " executes a trip plan");

		Hashtable<String, String> data1 = Toolkit.Windows.Cucumber.data.get(comment1_text);
		
		testMain(
			data1
		);
	}

	public static void testMain(
		Hashtable<String, String> trip_request
	) throws Exception
	{
        UI.transportnsw.Home.from_Windows.testMain(
        	Test_Environment.test_env
        );
        
        UI.transportnsw.Plan.Trip_planner.from_Home.testMain();
        
        UI.transportnsw.Plan.Trip_planner.Go.testMain(
        	trip_request.get("From"),
        	trip_request.get("To"),
        	trip_request.get("time option"),
        	trip_request.get("Search Date"),
        	trip_request.get("Search Hour"),
        	trip_request.get("Search Minute")
        );
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
