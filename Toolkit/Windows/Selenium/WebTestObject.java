package Toolkit.Windows.Selenium;

import static Toolkit.Windows.Selenium.Test_Script.get_timer;
import static Toolkit.Windows.Selenium.Test_Script.logDebug;
import static Toolkit.Windows.Selenium.Test_Script.sleep;
import static Toolkit.Windows.Selenium.Test_Script.start_timer;

import java.awt.List;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTestObject 
{
	private WebElement a;
	
	public WebTestObject(WebElement a)
	{
		this.a = a;
	}

	//--------------------------------------------------------------------------------
	//	Finding methods
	//--------------------------------------------------------------------------------

	public static WebTestObject find(WebTestObject curr_testobject, By[] args)
	{
		BrowserTestObject.set_current_driver_if_not_set(true);
		WebElement curr_webelement = null;
		WebElement orig_webelement = null;
		
		if (curr_testobject != null)
		{			
			curr_webelement = curr_testobject.a;
			orig_webelement = curr_testobject.a;
		}
		
    	String timerName = start_timer();
    	
		for (int loop_num = 0; loop_num < args.length; loop_num++)
		{
			try
			{
    			if (curr_webelement == null)
    			
    				curr_webelement = BrowserTestObject.get_current().findElement(args[loop_num]);
    			else
    				
    				curr_webelement = curr_webelement.findElement(args[loop_num]);
			} catch (NoSuchElementException e)
			{
				logDebug("NoSuchElementException *** loop_num " + loop_num + " " + args[loop_num]);
				return null;
			} catch (Exception e)
			{
				logDebug("Exception *** Message: " + e.getMessage());
				return null;
			}
		}

		WebTestObject tmp = new WebTestObject(curr_webelement);
        return tmp;
	}

	public static WebTestObject find_until_existent(WebTestObject curr_testobject, By[] args) throws Exception
	{
		BrowserTestObject.set_current_driver_if_not_set(true);
		WebElement curr_webelement = null;
		WebElement orig_webelement = null;
		
		if (curr_testobject != null)
		{			
			curr_webelement = curr_testobject.a;
			orig_webelement = curr_testobject.a;
		}
	
    	String timerName = start_timer();
    	
		for (int loop_num = 0; loop_num < args.length; loop_num++)
		{
			try
			{
    			if (curr_webelement == null)
    				
    				curr_webelement = BrowserTestObject.get_current().findElement(args[loop_num]);
    			else
    				
    				curr_webelement = curr_webelement.findElement(args[loop_num]);
    			
    //	    	String document_readystate = (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return document.readyState;");
    //	    	long num_ajax_requests = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return jQuery.active;");
    //	    	long num_animations = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(\":animated\").length;");

	//			logDebug("document.readyState = " + document_readystate + ", # ajax requests = " + num_ajax_requests + ", # animations = " + num_animations);

			} catch (Exception e)
			{
				if (get_timer(timerName) > Test_Script.get_wait_for_object_timeout())
				{
					throw new RuntimeException("Timed out trying to find a WebTestObject");
				} else
				{
    				logDebug("Failed to find " + args[loop_num] + " *** loop_num " + loop_num);
    				curr_webelement = orig_webelement;
    				loop_num = -1;
    				sleep(0.5, true);
				}
			}
		}
		
		WebTestObject tmp = new WebTestObject(curr_webelement);
        return tmp;
	}

	
	//--------------------------------------------------------------------------------
	//	Mouse related methods
	//--------------------------------------------------------------------------------

	public void hover()
	{
        try
        {
            (new Actions(BrowserTestObject.current_driver)).moveToElement(a).build().perform();
        } catch (Exception e)
        {
        }
    }


	public void click()
	{
		// Re-attempt click until no Selenium exceptions occur, or timeout occurs
		String timerName = start_timer();
		while (get_timer(timerName) < Test_Script.get_wait_for_object_timeout())
		{
			try
			{
				a.click();
				break;
			} catch (ElementNotVisibleException e)
			{
				logDebug("ElementNotVisibleException");
				sleep(0.5, true);
			} catch (StaleElementReferenceException e)
			{
				logDebug("StaleElementReferenceException");
				sleep(0.5, true);
			}
		}
	}

	public void click(boolean ensure_object_is_visible_first)
	{
		if (ensure_object_is_visible_first)
			
			((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].scrollIntoView(true);", a);
		
		a.click();
	}

	//--------------------------------------------------------------------------------
	//	Location / Position related methods
	//--------------------------------------------------------------------------------
	
	public void scroll_into_view()
	{
		// Re-attempt scroll until no Selenium exceptions occur, or timeout occurs
		String timerName = start_timer();
		while (get_timer(timerName) < Test_Script.get_wait_for_object_timeout())
		{
			try
			{
				((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].scrollIntoView(true);", a);
				break;
			} catch (ElementNotVisibleException e)
			{
				logDebug("ElementNotVisibleException");
				sleep(0.5, true);
			} catch (StaleElementReferenceException e)
			{
				logDebug("StaleElementReferenceException");
				sleep(0.5, true);
			}
		}
	}

	//--------------------------------------------------------------------------------
	//	Text Retrieval methods
	//--------------------------------------------------------------------------------
	
	public String get_text()
	{
		return a.getText();
	}
	

	//--------------------------------------------------------------------------------
	//	Text Entry methods
	//--------------------------------------------------------------------------------
	
	public void set_text(CharSequence... arg0)
	{
        a.clear();
		a.sendKeys(arg0);		
	}
	
	public void set_text(String arg0, boolean ensure_object_is_visible_first)
	{
		if (ensure_object_is_visible_first)
			
			((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].scrollIntoView(true);", a);
		
        a.clear();
		a.sendKeys(arg0);		
	}

	
}
