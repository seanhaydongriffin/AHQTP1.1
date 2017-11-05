package Toolkit.Windows.Process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static Toolkit.Windows.Selenium.Test_Script.*;

public class CommandLine 
{
	
	public static String get(long pid)
	{
        String line = "";
		
	    try 
	    {

            Process p = Runtime.getRuntime().exec("wmic PROCESS where \"ProcessId = " + pid + "\" get CommandLine");
            p.getOutputStream().close();
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int line_num = 0;
            
            while ((line = br.readLine()) != null) {

            	line_num++;
            	
            	if (line_num > 1 && !line.equals(""))
            		
            		break;
            }

            if (br != null) br.close();
            if (isr != null) isr.close();
            if (is != null) is.close();
	    } catch (Exception e) {}
		
	    return line;
	}

	
	
	public static boolean exists(String process_command_line)
	{
        boolean result = false;
		
	    try 
	    {
	        String line;

            Process p = Runtime.getRuntime().exec("wmic PROCESS where \"name!='wmic.exe' AND CommandLine like '%" + process_command_line + "%'\" get Processid");
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


	public static boolean terminate(String process_command_line)
	{
        boolean result = false;
		
        logInfo("Attempting to terminate process with command line \"" + process_command_line + "\" ...");

	    try
	    {
	        String line;

            Process p = Runtime.getRuntime().exec("wmic PROCESS where \"name!='wmic.exe' AND CommandLine like '%" + process_command_line + "%'\" call terminate");
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
