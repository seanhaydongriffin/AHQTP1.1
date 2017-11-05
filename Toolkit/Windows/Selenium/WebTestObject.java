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

	public int get_child_count(By arg)
	{
		BrowserTestObject.set_current_driver_if_not_set(true);
		return a.findElements(arg).size();
	}

	//--------------------------------------------------------------------------------
	//	Waiting methods
	//--------------------------------------------------------------------------------

	public void wait_until_nonexistent(String attribute_name)
	{
    	String timerName = start_timer();
		
		while (get_timer(timerName) < Test_Script.get_wait_for_object_timeout())
		{
			String ff = a.getAttribute(attribute_name);
			
			if (ff == null)
			
				return;
			
			sleep(1);
		}

		throw new TimeoutException("wait_until_nonexistent timed out");
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

	public void hover2()
	{
        try
        {
    		// Get the object's position relative to the viewport
    		Object x = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().left;", a);
    		Object y = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().top;", a);

    		// Move the mouse to the object's position relative to the viewport
	        Robot robot = null;
	        robot = new Robot();
	        robot.mouseMove(20 + ((Number)x).intValue() + (a.getSize().getWidth() / 2), 100 + ((Number)y).intValue() +  + (a.getSize().getHeight() / 2));
        } catch (Exception e)
        {
        }
    }
	
	public static void acceptAlert()
	{
        try 
        {
        	Alert alert = BrowserTestObject.current_driver.switchTo().alert();
        	alert.accept();
		} catch (Exception e)
        {
			e.printStackTrace();
		}  
	}
	
	public static String getAlertText()
	{
		try 
		{
			Alert alert = BrowserTestObject.current_driver.switchTo().alert();
			return alert.getText();
		} catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}  
	}
	
	public static void dismissAlert()
	{
		try 
		{
			Alert alert = BrowserTestObject.current_driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception e)
		{
			e.printStackTrace();
		}  
	}

	public static boolean alertExsists()
	{		
		if(BrowserTestObject.current_driver.getWindowHandles().size()>1){
			return true;
		}
		return false;
	}
	
	public static boolean isDialogBox()
	{	
		WebTestObject dialogBox = WebTestObject.find(
			null,	
			new By[] {
				By.tagName("body"),
				By.xpath(".//div[@role='region']"),
			}
		);
		if(dialogBox != null){
			return true;
		}
		return false;
	}
	
	public void hover(WebTestObject a2, Point offset) {
		
        Point p = a.getLocation();
        int x = p.getX();
        int y = p.getY();

        Dimension d = a.getSize();
        int h = d.getHeight();
        int w = d.getWidth();
		
        Point p2 = a2.a.getLocation();
        int x2 = p2.getX();
        int y2 = p2.getY();

        Dimension d2 = a2.a.getSize();
        int h2 = d2.getHeight();
        int w2 = d2.getWidth();

        System.out.println("x2 = " + x2);
        System.out.println("x = " + x);
        System.out.println("w2 = " + w2);
        System.out.println("y2 = " + y2);
        System.out.println("y = " + y);
        System.out.println("h2 = " + h2);
        System.out.println((x2 - x) + (w2 / 2));
        System.out.println((y2 - y) + (h2 / 2) + 100);
        
        try
        {
	        Robot robot = null;
	        robot = new Robot();
//	        robot.mouseMove((x - x2) + (w2 / 2), (y - y2) + (h2 / 2) + 100);
	        robot.mouseMove(offset.getX() + x2 + (w2 / 2), offset.getY() + (y - y2) + (h2 / 2) + 100);
//	        robot.mouseMove(x + 4, y + 100);
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

	public void click2()
	{
		Point p = a.getLocation();
		System.out.println(p.x + "," + p.y);
		Rectangle r = a.getRect();
		System.out.println(r.x + "," + r.y);
	//	Actions action = new Actions(BrowserTestObject.current_driver); 
	//	action.moveToElement(a).click().perform();
	}
	
	public void click(Point offset_to_click)
	{
		Actions action = new Actions(BrowserTestObject.current_driver); 
		action.moveToElement(a).moveByOffset(offset_to_click.x,  offset_to_click.y).click().perform();
/*		
		String moveTo = "var fireEvent = arguments[0];" + 
						"var evObj = document.createEvent('MouseEvents');" + 
						"evObj.initEvent( 'mouseover', true, true );" + 
						"fireEvent.dispatchEvent(evObj);";
		
		BrowserTestObject.current_driver.executeScript(moveTo, a);

		action.moveByOffset(offset_to_click.x,  offset_to_click.y).click().perform();

/*		
        try
        {
    		// Get the object's position relative to the viewport
    		Object x = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().left;", a);
    		Object y = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().top;", a);
    		
    		// Move the mouse to the object's position relative to the viewport
	        Robot robot = null;
	        robot = new Robot();
	        robot.mouseMove(20 + ((Number)x).intValue() + (a.getSize().getWidth() / 2) + offset_to_click.getX(), 20 + 100 + ((Number)y).intValue() + (a.getSize().getHeight() / 2) + offset_to_click.getY());
	   	    robot.mousePress(InputEvent.BUTTON1_MASK);
	 	    robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (Exception e)
        {
        }
  */      
    }

	public void Select()
	{
		if(!a.isSelected()){
			a.click();
		}
	}
	
	public void DeSelect()
	{
		if(a.isSelected()){
			a.click();
		}
	}
	
	public void doubleClick()
	{
		Actions action = new Actions(BrowserTestObject.current_driver); 
		action.doubleClick(a); 
		a.sendKeys("");
		action.perform();
	}
	
	public void scrollAndClick()
	{
		scroll_into_view_online();
		a.click();
		return;
	}
	
	public void scroll_into_view_online()
	{
		int elementPosition = a.getLocation().getY();
		String js = String.format("window.scroll(100, %s)", elementPosition-250);
		((JavascriptExecutor)BrowserTestObject.current_driver).executeScript(js);
		return;
	}	
	
	// Checking the element is Clickable or not
	public boolean isClickable()
	{
		try{
			WebDriverWait wait = new WebDriverWait(BrowserTestObject.current_driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(a));
		return true;
		} catch (Exception ex){
			return false;
		}
	}
	
	public void scroll_Once_html_based_dropdown(int xOffSet, int yOffSet)
	{	
		try
	        {
	    		// Get the object's position relative to the viewport
	    		Object x = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().left;", a);
	    		Object y = ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().top;", a);
	    		
	    		// Move the mouse to the object's position relative to the viewport
		        Robot robot = null;
		        robot = new Robot();
		        robot.mouseMove(20 + ((Number)x).intValue() + (a.getSize().getWidth() / 2) + xOffSet, 20 + 100 + ((Number)y).intValue() +  + (a.getSize().getHeight() / 2) + yOffSet);
		        robot.mousePress(InputEvent.BUTTON1_MASK);
		        robot.mouseRelease(InputEvent.BUTTON1_MASK);
		        sleep(2);
	    } catch (Exception e)
	    {
	    }
	}
	
	public boolean isDisplayed()
	{
		try{
			return a.isDisplayed();
		}catch(Exception ex){
			return false;
		}
	}
	
	public boolean isEnabled()
	{
		return a.isEnabled();
	}

	public void click(boolean ensure_object_is_visible_first)
	{
		if (ensure_object_is_visible_first)
			
			((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].scrollIntoView(true);", a);
		
		a.click();
	}

	public static void click_and_set_text(WebTestObject from_object, By[] click_bys, By[] set_text_bys, String text_to_set) throws Exception
	{
		WebTestObject xxx = WebTestObject.find_until_existent(
			from_object,	
			click_bys
		);
		
		xxx.click();
		Toolkit.Windows.Selenium.BrowserTestObject.wait_until_no_ajax();

		WebTestObject.find_until_existent(
			xxx,	
			set_text_bys
		).set_text(text_to_set);
		Toolkit.Windows.Selenium.BrowserTestObject.wait_until_no_ajax();
	}

	public static void click_and_set_text2(WebTestObject from_object, By[] click_bys, By[] set_text_bys, String text_to_set) throws Exception
	{
		WebTestObject xxx = WebTestObject.find_until_existent(
			from_object,	
			click_bys
		);
		
		xxx.scroll_into_view();

		xxx = WebTestObject.find_until_existent(
			from_object,	
			click_bys
		);
		
		xxx.click();
		Toolkit.Windows.Selenium.BrowserTestObject.wait_until_no_ajax();

		WebTestObject.find_until_existent(
			xxx,	
			set_text_bys
		).set_text(text_to_set);
		
		Toolkit.Windows.Selenium.BrowserTestObject.wait_until_no_ajax();
	}

	

	/*
	public void click(Point offset_to_click)
	{
		
		// Move mouse to the click location
        Point p = a.getLocation();
        int x = p.getX();
        int y = p.getY();

        Dimension d = a.getSize();
        int h = d.getHeight();
        int w = d.getWidth();

        try
        {
	        Robot robot = null;
	        robot = new Robot();
	        robot.mouseMove(x + offset_to_click.getX(), x + offset_to_click.getY());
	 //       robot.mousePress(InputEvent.BUTTON1_MASK);
	   //     robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (Exception e)
        {
        }
	}
*/
	public void low_level_click(WebTestObject a2)
	{
        System.out.println("a2.getLocation().getX() = " + a2.a.getLocation().getX());
        System.out.println("a2.getSize().getWidth() = " + a2.a.getSize().getWidth());
        System.out.println("a2.getLocation().getY() = " + a2.a.getLocation().getY());
        System.out.println("a2.getSize().getHeight() = " + a2.a.getSize().getHeight());
        System.out.println("a.getLocation().getY() = " + a.getLocation().getY());
        
        click(new Point(20 + a2.a.getLocation().getX() + (a2.a.getSize().getWidth() / 2), 20 + 100 + (a.getLocation().getY() - a2.a.getLocation().getY()) + (a2.a.getSize().getHeight() / 2)));
    }

	public void low_level_click(WebTestObject a2, Point offset)
	{
        System.out.println("offset.getX() = " + offset.getX());
        System.out.println("offset.getY() = " + offset.getY());
        System.out.println("a2.getLocation().getX() = " + a2.a.getLocation().getX());
        System.out.println("a2.getSize().getWidth() = " + a2.a.getSize().getWidth());
        System.out.println("a2.getLocation().getY() = " + a2.a.getLocation().getY());
        System.out.println("a2.getSize().getHeight() = " + a2.a.getSize().getHeight());
        System.out.println("a.getLocation().getY() = " + a.getLocation().getY());
        
        click(new Point(20 + offset.getX() + a2.a.getLocation().getX() + (a2.a.getSize().getWidth() / 2), 20 + 100 + offset.getY() + (a.getLocation().getY() - a2.a.getLocation().getY()) + (a2.a.getSize().getHeight() / 2)));
    }
	

	public void select_by_text(String text)
	{
		Select sb = new Select(a);

		if (sb.getOptions().size() == 1)
		
			a.click();
		else
			
			for (int i=0 ;i < sb.getOptions().size(); i++)
			{				
				if (sb.getOptions().get(i).getText().equals(text))
				{
					// If the list is scrolling the following will scroll the list to make the option visible
					((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].selectedIndex=" + i + ";", a);

					// The following clicks on the option
//					sb.getOptions().get(i).click();
	//				sleep(0.5, true);
					break;
				}
			}
	}
	
	public void scroll_and_select_by_text(String text)
	{
		scroll_into_view_online();
		Select sb = new Select(a);
		
		boolean searchItemFound = false;
		
		if (sb.getOptions().size() == 1) {
			a.click();
		}
		else {
			for (int i=0 ;i < sb.getOptions().size(); i++)
			{				
				if (sb.getOptions().get(i).getText().equals(text))
				{
					// If the list is scrolling the following will scroll the list to make the option visible
					((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].selectedIndex=" + i + ";", a);
					sleep(0.5, true);
					searchItemFound = true;
					break;
				}
			}
			
			if(!searchItemFound){
				((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].selectedIndex=1;", a);
				sleep(0.5, true);
			}
		}
	}
	
	public String focus()
	{
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].focus()", a);
	}


	//--------------------------------------------------------------------------------
	//	Location / Position related methods
	//--------------------------------------------------------------------------------

	public int get_x()
	{
		return a.getLocation().getX();
	}

	public int get_y()
	{
		return a.getLocation().getY();
	}

	public void get_location_relative_to_viewport()
	{
		long x = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().left;", a);
		long y = (long)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].getBoundingClientRect().top;", a);
		System.out.println(x + " " + y);
	}
	
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
	
	public String get_selected_text()
	{
		Select sb = new Select(a);
		return sb.getFirstSelectedOption().getText();
	}
	
	public boolean is_displayed()
	{
		return a.isDisplayed();
	}
	
	public boolean is_selected()
	{
		return a.isSelected();
	}
	
	public String get_text2()
	{
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(arguments[0]).text();", a);
	}
	
	public static String get_value_by_css_selector(String attribute_name, String attribute_value)
	{
		BrowserTestObject.set_current_driver_if_not_set(true);
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return document.querySelector('[" + attribute_name + "=\"" + attribute_value + "\"]').value;");
	}
	
	public String get_attribute(String attribute_name)
	{
		return a.getAttribute(attribute_name);
	}
	
	public String get_value()
    {
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return $(arguments[0]).value", a);
    }
	
	public String get_value2()
	{
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return arguments[0].value;", a);
	}
	
	public String get_computed_style()
	{
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("return window.getComputedStyle(arguments[0], null).getPropertyValue('display');", a);
	}

	public String get_css_value(String css_name)
	{
		return a.getCssValue(css_name);
	}	
	
	public ArrayList get_attributes()
	{
		return (ArrayList) ((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("var s = []; var attrs = arguments[0].attributes; for (var l = 0; l < attrs.length; ++l) { var a = attrs[l]; s.push(a.name + ':' + a.value); } ; return s;", a);
	}

	//--------------------------------------------------------------------------------
	//	Text Entry methods
	//--------------------------------------------------------------------------------
	
	public void set_text(CharSequence... arg0)
	{
        a.clear();
		a.sendKeys(arg0);		
	}
	
	public void set_text4(CharSequence... arg0)
	{
//		WebElement.sendKeys(Keys.TAB);
	}
	
	public void set_text(String arg0, boolean ensure_object_is_visible_first)
	{
		if (ensure_object_is_visible_first)
			
			((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].scrollIntoView(true);", a);
		
        a.clear();
		a.sendKeys(arg0);		
	}

	public void scrollAndSetText(CharSequence... arg0)
	{
		scroll_into_view_online();
		a.clear();
		a.sendKeys(arg0);		
	}
	
	public void set_text2(CharSequence... arg0)
	{
		a.sendKeys(arg0);		
	}
	
	public String set_attribute(String attribute_name, String attribute_value)
	{
		return (String)((JavascriptExecutor) BrowserTestObject.current_driver).executeScript("arguments[0].setAttribute('" + attribute_name + "', '" + attribute_value + "')", a);
	}

	//--------------------------------------------------------------------------------
	//	Frame related methods
	//--------------------------------------------------------------------------------
	
	// Switch control back to the main browser window
	
	public void switch_to_frame()
	{
		BrowserTestObject.set_current_driver_if_not_set(true);
		BrowserTestObject.current_driver.switchTo().frame(a);
	}

	
}
