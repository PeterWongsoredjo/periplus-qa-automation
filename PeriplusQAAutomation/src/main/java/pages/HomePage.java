package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ConfigReader;

import java.util.List;

public class HomePage extends BasePage {

    private final By searchInput = By.id("filter_name_desktop");
    private final By cartCounter = By.id("cart_total");
    private final By loggedInIndicator = By.id("nav-signin-text");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public LoginPage goToLogin() {
        driver.get(ConfigReader.getProperty("baseUrl") + "account/Login");
        return new LoginPage(driver);
    }

    public ProductPage search(String query) {
        WebElement input = visible(searchInput);
        // Clear any pre-filled query so consecutive searches do not concatenate
        input.clear();
        input.sendKeys(query, Keys.ENTER);
        return new ProductPage(driver);
    }

    public int getCartCount() {
        String text = visible(cartCounter).getText().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public void waitForCartCount(int expected) {
        wait.until(ExpectedConditions.textToBe(cartCounter, String.valueOf(expected)));
    }

    public CartPage goToCart() {
        driver.get(ConfigReader.getProperty("baseUrl") + "checkout/cart");
        return new CartPage(driver);
    }

    public boolean isUserLoggedIn() {
        List<WebElement> indicators = driver.findElements(loggedInIndicator);
        if (indicators.isEmpty()) {
            return false;
        }
        String text = indicators.get(0).getText().trim();
        return !text.isEmpty() && !text.equalsIgnoreCase("Sign In");
    }
}
