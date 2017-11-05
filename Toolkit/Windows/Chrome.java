package Toolkit.Windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.Test_Script;
import Toolkit.Windows.Selenium.WebTestObject;
import static Toolkit.Windows.Selenium.Test_Script.*;

public class Chrome
{
	// Clear the browser data in Chrome

	public static void clear_browser_data() throws Exception
	{
		BrowserTestObject.current_driver.get("chrome://settings/clearBrowserData");
		
		WebTestObject.find_until_existent(
			null,	
			new By[] {
				By.cssSelector("* /deep/ #clearBrowsingDataConfirm")
			}
		).click();
		
		(new WebDriverWait(BrowserTestObject.current_driver, 60)).until(ExpectedConditions.urlToBe("chrome://settings/"));
	}
	
}
