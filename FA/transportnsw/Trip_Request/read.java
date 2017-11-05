package FA.transportnsw.Trip_Request;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import java.util.Hashtable;

public class read 
{
	public static Hashtable testMain(
		boolean reset, 
		String Comment_1, 
		String Assigned_to
	) 
	{
		Hashtable<String, String> 	result        	= new Hashtable(),
									datapool_search = new Hashtable();
		
	    if (reset)
	    {   
	        // Reset the datapool cursor for this test case
  		
			Toolkit.Windows.Selenium.Datapool.Cursor.reset(
				"Trip Request"
			);
	    }
	
		datapool_search.put("Comment 1", Comment_1);
		datapool_search.put("Assigned to", Assigned_to);
		
		result = Toolkit.Windows.Selenium.Datapool.Row.search(
			Test_Environment.test_env + " - Trip Request", 
			datapool_search, 
			""
		);
		
		if (result == null)
			
			logInfo("Failed to read from datapool \"" + Test_Environment.test_env + " - Trip Request\"");
		else	
		{
			logInfo("Read Comment 1 \"" + result.get("Comment 1") + "\" from datapool \"" + Test_Environment.test_env + " - Trip Request\"");
			logInfo("Read Assigned to \"" + result.get("Assigned to") + "\" from datapool \"" + Test_Environment.test_env + " - Trip Request\"");
		}

		return result;
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
