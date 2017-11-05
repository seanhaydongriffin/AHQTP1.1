package Toolkit.Windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Toolkit.Windows.Selenium.Test_Script;
import Toolkit.Windows.Selenium.WebTestObject;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import static Toolkit.Windows.Selenium.Test_Script.*;

public class Cucumber
{
	public static Hashtable<String, Hashtable<String, String>> 	data = new Hashtable<String, Hashtable<String, String>>();

	@And("^I get the \"([^\"]*)\" data with comment \"([^\"]*)\" for scenario \"([^\"]*)\"$")
	public static void read_data(String datapool_name, String comment_text, String scenario_name) throws Exception
	{
		logInfo("And I get the " + datapool_name + " data with comment " + comment_text + " for scenario " + scenario_name);

		Hashtable<String, String> 	result        	= new Hashtable(),
				datapool_search = new Hashtable();
		
		// Reset the datapool cursor for this test case
		
		Toolkit.Windows.Selenium.Datapool.Cursor.reset(
			datapool_name
		);
	
		datapool_search.put("Comment 1", comment_text);
		datapool_search.put("Assigned to", scenario_name);
		
		result = Toolkit.Windows.Selenium.Datapool.Row.search(
			Test_Environment.test_env + " - " + datapool_name, 
			datapool_search, 
			""
		);
		
		if (result == null)
		
			logInfo("Failed to read from datapool \"" + Test_Environment.test_env + " - " + datapool_name + "\"");
		else	
		{
			logInfo("Read Comment 1 \"" + result.get("Comment 1") + "\" from datapool \"" + Test_Environment.test_env + " - " + datapool_name + "\"");
			logInfo("Read Assigned to \"" + result.get("Assigned to") + "\" from datapool \"" + Test_Environment.test_env + " - " + datapool_name + "\"");
		}
	
		data.put(comment_text, result);
		
	}
	
	@Given("^this is the first scenario$")
	public static void startup() throws Exception
	{
		logInfo("Given this is the first scenario");
		
		// Read all the details about the test environment, with static details accessible to any script during the run
		Toolkit.Windows.Selenium.Datapool.Test_Environment.read();
		
		// Setup the log for the run
		
		if (!Test_Environment.local_logs_path.equals(""))
		{
			if (!Toolkit.Windows.Directory.exists(Test_Environment.local_logs_path))
	
				Toolkit.Windows.Directory.create(Test_Environment.local_logs_path);
		
			String log_xml = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
								"<log>\r\n" +
								"<script_name>" + Test_Environment.test_case_script_full_name + "</script_name>\r\n" +
								"<start_date_time>07 Sep 2016 10:00:00</start_date_time>\r\n" +
								"<end_date_time>07 Sep 2016 12:00:00</end_date_time>\r\n" +
								"<result>PASS</result>\r\n" +
								"<first_exception_message>bad</first_exception_message>\r\n" + 
								"</log>";
	
			Toolkit.Windows.File.overwrite(Test_Environment.local_logs_path + "\\log.xml", log_xml);
		}
		
		// Cleanup old Selenium temporary files from previous runs

		logInfo("Removing old temporary Selenium files");

		String user_temp_folder = System.getProperty("user.home") + "\\AppData\\Local\\Temp";

		try
		{
			File directory = new File(user_temp_folder);
			File[] subdirs = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
			
			for (File dir : subdirs)
			{
				String dir_name = dir.getName(); 
				
				if (dir_name.matches("^webdriver.*") || 
					dir_name.matches("^.*webdriver-profile$") ||
					dir_name.matches("^rust_mozprofile.*"))
				
					FileUtils.deleteDirectory(dir);
			}
		} catch (IOException e)
		{
		}
		
		// Fix the Chrome profile preferences to stop crashing prompts
		
		try {
			FileUtils.copyFile(new File("C:\\Selenium\\Chrome\\Profiles\\Preferences"), new File("C:\\Selenium\\Chrome\\Profiles\\xxxxxxxx\\Default\\Preferences"));
		} catch (IOException e) 
		{
		}
	}
	

	
}
