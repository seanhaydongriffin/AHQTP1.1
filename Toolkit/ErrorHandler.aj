package Toolkit;

import java.util.Hashtable;
import static Toolkit.Windows.Selenium.Test_Script.*;

import org.aspectj.lang.JoinPoint;

public aspect ErrorHandler {

	pointcut uncaughtExceptionScope() : (execution(* *(..)));
	
	after() throwing(Throwable t) : uncaughtExceptionScope() && !cflow(adviceexecution())    {
	    handleException(thisJoinPoint, t);
	}   

	protected void handleException(JoinPoint jp, Throwable t)
	{
		boolean      result   = true;
        
		Hashtable<String, String> 	result2        	= new Hashtable(),
									datapool_search = new Hashtable();

		logInfo("screenshot");
		t.printStackTrace();
		logFail(org.apache.commons.lang3.exception.ExceptionUtils.getMessage(t));
		logFail(org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage(t));
		logFail(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(t));
        
        // Restore the system to a base state, if logging level is 1 or not set
		datapool_search.put("Parameter Name", "Logging Level");
		result2 = Toolkit.Windows.Selenium.Datapool.Row.search(
			"Release Parameters", 
			datapool_search, 
			""
		);
		
		if ((result2 == null || result2.get("Parameter Value").equals("1")) && get_logging_level() == 1)
			
			Base_State.invoke();

		System.exit(0);
	}
}