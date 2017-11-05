package Toolkit.Windows.Selenium.Datapool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import static Toolkit.Windows.Selenium.Test_Script.*;

public class Lock
{
	
	public static FileLock try1(String dp_name)
	{
	    String   dp_lock_fullname	  = "";

        String dp_path = Toolkit.Windows.Selenium.Datapool.Path.read();

	  	System.out.println("Started Custom_Datapool.try_lock");
	  	
		FileLock lock = null;
		
        // Get datapool full path names
	  	
		if (dp_name.contains(File.separator))
    	{
	        dp_name = dp_name.substring(dp_name.lastIndexOf(File.separator) + File.separator.length());
   	        dp_lock_fullname = dp_path + File.separator + "Locks" + File.separator + dp_name + ".lck";
    	} else
	     
   	        dp_lock_fullname = dp_path + File.separator + "Locks" + File.separator + dp_name + ".lck";
        
        File dp_lock_file = new File(dp_lock_fullname);
		
		try
		{
	        // If the datapool lock file doesn't yet exist, then create it
	        if (!dp_lock_file.exists())
	        {
	    	  	System.out.println("Creating a datapool lock file");
	        	dp_lock_file.createNewFile();
	        }

			// Get the channel of the datapool lock file
	        RandomAccessFile f = new RandomAccessFile(dp_lock_file, "rw");
			FileChannel channel = f.getChannel();
			
			try
			{
				while (true)
				{
					// Try to lock the datapool lock file
					lock = channel.tryLock();
					
					// If the lock was unsuccessful, then sleep and try again
					if (lock == null)
					{
					  	System.out.println("Unsuccessful in acquiring a datapool lock - sleeping ...");
						sleep(5);
					} else
						
						break;
				}
			} catch (OverlappingFileLockException e)
			{
				
			}
			
	//		f.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();	    	  
		} catch (IOException e)
		{
			e.printStackTrace();	    	  
		}
		
	  	System.out.println("Success in acquiring a datapool lock");

		return lock;

	}
	
	public static void unlock(FileLock lock)
	{
	  	System.out.println("Started Custom_Datapool.unlock");
	  	
		FileChannel channel = lock.channel();
		
	    // release the lock
	    try
	    {
		  	System.out.println("Releasing the datapool lock");
	    	lock.release();
	    	channel.close();
	    } catch (IOException e)
	    {
	    	e.printStackTrace();	    	  
	    }
	    
	  	System.out.println("Success in releasing the datapool lock");

	}

}
