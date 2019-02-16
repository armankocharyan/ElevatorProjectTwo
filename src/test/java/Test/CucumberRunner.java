package Test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(
		format = {"pretty", "json:target/cucumber.json"},
		features = {"Features"},
		glue = {"Test"}
		)
public class CucumberRunner {

}