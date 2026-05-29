package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    private final By firstProductLink = By.cssSelector("div.single-product .product-img a");
    private final By addToCartButton = By.cssSelector("button.btn-add-to-cart");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage selectFirstProduct() {
        click(firstProductLink);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        return this;
    }

    public void addToCart() {
        click(addToCartButton);
    }
}
