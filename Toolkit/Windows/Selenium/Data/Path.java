package Toolkit.Windows.Selenium.Data;

import java.io.File;

public class Path
{

	
	public static String read()
	{
		String jus_project_path = Toolkit.Windows.Selenium.Project.Path.read();

		return jus_project_path + File.separator + "Data";
	}

}
