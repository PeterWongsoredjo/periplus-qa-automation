package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By emailField = By.name("email");
    private final By passwordField = By.id("ps");
    private final By loginButton = By.id("button-login");
    private final By loggedInIndicator = By.id("nav-signin-text");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String email, String password) {
        visible(emailField).sendKeys(email);
        visible(passwordField).sendKeys(password);
        click(loginButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInIndicator));
        return new HomePage(driver);
    }
}
