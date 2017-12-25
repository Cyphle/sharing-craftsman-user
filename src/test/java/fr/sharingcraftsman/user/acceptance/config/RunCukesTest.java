package fr.sharingcraftsman.user.acceptance.config;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "fr.sharingcraftsman.acceptance"
)
public class RunCukesTest {
}
