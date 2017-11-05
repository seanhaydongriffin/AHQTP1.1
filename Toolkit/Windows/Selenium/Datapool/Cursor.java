package Toolkit.Windows.Selenium.Datapool;

import java.io.File;

public class Cursor
{
	
	public static void reset(
		String dp_name
	)
	{
        String dp_cursor_path = Test_Environment.cursors_path + File.separator + Test_Environment.test_env + " - " + dp_name + " - " + Test_Environment.test_case_name + ".cur";
        Toolkit.Windows.File.overwrite(dp_cursor_path, "0");
	}

}
