package Toolkit.Windows.Selenium.Project.Script;

import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import static Toolkit.Windows.Selenium.Test_Script.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

public aspect Test_Case_Script
{

	pointcut main() : execution(public static void main(..));

	// When main is executed ...
	
	before() : main()
	{
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
	
			Toolkit.Windows.File.overwrite(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
		}
		
		// If a Test Case Script, then restart the Selenium Node (because existing nodes sometimes fail) minimised, if it exists
/*
		if (Test_Environment.test_case_name.length() > 0)
		{
			logInfo("Restarting the Selenium Node process");
	
	    	String timerName = start_timer();
	
			while (get_timer(timerName) < get_wait_for_object_timeout())
			{
				boolean selenium_node_exists = Toolkit.Windows.Process.CommandLine.exists("selenium-server-standalone-3.0.0-beta2.jar -role node");
			
				if (!selenium_node_exists)
					
					break;
				
				Toolkit.Windows.Process.CommandLine.terminate("selenium-server-standalone-3.0.0-beta2.jar -role node");
			}
			
			try {
			
				Runtime.getRuntime().exec(
					"cmd.exe /c start /MIN C:\\ProgramData\\Oracle\\Java\\javapath\\java.exe -Xmx512m -jar selenium-server-standalone-3.0.0-beta2.jar -role node -hub http://localhost:4444/grid/register",
					null,
					new File("C:\\Selenium")
				);
			} catch (IOException e1)
			{
				logFail("Failed to start the Selenium Node process. Stopping run.");
			}
		}
*/
		// Cleanup old Selenium temporary files from previous runs

		logInfo("Removing old temporary Selenium files");

		String user_temp_folder = System.getProperty("user.home") + "\\AppData\\Local\\Temp";

		try
		{
			File directory = new File(user_temp_folder);
			File[] subdirs = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
			
			if (subdirs != null)
	
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
	}
		
	public static String read_name()
	{
		
		String result = "";
		
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			
			if (ste.getClassName().startsWith("Executable."))
			{
				result = ste.getClassName();
				result = result.substring(result.lastIndexOf(".") + 1);
				break;
			}
		}

		return result;
	}
	
	public static String read_full_name()
	{
		
		String result = "";
		
		// If this test is a Cucumber Feature file, then we can only get that feature file name from the command line of the running process
		long pid = Toolkit.Windows.Process.ID.get_current();
		String commandline = Toolkit.Windows.Process.CommandLine.get(pid);
		
		if (commandline.contains(" cucumber.api.cli.Main "))
		{
			String file_separator = "\\";
			result = StringUtils.substringBetween(commandline, file_separator + "Executable" + file_separator, "\"");
			
			if (result == null)
			{
				file_separator = "/";
				result = StringUtils.substringBetween(commandline, file_separator + "Executable" + file_separator, "\"");
			}
			
			result = "Executable" + file_separator + result;
			result = result.replace(file_separator, ".");
			result = result.replace(".feature", "");
		} else
	
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
