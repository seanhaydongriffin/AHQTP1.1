package Toolkit.Windows.Selenium;

import org.aspectj.lang.Signature;
import static Toolkit.Windows.Selenium.Test_Script.*;

public aspect TestLog
{
//    pointcut traceMethods() : (execution(* *(..))&& !cflow(within(TestLog)));
	
	// pointcut everything except within the Toolkit package
    pointcut traceMethods() : (execution(* *(..)) && !within(Toolkit..*));

    before(): traceMethods()
    {
    	try
    	{
    	
	        StackTraceElement[] element = new Throwable().getStackTrace();
	        logInfo(element[2].getClassName() + " line " + element[2].getLineNumber() + " called " + thisJoinPointStaticPart.getSignature().getDeclaringTypeName());
    	} catch (Exception e)
    	{
    		
    	}
    	
        /*        Signature sig = thisJoinPointStaticPart.getSignature();
        String line =""+ thisJoinPointStaticPart.getSourceLocation().getLine();
        String sourceName = thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName();
        
        System.out.println( "Call from "
                    +  sourceName
                    +" line " +
                    line
                    +" to " +sig.getDeclaringTypeName() + "." + sig.getName());
  */      

        /*
        Logger.getLogger("Tracing").log(
                Level.INFO, 
                "Call from "
                    +  sourceName
                    +" line " +
                    line
                    +" to " +sig.getDeclaringTypeName() + "." + sig.getName()
        );
        */
    }
}
