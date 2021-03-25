package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class WebdriverInstance {

    public final String registerUrl = "https://feature-qa.customer-frontend.staging.aspireapp.com/sg/register";
    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/driver/geckodriver");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void close() {
        driver.close();
    }

    public void openUrl(String registerURL) {
        driver.navigate().to(registerURL);
    }
}
