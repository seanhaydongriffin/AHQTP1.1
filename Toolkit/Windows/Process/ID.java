package Toolkit.Windows.Process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import static Toolkit.Windows.Selenium.Test_Script.*;

public class ID 
{
	
	public static long get_current()
	{
		
	    // Note: may fail in some JVM implementations
	    // therefore fallback has to be provided

	    // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
	    final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
	    final int index = jvmName.indexOf('@');

	    if (index < 1) {
	        // part before '@' empty (index = 0) / '@' not found (index = -1)
	        return -1;
	    }

	    try {
	        return Long.parseLong(jvmName.substring(0, index));
	    } catch (NumberFormatException e) {
	        // ignore
	    }
	    return -1;
	}

}
