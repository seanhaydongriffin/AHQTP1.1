package Toolkit.Windows.Selenium;
import static Toolkit.Windows.Selenium.Test_Script.get_timer;
import static Toolkit.Windows.Selenium.Test_Script.get_wait_for_object_timeout;
import static Toolkit.Windows.Selenium.Test_Script.logDebug;
import static Toolkit.Windows.Selenium.Test_Script.sleep;
import static Toolkit.Windows.Selenium.Test_Script.start_timer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Toolkit.Windows.Selenium.Datapool.Test_Environment;

public class BrowserTestObject extends RemoteWebDriver
{
//	public static WebDriver current_driver;
	//public static WebDriver driver2;
	public static BrowserTestObject current_driver = null;
	public static BrowserTestObject driver2 = null;
	public static String current_hub_url = "";
	public static DesiredCapabilities current_capability = null;

	
    public BrowserTestObject(CommandExecutor executor, DesiredCapabilities capabilities) 
    {
    	super(executor, capabilities);
    }

    // Attach to an existing driver session in an existing Selenium Hub

	public static BrowserTestObject createDriverFromSession(URL command_executor, final String sessionId){
	    CommandExecutor executor = new HttpCommandExecutor(command_executor) {

	    @Override
	    public Response execute(Command command) throws IOException {
	        Response response = null;
	        if (command.getName() == "newSession") {
	            response = new Response();
	            response.setSessionId(sessionId.toString());
	            response.setStatus(0);
	            response.setValue(Collections.<String, String>emptyMap());

	            try {
	                Field commandCodec = null;
	                commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
	                commandCodec.setAccessible(true);
	                commandCodec.set(this, new W3CHttpCommandCodec());

	                Field responseCodec = null;
	                responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
	                responseCodec.setAccessible(true);
	                responseCodec.set(this, new W3CHttpResponseCodec());
	            } catch (NoSuchFieldException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }

	        } else {
	            response = super.execute(command);
	        }
	        return response;
	    }
	    };
	    
	    BrowserTestObject fred = new BrowserTestObject(executor, new DesiredCapabilities());
	    
	    return fred;
	    
//	    return new RemoteWebDriver(executor, new DesiredCapabilities());
	}
	
	

	public static void set_current_driver_if_not_set(boolean minimise_eclipse)
	{
		if (current_driver == null)
		{
			String jus_current_driver_session_filename = Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "driver_session.cur";
			String jus_current_driver_capability_filename = Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "driver_capability.cur";
			String jus_current_selenium_hub_filename = Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "selenium_hub.cur";
			
			if (Toolkit.Windows.File.exists(jus_current_driver_session_filename) &&
				Toolkit.Windows.File.exists(jus_current_driver_capability_filename) &&
				Toolkit.Windows.File.exists(jus_current_selenium_hub_filename))
			{
				try
				{
			//		current_hub_url = Toolkit.Windows.File.read(jus_current_selenium_hub_filename);
					
					if (Toolkit.Windows.File.read(jus_current_driver_capability_filename).equals("firefox"))
						current_capability = DesiredCapabilities.firefox();
					
					String jus_current_driver_session_id = Toolkit.Windows.File.read(jus_current_driver_session_filename);
					
				//	current_driver = createDriverFromSession("57decfb6-4ca6-4540-b9b5-96a3b31c52d4", new URL("http://localhost:4444/wd/hub"));
//					current_driver = createDriverFromSession(new URL(current_hub_url), jus_current_driver_session_id);
					current_driver = createDriverFromSession(new URL(Test_Environment.selenium_hub_url), jus_current_driver_session_id);

					
					
				//	current_driver = new BrowserTestObject(new URL(current_hub_url), jus_current_driver_session_id);
			//		current_driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
			    	System.out.println("Attached to existing driver session with id " + jus_current_driver_session_id);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	// Starts a new driver session, of the specified capability, in an existing Selenium Hub
    public BrowserTestObject(URL selenium_hub_url, DesiredCapabilities capability) 
    {
        super(selenium_hub_url, capability);
    }

    // Attach to an existing driver session in an existing Selenium Hub
    public BrowserTestObject(URL selenium_hub_url, String driver_sessionId) 
    {

        super();
        setSessionId(driver_sessionId);
        
        setCommandExecutor(new HttpCommandExecutor(selenium_hub_url) 
	        {
	            @Override
	            public Response execute(Command command) throws IOException 
	            {
	                if (command.getName() != "newSession") 
	                {
	                    return super.execute(command);
	                }
	                return super.execute(new Command(getSessionId(), "getCapabilities"));
	            }
	        }
        );
        startSession(new DesiredCapabilities());
        
    }

	public static void set_current_hub(String selenium_hub_url)
	{
		current_hub_url = selenium_hub_url;
		Toolkit.Windows.File.overwrite(Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "selenium_hub.cur", current_hub_url);
	}

	public static void set_current_capability(DesiredCapabilities capability)
	{
		current_capability = capability;
		Toolkit.Windows.File.overwrite(Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "driver_capability.cur", current_capability.getBrowserName());
	}
    
	public static void start(String url_to_visit)
	{
		try
		{
			current_driver = new BrowserTestObject(new URL(url_to_visit), DesiredCapabilities.firefox());
			current_driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			current_driver.get(url_to_visit);
		
	    	System.out.println("Starting current driver session with id " + current_driver.getSessionId());
			Toolkit.Windows.File.overwrite(Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "driver_session.cur", current_driver.getSessionId().toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    
	public static void start(String selenium_hub_url, String browser_name, String browser_version, String os, String os_version, String resolution, String url_to_visit, String profile_to_use, boolean clear_browser_data, String browserstack_debug)
	{
		if (selenium_hub_url == null || selenium_hub_url.equals(""))

			selenium_hub_url = "http://localhost:4444/wd/hub";

		if (browserstack_debug == null && browserstack_debug.equals(""))
			
			browserstack_debug = "false";

		// Kill any pre-existing geckodriver processes from old Firefox instances
		while (Toolkit.Windows.Process.Name.exists("geckodriver.exe"))
			
			Toolkit.Windows.Process.Name.terminate("geckodriver.exe");

		while (Toolkit.Windows.Process.Name.exists("chromedriver.exe"))
			
			Toolkit.Windows.Process.Name.terminate("chromedriver.exe");

		while (Toolkit.Windows.Process.Name.exists("IEDriverServer.exe"))
			
			Toolkit.Windows.Process.Name.terminate("IEDriverServer.exe");

		DesiredCapabilities capability = null;

		if (browser_name.equals("Firefox"))
			
			capability = DesiredCapabilities.firefox();
		
		if (browser_name.equals("Chrome"))
			
			capability = DesiredCapabilities.chrome();
		
		if (browser_name.equals("IE"))
			
			capability = DesiredCapabilities.internetExplorer();
		
		if (browser_name.equals("Safari"))
			
			capability = DesiredCapabilities.safari();
		
		if (browser_name.equals("Opera"))
			
			capability = DesiredCapabilities.opera();
		
		set_current_capability(capability);

		if (browser_version != null && !browser_version.equals(""))
			
			capability.setCapability("browser_version", browser_version);
		
		if (os != null && !os.equals(""))
		
			capability.setCapability("os", os);
		
		if (os_version != null && !os_version.equals(""))

			capability.setCapability("os_version", os_version);
		
		if (resolution != null && !resolution.equals(""))

			capability.setCapability("resolution", resolution);
		
//		if (browserstack_debug != null && !browserstack_debug.equals(""))

//			capability.setCapability("browserstack.debug", browserstack_debug);
			capability.setCapability("browserstack.debug", "false");
		
//		if (browser_version != null && !browser_version.equals(""))

			capability.setCapability("browserstack.video", "true");
		
//		if (browser_version != null && !browser_version.equals(""))

//			capability.setCapability("browserstack.geckodriver", "0.18.0");
		
//		if (browser_version != null && !browser_version.equals(""))

//			capability.setCapability("browserstack.selenium_version", "3.5.2");

		try
		{
			// if Internet Explorer browser
			
			if (browser_name.equals("IE"))
			{
				current_driver = new BrowserTestObject(new URL(selenium_hub_url), capability);
			}

			// if Chrome browser

			if (browser_name.equals("Chrome"))
			{
				ChromeOptions options = new ChromeOptions();
				
				if (!profile_to_use.equals(""))
					
					options.addArguments("user-data-dir=" + profile_to_use);
				
				// Fix the Chrome profile preferences to stop crashing prompts
				String profile_preferences = Toolkit.Windows.File.read(profile_to_use + "\\Default\\Preferences");
				profile_preferences = profile_preferences.replace("\"exit_type\":\"Crashed\"", "\"exit_type\":\"Normal\"");
				Toolkit.Windows.File.overwrite(profile_to_use + "\\Default\\Preferences", profile_preferences);
					
				options.addArguments("disable-infobars");
		//		options.addExtensions(new File("C:" + File.separator + "Selenium" + File.separator + "stylish_extension_1_8_2.crx"));
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				current_driver = new BrowserTestObject(new URL(selenium_hub_url), capability);
			}
			
			// if Firefox browser

			if (browser_name.equals("Firefox"))
			{
				FirefoxProfile profile = null;
				
				if (browserstack_debug.equals("false"))
				{	
					File profileDirectory = new File(profile_to_use);
					profile = new FirefoxProfile(profileDirectory);
				} else

					profile = new FirefoxProfile();

				FirefoxOptions options = new FirefoxOptions();
				options.setProfile(profile);
				options.addTo(capability);
				
				profile.setPreference("browser.download.folderList",2);
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				profile.setPreference("browser.download.manager.closeWhenDone", true);
				profile.setPreference("browser.download.manager.showAlertOnComplete", false);
				profile.setPreference("browser.download.manager.useWindow", false);
				profile.setPreference("browser.download.dir","r:" + File.separator);
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/csv");
				capability.setCapability(FirefoxDriver.PROFILE, profile);
				capability.setCapability("browser", "Firefox");
				
				if (current_hub_url.equals(""))
					
					current_driver = new BrowserTestObject(new URL(selenium_hub_url), capability);
				else
					
					current_driver = new BrowserTestObject(new URL(current_hub_url), capability);
			}
			
			// if Safari browser
			
			if (browser_name.equals("Safari"))
			{
				current_driver = new BrowserTestObject(new URL(selenium_hub_url), capability);
			}
			
			// if Opera browser
			
			if (browser_name.equals("Opera"))
			{
				current_driver = new BrowserTestObject(new URL(selenium_hub_url), capability);
			}
			
			current_driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			if (clear_browser_data)
			{
				if (browser_name.equals("Chrome"))
	
					Toolkit.Windows.Chrome.clear_browser_data();
			}
			
			current_driver.get(url_to_visit);
	
	    	System.out.println("Starting current driver session with id " + current_driver.getSessionId());
			Toolkit.Windows.File.overwrite(Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "driver_session.cur", current_driver.getSessionId().toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    
	public static void start(DesiredCapabilities capability, String url_to_visit, String firefox_profile_to_use, String proxy_server, int proxy_port)
	{
		// Kill any pre-existing geckodriver processes from old Firefox instances
		while (Toolkit.Windows.Process.Name.exists("geckodriver.exe"))
			Toolkit.Windows.Process.Name.terminate("geckodriver.exe");
		
		set_current_capability(capability);
		try
		{
			File profileDirectory = new File(firefox_profile_to_use);
			FirefoxProfile profile = new FirefoxProfile(profileDirectory);
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", "websense.telecom.tcnz.net");
			profile.setPreference("network.proxy.http_port", 8080);
			profile.setPreference("network.proxy.share_proxy_settings", true);
			capability.setCapability(FirefoxDriver.PROFILE, profile);
	
			if (current_hub_url.equals(""))
				current_driver = new BrowserTestObject(new URL("http://localhost:4444/wd/hub"), capability);
			else
				current_driver = new BrowserTestObject(new URL(current_hub_url), capability);
			
			current_driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			current_driver.get(url_to_visit);
	
	    	System.out.println("Starting current driver session with id " + current_driver.getSessionId());
			Toolkit.Windows.File.overwrite(Toolkit.Windows.Selenium.Data.Path.read() + "\\driver_session.cur", current_driver.getSessionId().toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void load_url(String url)
	{
		current_driver.get(url);
	}
	
	public static WebDriver get_current()
	{
		return current_driver;
	}
	
	public static void navigate_back()
	{
		current_driver.navigate().back();
	}
	
	public static void close_current()
	{
		current_driver.close();
	}
	
	public static void close_all()
	{
		current_driver.manage().deleteAllCookies();
		current_driver.quit();
	}
	
	public static void takeScreenShot(String fname) throws Exception {

	    System.setProperty("selenium.screenshot.dir", "C:/fred");
		
		File scrFile = ((TakesScreenshot)current_driver).getScreenshotAs(OutputType.FILE);
	    String imageFileDir = System.getProperty("selenium.screenshot.dir");
	    System.setProperty("selenium.screenshot.dir", "C:/fred");

    	imageFileDir = "C:/fred";
    	FileUtils.copyFile(scrFile, new File(imageFileDir, fname));
	}
	
	public static void takeScreenShot(String folder_name, String file_name) throws Exception {

	    System.setProperty("selenium.screenshot.dir", folder_name);
		
		File scrFile = ((TakesScreenshot)current_driver).getScreenshotAs(OutputType.FILE);
	    String imageFileDir = System.getProperty("selenium.screenshot.dir");
	    System.setProperty("selenium.screenshot.dir", folder_name);

    	imageFileDir = folder_name;
    	FileUtils.copyFile(scrFile, new File(imageFileDir, file_name));
	}

	// Switch control back to the main browser window
	public static void switch_to_default_content()
	{
		set_current_driver_if_not_set(true);
		current_driver.switchTo().defaultContent();
	}
	
	public static void move_to()
	{
		((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("window.moveTo(0,0);");
	}
	
	public static void move_previous_page()
	{
		((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("window.history.go(-1)");
	}

	public static void wait_until_no_ajax()
	{
		try{
	    	String timerName = start_timer();
	    	long num_ajax_requests = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return jQuery.active;");
	
			while (get_timer(timerName) < get_wait_for_object_timeout() && num_ajax_requests > 0)
			{
				sleep(0.5, true);
				num_ajax_requests = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return jQuery.active;");
				logDebug("Number of pending AJAX requests = " + num_ajax_requests);
			}
		}catch (Exception ex){
			
		}
	}

	public static void wait_until_readystate_complete()
	{
		try{
	    	String timerName = start_timer();
	
			while (get_timer(timerName) < get_wait_for_object_timeout())
			{
				sleep(0.5, true);
				String readyState = (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return document.readyState;");
				logDebug("readyState = " + readyState);
				
				if (readyState.equals("complete"))
					
					break;
			}
		}catch (Exception ex){
			ex.printStackTrace();

		}
	}
	
	public static void go_to_Top()
	{
		try{
			((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		} catch (Exception ex){
			
		}
	}

	public static void wait_until_no_animations()
	{
		try{
	    	String timerName = start_timer();
	        long num_animations = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(\":animated\").length;");

			while (get_timer(timerName) < get_wait_for_object_timeout() && num_animations > 0)
			{
				sleep(0.5, true);
		        num_animations = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(\":animated\").length;");
				logDebug("Number of animations = " + num_animations);
			}
		}catch (Exception ex){
			
		}
	}

	public static long get_busy_item_count()
	{
		long num_busy_items = 0;
		
		try
		{
	        String document_status = (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return document.readyState;");
	        long active_jquery_count = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return jQuery.active");
	        long animation_count = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(\":animated\").length;");
	        long document_state = ((document_status.equals("complete")) ? 0 : 1);
	        num_busy_items = document_state + active_jquery_count + animation_count;

			logDebug("Number of busy items = " + num_busy_items + " (Document State: " + document_status + ", Active JQuery Count: " + active_jquery_count + ", Animation Count: " + animation_count + ")");
		} catch (Exception ex)
		{
		}
		
		return num_busy_items;
	}

	public static void wait_until_ready()
	{
		try
		{
	    	String timerName = start_timer();
			while (get_timer(timerName) < get_wait_for_object_timeout())
			{
		        String document_status = (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return document.readyState;");
		        long active_jquery_count = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return jQuery.active");
		        long animation_count = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(\":animated\").length;");
		        long document_state = ((document_status.equals("complete")) ? 0 : 1);
		        long num_busy_items = document_state + active_jquery_count + animation_count;
		      
		        if (num_busy_items < 1)
		        	break;
		        
		        if (WebTestObject.alertExsists())
		        	break;

				logDebug("Number of busy items = " + num_busy_items + " (Document State: " + document_status + ", Active JQuery Count: " + active_jquery_count + ", Animation Count: " + animation_count + ")");
				sleep(0.5, true);
			}
		} catch (Exception ex)
		{
		}
	}

	public static void wait_until_url(String url)
	{
    	String timerName = start_timer();

		while (get_timer(timerName) < get_wait_for_object_timeout() && !current_driver.getCurrentUrl().matches(url))
		{
			sleep(1, true);
			logDebug("Current URL = " + current_driver.getCurrentUrl());
		}
	}
	

	public static String get_window_handle()
	{
		return current_driver.getWindowHandle();
	}

	public static void switch_to_window(String window_handle)
	{
       	current_driver.switchTo().window(window_handle);
	}

	public static void switch_to_current_window()
	{
		String current_window = current_driver.getWindowHandle();
       	current_driver.switchTo().window(current_window);
	}

	public static void switch_to_next_window(boolean close_current_window)
	{
		final String current_window_handle = current_driver.getWindowHandle();
		
		do
			sleep(1);
		while (current_driver.getWindowHandles().size() == 1);
		
	    for (String activeHandle : current_driver.getWindowHandles())
	    {
	        if (!activeHandle.equals(current_window_handle))
	        {
	        	if (close_current_window)
	        		current_driver.close();
	        	
	        	current_driver.switchTo().window(activeHandle);
	        	break;
	        }
	    }
	}

	public static boolean window_exists(String window_handle)
	{
	    for (String activeHandle : current_driver.getWindowHandles())
	    {
	        if (activeHandle.equals(window_handle))
	        	return true;
	    }
		
	    return false;
	}

	public static void close_window(String window_handle)
	{
		final String current_window_handle = current_driver.getWindowHandle();
		
	    for (String activeHandle : current_driver.getWindowHandles())
	    {
	        if (activeHandle.equals(window_handle))
	        {
	        	current_driver.switchTo().window(activeHandle);
        		current_driver.close();
	        	current_driver.switchTo().window(current_window_handle);
	        	break;
	        }
	    }
	}


	public static void send_key(Keys key)
	{
		Actions action = new Actions(current_driver); 
		action.sendKeys(key).build().perform();
	}

	public static void send_key2(Keys key) throws Exception
	{
		WebTestObject.find_until_existent(
			null,	
			new By[] {
				By.tagName("body"),
			}
		).set_text2(key);
	}

	
	public void scroll_by(int x_offset, int y_offset)
	{
		((JavascriptExecutor)current_driver).executeScript("window.scrollBy(" + x_offset + ", " + y_offset + ");");
	}
}
