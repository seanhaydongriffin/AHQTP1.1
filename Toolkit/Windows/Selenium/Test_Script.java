package Toolkit.Windows.Selenium;

import java.awt.Dimension;
import java.util.Map;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.RandomStringUtils;

import Toolkit.Windows.Selenium.Datapool.Test_Environment;

public class Test_Script
{
	private static long startTime = 0;
	private static HashMap<String, Date> TimerMap = new HashMap<String, Date>();
	private static int wait_for_object_timeout = 60;
	private static int logging_level = 1;
	private static boolean debugging = false;

	public static void logMessage(String message_type, String message)
	{
        try 
        {
			String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			System.out.println(now + " [" + message_type + "] " + message);
			String log_xml = "";
			
			if (!Test_Environment.local_logs_path.equals(""))
				
				if (message.equals("screenshot"))
				{
//					BrowserTestObject.wait_until_ready();
					Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
					Rectangle screenRectangle = new Rectangle(screenSize);
					Robot robot = new Robot();
					BufferedImage image = robot.createScreenCapture(screenRectangle);
	
					// reduce the size of the bufferedimage a little
		            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
	            	Graphics2D g = indexedImage.createGraphics();
	            	g.drawImage(image, 0,0,null);
		            
//		            com.idrsolutions.image.png.PngEncoder  pngEncoder =  new com.idrsolutions.image.png.PngEncoder();
					
					// look for the next screenshot filename
					int screenshot_file_num = 1;
					
					while (Toolkit.Windows.File.exists(Test_Environment.local_logs_path + File.separator + screenshot_file_num + ".png"))
						screenshot_file_num++;
										
					// use JDeli's pngencoder for much fast PNG encoding than imageIO above
//		            pngEncoder.write(indexedImage, new FileOutputStream(Test_Environment.local_logs_path + File.separator + screenshot_file_num + ".png"));
	
					log_xml = 	"<msg><date_time>" + now + "</date_time><result>" + message_type + "</result><text>Screenshot " + screenshot_file_num + ".png</text></msg>\r\n</log>";
					
					Toolkit.Windows.File.remove_bytes_from_end(Test_Environment.local_logs_path + File.separator + "log.xml", 6);
					Toolkit.Windows.File.append(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
				} else
					
					if (message.equals("screenshot browser"))
					{
						// look for the next screenshot filename
						int screenshot_file_num = 1;
						
						while (Toolkit.Windows.File.exists(Test_Environment.local_logs_path + File.separator + screenshot_file_num + ".png"))
							
							screenshot_file_num++;
											
						BrowserTestObject.takeScreenShot(Test_Environment.local_logs_path, screenshot_file_num + ".png");
		
						log_xml = 	"<msg><date_time>" + now + "</date_time><result>" + message_type + "</result><text>Screenshot " + screenshot_file_num + ".png</text></msg>\r\n</log>";
						
						Toolkit.Windows.File.remove_bytes_from_end(Test_Environment.local_logs_path + File.separator + "log.xml", 6);
						Toolkit.Windows.File.append(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
					} else
						
						if (message.equals("screenshot android"))
						{
							// look for the next screenshot filename
							int screenshot_file_num = 1;
							
							while (Toolkit.Windows.File.exists(Test_Environment.local_logs_path + File.separator + screenshot_file_num + ".png"))
								
								screenshot_file_num++;
												
//							AndroidDeviceTestObject.takeScreenShot(Test_Environment.local_logs_path, screenshot_file_num + ".png");
			
							log_xml = 	"<msg><date_time>" + now + "</date_time><result>" + message_type + "</result><text>Screenshot " + screenshot_file_num + ".png</text></msg>\r\n</log>";
							
							Toolkit.Windows.File.remove_bytes_from_end(Test_Environment.local_logs_path + File.separator + "log.xml", 6);
							Toolkit.Windows.File.append(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
						} else
							
							if (message.equals("screenshot ios"))
							{
								// look for the next screenshot filename
								int screenshot_file_num = 1;
								
								while (Toolkit.Windows.File.exists(Test_Environment.local_logs_path + File.separator + screenshot_file_num + ".png"))
									
									screenshot_file_num++;
													
//								iOSDeviceTestObject.takeScreenShot(Test_Environment.local_logs_path, screenshot_file_num + ".png");
				
								log_xml = 	"<msg><date_time>" + now + "</date_time><result>" + message_type + "</result><text>Screenshot " + screenshot_file_num + ".png</text></msg>\r\n</log>";
								
								Toolkit.Windows.File.remove_bytes_from_end(Test_Environment.local_logs_path + File.separator + "log.xml", 6);
								Toolkit.Windows.File.append(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
							} else
								
								if (!message_type.equals("DEBUG"))
								{
									message = message.replaceAll("&", "amp;");
									log_xml = 	"<msg><date_time>" + now + "</date_time><result>" + message_type + "</result><text>" + message + "</text></msg>\r\n</log>";
					
									Toolkit.Windows.File.remove_bytes_from_end(Test_Environment.local_logs_path + File.separator + "log.xml", 6);
									Toolkit.Windows.File.append(Test_Environment.local_logs_path + File.separator + "log.xml", log_xml);
								}
			
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}
	
	public static void logInfo(String message)
	{
        try 
        {
        	logMessage("INFO", message);
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}
	
	public static void logDebug(String message)
	{
        try 
        {
        	logMessage("DEBUG", message);
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}
	
	public static void logVerification(String message)
	{
        try 
        {
        	logMessage("VERIFICATION", message);
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}

	public static void logFail(String message)
	{
        try 
        {
        	logMessage("FAIL", message);
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}
	
	public static void log_test_result(String vp_name, String expected, String actual)
	{
		String test_result = "";
		
        try 
        {
        	if (actual.equals(expected))
        		
        		test_result = "PASS";
        	else

        		test_result = "FAIL";

        	logMessage(test_result, "Verification Point \"" + vp_name + "\", Expected = " + expected + ", Actual = " + actual);
			
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}

	public static void sleep(double seconds)
	{
        try
        {
        	logInfo("Sleeping for " + seconds + " seconds ...");
			Thread.sleep((long)(seconds * 1000));
		} catch (InterruptedException e)
        {
			e.printStackTrace();
		}  
	}

	public static void sleep(double seconds, boolean log_debug)
	{
        try
        {
        	if (log_debug)

        		logDebug("Sleeping for " + seconds + " seconds ...");
        	else
        		
        		logInfo("Sleeping for " + seconds + " seconds ...");
        	
			Thread.sleep((long)(seconds * 1000));
		} catch (InterruptedException e)
        {
			e.printStackTrace();
		}  
	}

	public static void stop()
	{
    	logInfo("stop() called.  See below stack trace ...");
    	
		String result = "";
		
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			
			if (ste.getClassName().startsWith("Executable.") ||
				ste.getClassName().startsWith("FA.") ||
				ste.getClassName().startsWith("UI."))
			{
				if (result.length() > 0)
					
					result = result + '\n';
				
				result = result + ste;
			}
		}

		System.out.println(result);

        System.exit(0);
	}
	
	public static String start_timer()
	{
		String timerName = "Timer" + RandomStringUtils.randomNumeric(6);
		start_timer(timerName);
		return timerName;
	}
	
	public static void start_timer(String timerName)
	{
		TimerMap.put(timerName, new Date());
	}
	
	public static long get_timer(String timerName)
	{
		Date startTime = TimerMap.get(timerName);
		Date now = new Date();
		long duration = (now.getTime() - startTime.getTime())/1000;
		return duration;
	}

    public static void start_debugging()
    {
    	Test_Script.debugging = true;
    }

    public static void stop_debugging()
    {
    	Test_Script.debugging = false;
    }

    public static boolean get_debugging()
    {
    	return Test_Script.debugging;
    }
	
	public static void information_message(String message, boolean always_on_top)
	{
 		JFrame frmOpt = new JFrame();

	    frmOpt.setVisible(true);
    	frmOpt.setLocation( 400,400);
	    
	    if (always_on_top)
	    
	    	frmOpt.setAlwaysOnTop(true);
		
	    JOptionPane.showMessageDialog(frmOpt, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	    frmOpt.dispose();		
	}
	
	public static void log_screen()
	{
		logInfo("screenshot browser");
		
		try
		{
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void set_wait_for_object_timeout(int secs)
	{
		wait_for_object_timeout = secs;
	}
	
	public static void set_logging_level(int logLevel)
	{
		logging_level = logLevel;
	}
	
	public static int get_logging_level()
	{
		return logging_level;
	}
	
	public static int get_wait_for_object_timeout()
	{
		return wait_for_object_timeout;
	}
	
	
	
}
