package Toolkit.Windows.Selenium.Datapool;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Row
{
	
	public static Hashtable search(String dp_name, Hashtable search_fields, String overriding_row_number)
	{
		int dp_cursor = 0;
		int i = 1;
		int j = 1;
		int row_num = 0;
		String dp_fullname = "";
		String dp_cursor_path = "";
		FileLock 		lock	  		= null;
		Hashtable<String, String> result        = new Hashtable();

        String dp_path = Toolkit.Windows.Selenium.Datapool.Path.read();
      
	    try
	    {
    	  	System.out.println("Started Custom_Datapool.Row.search for " + dp_name);

	         // Get datapool full path names
    	  	
	    	 if (dp_name.toString().contains(File.separator))
		    	  	
	    		 dp_fullname = dp_name + ".csv";
	    	 else
		     
	    		 dp_fullname = dp_path + File.separator + dp_name + ".csv";

	         // If an overriding row number is specified, seek to that row
	         if (!overriding_row_number.equals(""))

	            dp_cursor = Integer.parseInt(overriding_row_number);
//		         	dp_cursor = (Integer)overriding_row_number;
	         else
	         {   
	         
	            // If persistent cursor is to be used,
	            //   move to the row number specified by the cursor.
	 			String test_case_name = Toolkit.Windows.Selenium.Project.Script.Test_Case.read_name();
	            dp_cursor_path = dp_path + File.separator + "Cursors" + File.separator + dp_name + " - " + test_case_name + ".cur";
	            
	            boolean dp_cursor_exists = Toolkit.Windows.File.exists(dp_cursor_path);

	            if (dp_cursor_exists)
	            {
	               dp_cursor = Integer.parseInt(Toolkit.Windows.File.read(dp_cursor_path));
	               
	       	       // Move the cursor ahead one row
//	               dp_cursor++;
	    	    
	               // Write the new cursor
	//               dp_cursor_path[1] = Integer.toString(dp_cursor);
	  //             (new BTC_Toolkit.Windows.File.overwrite()).testMain(dp_cursor_path);
	            }
	         }

		
		

            System.out.println(dp_cursor);
            
            // Try to acquire a lock on the datapool before proceeding
            
            lock = Toolkit.Windows.Selenium.Datapool.Lock.try1(dp_name);

    	  	System.out.println("Reading the datapool");

            // Read the datapool columns spec
	    	
//	            CSVReader reader2 = new CSVReader(new FileReader(dp_fullname), ',', '"', '\\', 0);
    	  	
    	    //11-02-2015 - Updated to avoid NULL character in order to avoid the escape characters like backslash
    	  	CSVReader reader2 = new CSVReader(new FileReader(dp_fullname), ',', '"', '\0', 0);
    	  	
	    	String [] header_row;
	    	header_row = reader2.readNext();
	    	reader2.close();
            
            // Read the datapool data
	    	
//	            CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\\', dp_cursor);
            
	    	//11-02-2015 - Updated to avoid NULL character in order to avoid the escape characters like backslash
            CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\0', dp_cursor);
            java.util.List<String[]> myEntries = (java.util.List<String[]>)reader.readAll();
            reader.close();
		    Iterator<String[]> iterator = myEntries.iterator();

		    // Release the datapool lock
            
            Toolkit.Windows.Selenium.Datapool.Lock.unlock(lock);

		    // convert the search hashtable back into a String array to compare against the datapool fields
		    Enumeration search_fields_keys = search_fields.keys();
		    int num_search_fields = search_fields.size();
		    int[] search_field_num = new int[num_search_fields];
		    String[] search_value = new String[num_search_fields];
		    
		    for (i=0; search_fields_keys.hasMoreElements(); i++)
		    {	
		    	String search_key_name = (String)search_fields_keys.nextElement();
		    	
		    	// Find the field number for each search key in the datapool header
		    	for (j=0; j < header_row.length && !header_row[j].equals(search_key_name); j++);
		    	
		    	search_field_num[i] = j;
		    	search_value[i] = (String)search_fields.get(search_key_name);
		    	
		    	//System.out.println(search_fields_keys.nextElement());
		    }

		    String[] row = new String[header_row.length];
		    boolean all_fields_match = false;
		    row_num = -1;
		    
		    // Find a matching row
		    while (iterator.hasNext()) {
		    	
		    	row = iterator.next();
		    	row_num++;

	    		// Assume all fields will match to start with
		    	all_fields_match = true;

		    	for (i=0; i < num_search_fields; i++)
		    	{
		    		// workaround for when CSVReader above reads an empty value with quotation qualifiers, and it reads a quote 
		    		if (row[search_field_num[i]].equals("\""))
		    		{
		    			row[search_field_num[i]] = "";
		    		}
		    		
		    		// If the search value does not equal the corresponding field value, or the search value is something (*) and the corresponding field value is empty 
		    		if (!search_value[i].equals(row[search_field_num[i]]) && !(search_value[i].equals("*") && !row[search_field_num[i]].equals("")))
                  {
		    			// then all (or some) fields do not match 
		    			all_fields_match = false;
		    			break;
                  }
		    	}
		    	
		    	// all search fields match the row, so exit
		    	if (all_fields_match == true)
		    	{
		    		// If persistent cursor is to be used,
		            //   write the row number (+1) back to the cursor.
			         if (overriding_row_number.equals(""))
			         {
		       	       // Move the cursor ahead one row
		               dp_cursor = row_num;
		    	    
		               // Write the new cursor
		               
		               Toolkit.Windows.File.overwrite(dp_cursor_path, Integer.toString(dp_cursor));
			         }
		    		
		    		break;
		    	}
		    	
//					System.out.println();
			}

		    // if no record was found
		    if (!all_fields_match)
		    	
		    	result = null;
		    else
		    	
			    // Convert the String array found back to a Hashtable
			    for (i=0; i < row.length; i++)
			    {
			    	result.put(header_row[i], row[i]);
			    }
		    
    	  	System.out.println("Ended Custom_Datapool.Row.search for " + dp_name);
	      } catch(Exception e)
	      {
	    	  e.printStackTrace();	    	  
	      } 

		return result;
	}
	
	public static boolean update(
		String dp_name, 
		String row_number_to_update, 
		Hashtable update_row
	)
	{
		int i;
		int dp_cursor = 0;
		String dp_cursor_str;
	    String[] 		dp_cursor_path 	= {"", ""};
	    java.util.List<String[]> myEntries = null;
	      FileLock 		lock	  		= null;
	      boolean 		result        	= true;

	      try
	      {
	    	  
	    	  System.out.println("Started Custom_Datapool.Row.update for " + dp_name);

		      // Get datapool full path names
	    	  String dp_path = Toolkit.Windows.Selenium.Datapool.Path.read();
	    	  String dp_fullname = dp_path + File.separator + dp_name + ".csv";
		         
	            // If persistent cursor is to be used,
	            //   move to the row number specified by the cursor.
				String test_case_name = Toolkit.Windows.Selenium.Project.Script.Test_Case.read_name();
	            dp_cursor_path[0] = Toolkit.Windows.Selenium.Datapool.Path.read();
	            dp_cursor_path[0] = dp_cursor_path[0] + File.separator + "Cursors" + File.separator + dp_name + " - " + test_case_name + ".cur";
	            
	            boolean dp_cursor_exists = Toolkit.Windows.File.exists(dp_cursor_path[0]);
	            
	            System.out.println("script_arg" + dp_cursor_path[0] + " dp_cursor_exists is " + dp_cursor_exists );
	            
	            if (dp_cursor_exists)
	            {
	               dp_cursor_path[1] = "String";
	               dp_cursor_str = Toolkit.Windows.File.read(dp_cursor_path[0]);
	               dp_cursor = Integer.parseInt(dp_cursor_str.trim());
	            }

	            System.out.println("Datapool name: " + dp_name + ", cursor: " + dp_cursor);
	            
	            if (dp_cursor <= 0)
	            	
	            	return false;

	            // Try to acquire a lock on the datapool before proceeding

		    	 lock = Toolkit.Windows.Selenium.Datapool.Lock.try1(
		    		 dp_name
				 );
	            
	    	  	System.out.println("Reading the datapool");

	            // Read the datapool columns spec
		    	
//	            CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\\', 0);
	    	  	
	    	  //11-02-2015 - Updated to avoid NULL character in order to avoid the escape characters like backslash
	    	  	
	    	  	CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\0', 0);
	    	  	
	            myEntries = (java.util.List<String[]>)reader.readAll();
	            reader.close();
			//    Iterator<String[]> iterator = myEntries.iterator();

			    // get the row to update
			    String[] row_to_update = myEntries.get(dp_cursor);

			    // get the header
			    String[] header_row = myEntries.get(0);

			    // for each field in the datapool row
			    for (i=0; i < header_row.length; i++)
			    {
			    	// if the updated row contains a field matching the header
			    	if (update_row.containsKey(header_row[i]))

			    		// update the field in the row
			    		row_to_update[i] = (String)update_row.get(header_row[i]);
			    }
			    
			    // update the row
			    myEntries.set(dp_cursor, row_to_update);
			    
	    	  	System.out.println("Rewriting the datapool");

			    // write the datapool back
			    CSVWriter writer = new CSVWriter(new FileWriter(dp_fullname));
			    writer.writeAll(myEntries);
			    writer.close();
			    
			    // Release the datapool lock

		    	 Toolkit.Windows.Selenium.Datapool.Lock.unlock(
		    		 lock
				 );

	    	  	System.out.println("Ended Custom_Datapool.Row.update for " + dp_name);

			    
	      } catch(IOException e)
	      {
		       e.printStackTrace();	  
	      } catch(Exception e){
	    	  e.printStackTrace();
	      }
      
      
	      return result;

	}
	
	public static boolean append(
		String dp_full_path,
		Object data_to_append_input
	)
	{
	      /**
	       *   USER DECLARATIONS
	       */
	      final int user1    = 1;
	      
	      /**
	       *   SYSTEM DECLARATIONS
	       */
	      int      i              = 1;
	      Object[] script_arg     = new String[5];
	      boolean result        = true;
	      FileLock 		lock	  		= null;
	      String dp_fullname	  = "";
	      
	      /** 
	       *   BODY
	       */
	      
	      String[] data = null;
	      
	      String dp_path = Toolkit.Windows.Selenium.Datapool.Path.read();

	      try
	      {
	    	 System.out.println("Started Custom_Datapool.Row.append for " + dp_full_path);
	    	  
		     // Get datapool full path names
	    	  	
	    	 if (dp_full_path.contains(File.separator))
	    	  	
	    		 dp_fullname = dp_full_path + ".csv";
	    	 else
		     
		    	 if (dp_full_path.contains(File.separator))

	         // Try to acquire a lock on the datapool before proceeding

	    	 lock = Toolkit.Windows.Selenium.Datapool.Lock.try1(
				dp_full_path
			 );

  	  	 	 System.out.println("Reading the datapool");

	    	  	// Read the datapool columns spec
//	            CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\\', 0);
	    	  	
	    	  //11-02-2015 - Updated to avoid NULL character in order to avoid the escape characters like backslash	            
  	  	 	 CSVReader reader = new CSVReader(new FileReader(dp_fullname), ',', '"', '\0', 0);
  	  	 	 java.util.List<String[]> myEntries = (java.util.List<String[]>)reader.readAll();
          	 reader.close();

		     // get the header
		     String[] header_row = myEntries.get(0);
		    
		     // Prepare to append the row(s)
		     CSVWriter writer = new CSVWriter(new FileWriter(dp_fullname, true));

  	  	 	 System.out.println("Appending to the datapool");

		     // If only one Hashtable to append... 
		     if (data_to_append_input instanceof Hashtable)
		     {
		    	Hashtable data_to_append = (Hashtable)data_to_append_input;

			    // build a string array of values from the hashtable in the same order as the header
			    data = new String[header_row.length];
			    
			    for (i=0; i < header_row.length; i++)
			    {
			    	if (data_to_append.containsKey(header_row[i]))
                  {
			    		data[i] = (String)data_to_append.get(header_row[i]);
                  }
			    }

			    // append the row to the datapool
			    writer.writeNext(data);
		     }
		    
		     // If many Hashtables to append (ArrayList of Hashtables)... 
		     if (data_to_append_input instanceof ArrayList)
		     {
		        ArrayList<Hashtable> data_to_append = (ArrayList<Hashtable>)data_to_append_input;

		        // Loop through each Hashtable in the array and append it
		    	for (int record_num = 0; record_num < data_to_append.size(); record_num++)
		    	{
				    // build a string array of values from the hashtable in the same order as the header
				    data = new String[header_row.length];
				    
				    for (i=0; i < header_row.length; i++)
				    {
				    	if (data_to_append.get(record_num).containsKey(header_row[i]))
	                    {
				    		data[i] = (String)data_to_append.get(record_num).get(header_row[i]);
	                    }
				    }
			    	
				    // append the row to the datapool
				    writer.writeNext(data);
		    	}
		     }
		    
		     // Close the datapool
		    
		     writer.close();
		    
		     // Release the datapool lock

	    	 Toolkit.Windows.Selenium.Datapool.Lock.unlock(
	    		 lock
			 );

	    	 System.out.println("Ended Custom_Datapool.Row.append for " + dp_full_path);

	      } catch(IOException e)
	      {
		       e.printStackTrace();	 
	      } 
      
	      return result;
	}
}
