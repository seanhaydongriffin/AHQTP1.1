package Toolkit.Windows.Selenium.Project.Script;
import static Toolkit.Windows.Selenium.Test_Script.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class Test_Case
{
	
	public static String read_name()
	{
		String result = "";
		
		// If this test is a Cucumber Feature file, then we can only get that feature file name from the command line of the running process
		long pid = Toolkit.Windows.Process.ID.get_current();
		String commandline = Toolkit.Windows.Process.CommandLine.get(pid);
		
		if (commandline.contains(" cucumber.api.cli.Main "))
		{
			result = StringUtils.substringBetween(commandline, " cucumber.api.cli.Main \"", "\"");
			result = FilenameUtils.getBaseName(result);
			result = result.substring(result.lastIndexOf(".") + 1);
			result = result.substring(0, result.length() - 9);
		} else
		{
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				
				if (ste.getClassName().startsWith("Executable.Datapool_Setup_Porting"))
				{
					result = "Datapool_Setup_Porting";
				} else
					
					if (ste.getClassName().startsWith("Executable."))
					{
						result = ste.getClassName();
						result = result.substring(result.lastIndexOf(".") + 1);
						result = result.substring(0, result.length() - 9);
					}
			}
		}

		return result;
	}

}
