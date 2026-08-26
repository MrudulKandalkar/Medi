package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By usernameField =
            By.id("Email");

    private By passwordField =
            By.id("Password");

    private By signInButton =
            By.xpath("//button[@value='Login']");


    // Constructor
    public LoginPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }


    // Enter username
    public void enterUsername(String username) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        usernameField
                )
        ).clear();

        driver.findElement(usernameField)
                .sendKeys(username);
    }


    // Enter password
    public void enterPassword(String password) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        passwordField
                )
        ).clear();

        driver.findElement(passwordField)
                .sendKeys(password);
    }


    // Click Sign In
    public void clickSignIn() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        signInButton
                )
        ).click();
    }


    // Complete login
    public void login(String username, String password) {

        enterUsername(username);

        enterPassword(password);

        clickSignIn();
    }
}