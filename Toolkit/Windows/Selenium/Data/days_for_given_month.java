package Toolkit.Windows.Selenium.Data;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * Description : Functional Script 
 * Author : Anjana Kulasinghe
 *
 */

public class days_for_given_month
{

	public static int testMain(String get_due_date) 
	{
		//Split into Array for processing
		String[] date_array = get_due_date.toString().split("/");
				
		//Identify Date elements
		int iDay   = Integer.parseInt(date_array[0]);
		int iMonth = getMonth(Integer.parseInt(date_array[1])-1);
		int iYear  = Integer.parseInt(date_array[2]);

		Calendar mycal = new GregorianCalendar (iYear, iMonth, iDay);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return daysInMonth;
	}

	static int getMonth(int mthNo)
	{
		int month = 99;
		
		switch (mthNo)
		{
			case 0:
				month = Calendar.JANUARY;
				break;
			case 1:
				month = Calendar.FEBRUARY;
				break;
			case 2:
				month = Calendar.MARCH;
				break;
			case 3:
				month = Calendar.APRIL;
				break;
			case 4:
				month = Calendar.MAY;
				break;
			case 5:
				month = Calendar.JUNE;
				break;
			case 6:
				month = Calendar.JULY;
				break;
			case 7:
				month = Calendar.AUGUST;
				break;
			case 8:
				month = Calendar.SEPTEMBER;
				break;
			case 9:
				month = Calendar.OCTOBER;
				break;
			case 10:
				month = Calendar.NOVEMBER;
				break;
			case 11:
				month = Calendar.DECEMBER;
				break;
		}
		
		return month;
	}

}
