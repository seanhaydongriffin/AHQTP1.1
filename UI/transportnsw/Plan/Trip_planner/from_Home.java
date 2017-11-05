package UI.transportnsw.Plan.Trip_planner;

import static Toolkit.Windows.Selenium.Test_Script.*;

import java.util.ArrayList;

import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import org.openqa.selenium.By;

public class from_Home {
	
	public static void testMain(
	) throws Exception
	{
		// Plan
		
		WebTestObject plan = WebTestObject.find_until_existent(
			null,	
			new By[] {
				By.tagName("body"),
				By.xpath("./div[@class='tfnsw-sticky-header-spacer-new']"),
				By.xpath("./div"),
				By.xpath("./div"),
				By.xpath("./div[@class='max-site-width main-header']"),
				By.xpath("./div"),
				By.xpath("./div[@class='col-sm-9 navigation-container']"),
				By.xpath("./div"),
				By.xpath("./div[@class='col-sm-10 col-md-11 col-lg-9']"),
				By.xpath("./nav"),
				By.xpath("./div[@id='tfnsw-navigation-primary-new']"),
				By.xpath("./ul"),
				By.xpath("./li[contains(., 'Plan')]")
			}
		);
		
		plan.hover();

		// Trip Planner
		
		WebTestObject trip = WebTestObject.find_until_existent(
			plan,	
			new By[] {
				By.xpath("./div[@id='/plan']"),
				By.xpath("./div[@class='nav-container']"),
				By.xpath("./ul"),
				By.xpath("//a[@href='/trip']"),
			}
		);

		logInfo("screenshot browser");

		trip.click();
	}

	public static void main(String[] args) throws Exception 
	{
		testMain(
		);
	}
}
