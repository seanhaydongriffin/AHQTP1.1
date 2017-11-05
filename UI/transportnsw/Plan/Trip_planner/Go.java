package UI.transportnsw.Plan.Trip_planner;

import static Toolkit.Windows.Selenium.Test_Script.*;

import java.util.ArrayList;

import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import org.openqa.selenium.By;

public class Go {
	
	public static void testMain(
		String From,
		String To,
		String time_option,
		String Search_Date,
		String Search_Hour,
		String Search_Minute
	) throws Exception
	{
		WebTestObject 	tni_tabs			= null,
						form				= null,
						from_dropdown		= null,
						to_dropdown			= null,
						search_footer		= null,
						date_time_selector	= null;
		
		tni_tabs = WebTestObject.find_until_existent(
			null,	
			new By[] {
				By.tagName("body"),
				By.xpath("./main"),
				By.xpath("./div"),
				By.xpath("//section[@id='block-tfnsw-bootstrap-content']"),
				By.xpath("./div"),
				By.xpath("./section"),
				By.xpath("./div"),
				By.xpath("./div"),
				By.xpath("./section"),
				By.xpath("./section"),
				By.xpath("./div"),
				By.xpath("./div"),
				By.xpath("./div[@class='col-xs-12 col-sm-6 col-md-5 col-lg-4 tp-col-left ng-isolate-scope']"),
				By.xpath("./tni-tabs"),
				By.xpath("./div"),
			}
		);
		
		// Trip Planner form
		
		form = WebTestObject.find_until_existent(
			tni_tabs,	
			new By[] {
				By.xpath("./div"),
				By.xpath("./div"),
				By.xpath("./ng-transclude"),
				By.xpath("./tni-tab-pane[@id='tp-tab']"),
				By.xpath("./div"),
				By.xpath("./ng-transclude"),
				By.xpath("./div"),
				By.xpath("./journeys-search-form"),
				By.xpath("./form"),
				By.xpath("./div"),
				By.xpath("./div"),
			}
		);


		
		// From dropdown
		
		from_dropdown = WebTestObject.find_until_existent(
			form,	
			new By[] {
				By.xpath("./origin-destination"),
				By.xpath("./auto-suggest-dropdown[@id='From']"),
			}
		);
		
		// To dropdown
		
		to_dropdown = WebTestObject.find_until_existent(
			form,	
			new By[] {
				By.xpath("./origin-destination"),
				By.xpath("./auto-suggest-dropdown[@id='To']"),
			}
		);
		
		// Search Footer
		
		search_footer = WebTestObject.find_until_existent(
			form,	
			new By[] {
				By.xpath("./div[@class='tp-search-footer']"),
			}
		);

		if (!From.equals(""))
		{
			WebTestObject.find_until_existent(
				from_dropdown,	
				new By[] {
					By.xpath("./input"),
				}
			).set_text(From);

			WebTestObject.find_until_existent(
				from_dropdown,	
				new By[] {
					By.xpath("./div"),
					By.xpath("./div"),
					By.xpath("//div[text()='" + From + "']"),
				}
			).click();
			
			if (Test_Environment.browser_name.equals("Chrome") ||
				Test_Environment.browser_name.equals("IE"))
			{
				// In some browsers the from dropdown can re-display for no good reason.
				//	To ensure the dropdown is removed we click on the Trip Planner tab below.

				WebTestObject.find_until_existent(
					from_dropdown,	
					new By[] {
						By.xpath("./input"),
					}
				).click();
				
				WebTestObject.find_until_existent(
					tni_tabs,	
					new By[] {
						By.xpath("./ul"),
						By.xpath("./li[@class='ng-scope active']"),
					}
				).click();
			}
		}
		
		if (!To.equals(""))
		{
			WebTestObject.find_until_existent(
				to_dropdown,	
				new By[] {
					By.xpath("./input"),
				}
			).set_text(To);
			
			WebTestObject.find_until_existent(
				to_dropdown,	
				new By[] {
					By.xpath("./div"),
					By.xpath("./div"),
					By.xpath("//div[text()='" + To + "']"),
				}
			).click();

			if (Test_Environment.browser_name.equals("Chrome") ||
				Test_Environment.browser_name.equals("IE"))
			{
				// In some browsers the from dropdown can re-display for no good reason.
				//	To ensure the dropdown is removed we click on the Trip Planner tab below.

				WebTestObject.find_until_existent(
					to_dropdown,	
					new By[] {
						By.xpath("./input"),
					}
				).click();
				
				WebTestObject.find_until_existent(
					tni_tabs,	
					new By[] {
						By.xpath("./ul"),
						By.xpath("./li[@class='ng-scope active']"),
					}
				).click();
			}
		}
		
		if (!time_option.equals(""))
		{			
			WebTestObject.find_until_existent(
				search_footer,	
				new By[] {
					By.xpath("./span"),
					By.xpath("./a")
				}
			).click();

			// Date Time Selector
			
			date_time_selector = WebTestObject.find_until_existent(
				search_footer,	
				new By[] {
					By.xpath("./date-time-selector"),
					By.xpath("./div"),
				}
			);
			
			WebTestObject.find_until_existent(
				date_time_selector,	
				new By[] {
					By.xpath("./div"),
					By.xpath("./div"),
					By.xpath("//span[text()='" + time_option + "']")
				}
			).click();
		}

		if (!Search_Date.equals(""))

			WebTestObject.find_until_existent(
				date_time_selector,	
				new By[] {
					By.xpath("//div[@class='form-group date']"),
					By.xpath("./div"),
					By.xpath("./select"),
					By.xpath("./option[@label='" + Search_Date + "']"),
				}
			).click();
		
		if (!Search_Hour.equals(""))

			WebTestObject.find_until_existent(
				date_time_selector,	
				new By[] {
					By.xpath("//div[@class='form-group hour']"),
					By.xpath("./div"),
					By.xpath("./select"),
					By.xpath("./option[@label='" + Search_Hour + "']"),
				}
			).click();
		
		if (!Search_Minute.equals(""))

			WebTestObject.find_until_existent(
				date_time_selector,	
				new By[] {
					By.xpath("//div[@class='form-group minute']"),
					By.xpath("./div"),
					By.xpath("./select"),
					By.xpath("./option[@label='" + Search_Minute + "']"),
				}
			).click();
		
		logInfo("screenshot");

		// Go
		
		WebTestObject.find_until_existent(
			search_footer,	
			new By[] {
				By.xpath("./div[@class='tp-buttons-panel']"),
				By.xpath("./button[@id='search-button']")
			}
		).click();

	}

	public static void main(String[] args) throws Exception 
	{
		testMain(
			"",
			"",
			"",
			"",
			"",
			""
		);
	}
}
