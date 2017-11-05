package Toolkit.Windows;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSV {

	public static Hashtable[] to_hashtable_array(
			String csvFilename, 
			int header_row_num, 
			String[] header_field_text_replace, 
			char separator
	) 
	{
		try {
			// separator = ',';

			// if (separator != null)

			// separator = (char)args[3];

			// String csvFilename = "R:\\Spark Fibre Transfer Report.csv";
			// int header_row_num = 1;

			// CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			CSVReader csvReader = new CSVReader(new FileReader(csvFilename), separator, '"', '\0', 0);
			String[] col = null;
			java.util.List<String[]> myEntries = (java.util.List<String[]>) csvReader.readAll();

			int num_records = myEntries.size();
			Hashtable[] output = new Hashtable[num_records - header_row_num];
			boolean read_header = true;
			String[] header = null;
			int output_index = -1;
			int row_num = 0;

			for (Object myEntry : myEntries) {
				row_num++;

				if (row_num >= header_row_num) {
					if (read_header) {
						header = (String[]) myEntry;

						// By default trim all the header fields (because there
						// may be unwanted spaces)
						for (int header_field_num = 0; header_field_num < header.length; header_field_num++) {
							header[header_field_num] = header[header_field_num].trim();

							// If a list of text replacements are provided
							// (String[])
							if (header_field_text_replace != null) {
								for (int header_field_text_replace_index = 0; header_field_text_replace_index < header_field_text_replace.length; header_field_text_replace_index = header_field_text_replace_index
										+ 2) {
									String text_to_find = header_field_text_replace[header_field_text_replace_index];
									String text_to_replace = header_field_text_replace[header_field_text_replace_index + 1];

									header[header_field_num] = header[header_field_num].replaceAll(text_to_find, text_to_replace);
								}
							}
						}

						read_header = false;
					} else {
						output_index++;
						/*
						 * System.out.println(output_index);
						 * 
						 * int p;
						 * 
						 * if (output_index > 2095)
						 * 
						 * p=0;
						 */

						output[output_index] = new Hashtable();
						String[] data = (String[]) myEntry;

						for (int data_field_num = 0; data_field_num < data.length; data_field_num++)

							output[output_index].put(header[data_field_num], data[data_field_num]);
					}
				}
			}

			csvReader.close();

			return output;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Hashtable[] to_CSV(
			String csvFile, 
			String[][] content
	) 
	{
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(csvFile));

			for (String[] row : content) {
				writer.writeNext(row);
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String appendDQ(String str) {
	    return "\"" + str + "                                                                                                 \"";
	}

}
