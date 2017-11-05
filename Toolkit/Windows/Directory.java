package Toolkit.Windows;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;

public class Directory {

	public static boolean exists(String filename)
	{
		return (new java.io.File(filename)).exists();
	}
	
	public static boolean create(String path)
	{
		java.io.File dir = new java.io.File(path);

		return dir.mkdirs();
	}
	
	
}
