package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage.login(
                "MrudulKandalkar",
                "Megod@143"
        );
    }
}