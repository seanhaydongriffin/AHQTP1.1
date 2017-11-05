package Toolkit.Windows.Selenium.Datapool;

import java.io.File;

public class Path {

	
	public static String read()
	{
		return Toolkit.Windows.Selenium.Data.Path.read() + File.separator + "Pools";
	}

}
