package UI.transportnsw.Home;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import java.util.Hashtable;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;

public class from_Windows 
{
	public static void testMain(
		String test_env
	)
	{
		Hashtable<String, String> 	parameter_search 		= new Hashtable(),
									browser_search 			= new Hashtable(),
									url_result 				= new Hashtable(),
									browser_result 			= new Hashtable();
		String 						browser_profile_path	= null;
		
		// Get the application URL
		
		parameter_search.put("Parameter Name", "transportnsw URL");
		
		url_result = Toolkit.Windows.Selenium.Datapool.Row.search(
			test_env + " - Parameters", 
			parameter_search, 
			"0"
		);
		
		// Get the targeted browser
		
		browser_search.put("Comment 1", "Browser 1");
		browser_search.put("Assigned to", Test_Environment.test_case_name);

		browser_result = Toolkit.Windows.Selenium.Datapool.Row.search(
			test_env + " - Browser", 
			browser_search, 
			"0"
		);

		Test_Environment.browser_name = browser_result.get("Browser");
		
		if (System.getProperty("os.name").contains("Windows"))
		{		
			if (Test_Environment.browser_name.equals("Firefox"))
				
				browser_profile_path = "C:\\Selenium\\Firefox\\Profiles\\";
			
			if (Test_Environment.browser_name.equals("Chrome"))
				
				browser_profile_path = "C:\\Selenium\\Chrome\\Profiles\\";
		} else
		{
			if (Test_Environment.browser_name.equals("Firefox"))

				browser_profile_path = "/Users/admin/Selenium/Firefox/Profiles/";
			
			if (Test_Environment.browser_name.equals("Chrome"))
				
				browser_profile_path = "/Users/admin/Selenium/Chrome/Profiles/";
		}
		
		BrowserTestObject.start(
			Test_Environment.selenium_hub_url,
			browser_result.get("Browser"),
			browser_result.get("Browser Version"),
			browser_result.get("Operating System"),
			browser_result.get("Operating System Version"),
			browser_result.get("Resolution"),
			url_result.get("Parameter Value"),
			browser_profile_path + browser_result.get("Profile Folder"),
			false,
			"false"
		);
	
		// Resize Browser
		BrowserTestObject.get_current().manage().window().setSize(new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width - (20*2), java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 250));
		BrowserTestObject.get_current().manage().window().setPosition(new Point(20, 0));
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}

}
