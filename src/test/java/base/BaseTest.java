package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import driver.BrowserManager;

public class BaseTest {

    protected WebDriver driver;

    protected final String BASE_URL =
            "https://medifit.fitnessmanager.in";

    @BeforeMethod
    public void setUp() {

        driver = BrowserManager.createDriver();

        driver.get(BASE_URL);
    }

//    @AfterMethod
//    public void tearDown() {
//
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}