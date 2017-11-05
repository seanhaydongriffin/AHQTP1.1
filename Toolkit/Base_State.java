package Toolkit;

public class Base_State {

	public static void invoke()
	{
		try
		{
			Toolkit.Windows.Selenium.BrowserTestObject.close_all();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		} 
	}
}