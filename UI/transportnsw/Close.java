package UI.transportnsw;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Close {
	
	public static void testMain() throws Exception
	{
		logInfo("screenshot browser");

		BrowserTestObject.close_all();
	}

	public static void main(String[] args) throws Exception
	{
		testMain();
	}
}