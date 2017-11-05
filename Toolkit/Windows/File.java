package Toolkit.Windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import com.google.common.io.Files;
import static Toolkit.Windows.Selenium.Test_Script.*;

public class File {

	public static boolean exists(String filename)
	{
		return (new java.io.File(filename)).exists();
	}
	
	public static Boolean fileExists(String filename)
	{
		try
		{
			java.io.File file = new java.io.File(filename);
			return file.exists();
		}catch (Exception ex){
			return false;
		}
	}
	    

	public static boolean delete(String filename)
	{
		return (new java.io.File(filename)).delete();
	}

	public static void wait_until_existent(String filename)
	{
		java.io.File file = new java.io.File(filename);
		
    	String timerName = start_timer();
    	
		while (get_timer(timerName) < get_wait_for_object_timeout())
		{
			if (file.exists())
				
				break;
			
			sleep(0.5, true);
		}
	}

	
	public static String read(String filename)
	{
	    String result_str = null;      

		try
		{
			java.io.File file = new java.io.File(filename);

            if ( file != null && file.exists() )
            {
            	// Read in the specified file ...
            	FileInputStream input = new FileInputStream(file);
            	byte[] result =  new byte[(int)file.length()];
            	final int length = result.length;
            	int bytesRead = 0;
            	int offset = 0;
   
            	while ( bytesRead != -1 && offset < length )
            	{
            		bytesRead = input.read(result, offset, length-offset);
            		
            		if ( bytesRead >= 0 )
            			
            			// bytesRead == -1 when end-of-file is reached
            			offset += bytesRead;
            	}
               
               input.close();
               result_str = new String(result);
            }
            else

            	result_str = "";

		} catch(Exception e)
		{
			e.printStackTrace();
		} 

		return result_str;
	}
	
	public static String read_lines(String filename, int num_lines)
	{
	      int   i           = 1;
	      String            result_str  = "";      

	      try
	      {
   	      	  BufferedReader brTest = new BufferedReader(new FileReader(filename));
	    	  
	    	  for (i = 0; i < num_lines; i++)
	    	  {
	    		  result_str = result_str + brTest .readLine() + "\n";
	    	  }

	    	  brTest.close();

	      } catch(Exception e)
	      {
	      } 

	      return result_str;
	}
	
	public static boolean overwrite(String filename, Object data)
	{
		try
		{
			// If String data is to be written
			if (data.getClass().getName().matches(".*String.*"))
			{         
			    String file = filename;
			    byte[] content = ((String)data).getBytes();
			    FileOutputStream output = new FileOutputStream(file);
			    output.write(content);
			    output.close();
			}
 
			// If Object data is to be written
			if (data.getClass().getName().matches(".*Object.*"))
			{         
			    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));                            
			    out.writeObject(data);
			    out.close();
			}

		} catch(Exception e)
		{
			e.printStackTrace();
		} 

		return true;
	}
	
	public static boolean append(String filename, String text)
	{
		try
		{
	         FileWriter fstream = new FileWriter(filename, true);
	         BufferedWriter out = new BufferedWriter(fstream);
	         out.write(text);
	         out.close();

		} catch(Exception e)
		{
			e.printStackTrace();
		} 

		return true;
	}
	
	public static boolean remove_bytes_from_end(String filename, long num_of_bytes)
	{
		try
		{
	        RandomAccessFile file= new RandomAccessFile(filename, "rw");
            long length = file.length();
            file.setLength(length - num_of_bytes);
            file.close();

		} catch(Exception e)
		{
			e.printStackTrace();
		} 

		return true;
	}
	
	public static boolean copy(String source_path, String target_path)
	{
		try
		{
			java.io.File source_path_file = new java.io.File(source_path);
			java.io.File target_path_file = new java.io.File(target_path);
			
			Files.copy(source_path_file, target_path_file);

		} catch(Exception e)
		{
			e.printStackTrace();
		} 

		return true;
	}

	
	
}
