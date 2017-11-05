package Toolkit.Windows.Process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static Toolkit.Windows.Selenium.Test_Script.*;

public class Name 
{


	public static boolean exists(String process_name)
	{
        boolean result = false;
		
	    try 
	    {
	        String line;

            Process p = Runtime.getRuntime().exec("wmic PROCESS where name='" + process_name + "' get Processid");
            p.getOutputStream().close();
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            while ((line = br.readLine()) != null) {

            	if (line.indexOf("ProcessId") > -1)
            	{
            		result = true;
            		break;
            	}
//            	System.out.println(line);
            }

            if (br != null) br.close();
            if (isr != null) isr.close();
            if (is != null) is.close();
	    } catch (Exception e) {}
		
	    return result;
	}


	public static boolean terminate(String process_name)
	{
        boolean result = false;
		
        logInfo("Attempting to terminate process \"" + process_name + "\" ...");

	    try
	    {
	        String line;

            Process p = Runtime.getRuntime().exec("wmic PROCESS where name='" + process_name + "' call terminate");
            p.getOutputStream().close();
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            while ((line = br.readLine()) != null)
            {
//            	System.out.println(line);
            }

            if (br != null) br.close();
            if (isr != null) isr.close();
            if (is != null) is.close();
            
            result = true;
	    } catch (Exception e) {}
		
	    return result;

	}
}
