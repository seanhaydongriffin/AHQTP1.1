package Toolkit.Windows.Selenium.Datapool;
import static Toolkit.Windows.Selenium.Test_Script.*;

import java.io.File;
import java.util.Hashtable;

public class Test_Environment
{
	public static String project_path = "";					// The absolute Path of the Project folder
	public static String data_path = "";					// The absolute Path of the Data folder
	public static String pools_path = "";					// The absolute Path of the Pools folder
	public static String cursors_path = "";					// The absolute Path of the Cursors folder
	public static String local_logs_path = "";				// The absolute Path of the Local Logs folder
	public static String remote_logs_path = "";				// The absolute Path of the Remote Logs folder
	public static String release = "";						// Not used
	public static String test_env = "";						// The Name of the Test Environment
	public static String proxy_scheme = "";			// The Scheme of the Proxy Server
	public static String proxy_host = "";				// The Hostname of the Proxy Server
	public static String proxy_port = "";				// The Port Number of the Proxy Server
	public static String proxy_username = "";			// A valid Username for the Proxy Server
	public static String proxy_password = "";			// A valid Password for the Proxy Server
	public static String browser_name = "";					// The Name of the Browser to automate
	public static String test_case_name = "";				// The Name of the current Test Case
	public static String test_case_script_full_name = "";	// The Full Name of the current Test Case Script
	public static String selenium_hub_url = "";
	
	public static void read()
	{
		Hashtable 					datapool_search	= new Hashtable();
		Hashtable<String, String>	result			= new Hashtable();

		// Get the project path
		project_path = Toolkit.Windows.Selenium.Project.Path.read();
		data_path = project_path + File.separator + "Data";
		pools_path = data_path + File.separator + "Pools";
		cursors_path = data_path + File.separator + "Pools" + File.separator + "Cursors";
     
		// Get the test environment
		
		datapool_search.put("Parameter Name", "Test Environment");
		
		result = Toolkit.Windows.Selenium.Datapool.Row.search(
			"Release Parameters", 
			datapool_search, 
			"0"
		);
     
		test_env = result.get("Parameter Value");
     
		// Get the selenium hub url
		
		datapool_search.put("Parameter Name", "Selenium Hub URL");
		
		result = Toolkit.Windows.Selenium.Datapool.Row.search(
			"Release Parameters", 
			datapool_search, 
			"0"
		);
     
		selenium_hub_url = result.get("Parameter Value");
     
        // Get the test case name and script name
		test_case_name = Toolkit.Windows.Selenium.Project.Script.Test_Case.read_name();
		test_case_script_full_name = Toolkit.Windows.Selenium.Project.Script.Test_Case_Script.read_full_name();

        // Get the test case script log paths

		if (!test_case_script_full_name.equals(""))
			
			local_logs_path = project_path + "_local_logs" + File.separator + test_case_script_full_name;

	}
	
	public static String read_full_name()
	{
		
		String result = "";
		
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			
			if (ste.getClassName().startsWith("Executable."))
			{
				result = ste.getClassName();
				break;
			}
		}

		return result;
	}

}
