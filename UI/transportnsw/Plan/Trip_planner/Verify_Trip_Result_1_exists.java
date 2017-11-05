package UI.transportnsw.Plan.Trip_planner;

import static Toolkit.Windows.Selenium.Test_Script.*;
import Toolkit.Windows.Selenium.Datapool.Test_Environment;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import Toolkit.Windows.Selenium.BrowserTestObject;
import Toolkit.Windows.Selenium.WebTestObject;




import org.openqa.selenium.By;

public class Verify_Trip_Result_1_exists {
	
	@Then("^a list of trips should be provided$")
	public static void cucumber() throws Exception
	{
		logInfo("Then a list of trips should be provided");
		testMain();
	}

	public static void testMain(
	) throws Exception
	{
		String vp_name = "Trip Result 1 exists", expected = "true", actual = "false";
		
		WebTestObject trip_result_1 = WebTestObject.find(
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
				By.xpath("./div"),
				By.xpath("./div"),
				By.xpath("./ng-transclude"),
				By.xpath("./tni-tab-pane[@id='tp-tab']"),
				By.xpath("./div"),
				By.xpath("./ng-transclude"),
				By.xpath("./div"),
				By.xpath("./div[@id='tp-result-list']"),
				By.xpath("./div"),
				By.xpath("./trip-results"),
				By.xpath("./div[@role='list']"),
				By.xpath("./div[@class='tripResults tp-result-item panel panel-default ng-scope ng-isolate-scope'][1]"),
			}
		);
		
		if (trip_result_1 != null)
			
			actual = "true";

        log_test_result(vp_name, expected, actual);
	}

	public static void main(String[] args) throws Exception
	{
		testMain(
		);
	}

}
