// package driver;

// import java.time.Duration;

// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;

// public class BrowserManager {

//     public static WebDriver createDriver() {

//         ChromeOptions options = new ChromeOptions();

//         options.addArguments("--start-maximized");

//         WebDriver driver = new ChromeDriver(options);

//         driver.manage().timeouts()
//                 .implicitlyWait(Duration.ofSeconds(10));

//         driver.manage().timeouts()
//                 .pageLoadTimeout(Duration.ofSeconds(30));

//         return driver;
//     }
// }


package driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserManager {

    public static WebDriver createDriver() {

        ChromeOptions options =
                new ChromeOptions();

        // ========================================
        // GitHub Actions / Linux
        // ========================================

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        // ========================================
        // Browser size
        // ========================================

        options.addArguments("--window-size=1920,1080");

        // ========================================
        // Stability
        // ========================================

        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");

        WebDriver driver =
                new ChromeDriver(options);

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(10));

        driver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30));

        return driver;
    }
}
