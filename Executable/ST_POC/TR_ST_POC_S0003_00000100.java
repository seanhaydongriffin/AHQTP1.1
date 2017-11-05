package Executable.ST_POC;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;






import java.awt.Robot;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;


import Toolkit.Windows.File;



import autoitx4java.AutoItX;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TR_ST_POC_S0003_00000100
{
	public static void testMain() throws Exception
	{
		Hashtable<String, String>      	trip_request			 = null;

		logInfo("PRE-CONDITIONS");

		logInfo("PR1. Get a Trip Request from the datapool");	
		
		trip_request = FA.transportnsw.Trip_Request.read.testMain(
			true,        					        // reset DP 
			"Request 1",							// Comment 1
			Test_Environment.test_case_name			// Assigned to the current test case
		);

        logInfo("TEST STEPS");
        
        logInfo("A1. Plan a trip between two locations and arriving before a given date time.");   

        FA.transportnsw.Trip_Request.Submit.from_Windows.testMain(
        	trip_request
        );
        
        logInfo("R1. A list of trips should be provided.");   

        UI.transportnsw.Plan.Trip_planner.Verify_Trip_Result_1_exists.testMain();

        logInfo("A2. Close the portal.");   

        FA.transportnsw.Close.testMain();
        
        logInfo("R2. Verify the portal is closed.");   

        logInfo("No verification.");
   
	}

	public static void main(String[] args) throws Exception
	{
		
		set_wait_for_object_timeout(120);
		set_logging_level(1);
		testMain();
	}

}
