package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartPage extends BasePage {

    private static final Pattern RUPIAH = Pattern.compile("Rp\\s*([\\d.,]+)");

    private final By itemRow = By.cssSelector("div.row-cart-product");
    private final By itemPrice = By.cssSelector(".col-lg-10 > div:nth-of-type(3)");
    private final By quantityInput = By.cssSelector("input.input-number");
    private final By increaseButton = By.cssSelector("button[data-type='plus']");
    private final By decreaseButton = By.cssSelector("button[data-type='minus']");
    private final By updateButton = By.cssSelector("input[value='Update']");
    private final By removeButton = By.cssSelector("a.btn-cart-remove");
    private final By subtotal = By.id("sub_total");
    private final By voucherInput = By.name("Coupon");
    private final By voucherApply = By.cssSelector("div.coupon button");
    private final By voucherError = By.cssSelector("div.coupon .text-danger");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return driver.findElements(itemRow).size();
    }

    public int getItemPrice(int index) {
        List<WebElement> rows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(itemRow, index));
        return parseRupiah(rows.get(index).findElement(itemPrice).getText());
    }

    public int getFirstItemPrice() {
        return getItemPrice(0);
    }

    public void setQuantity(int qty) {
        WebElement input = visible(quantityInput);
        int current = Integer.parseInt(input.getAttribute("value").trim());
        String previousSubtotal = getSubtotalText();
        // Step the quantity with the +/- controls, clearing the field would trip the min-value alert
        while (current < qty) {
            click(increaseButton);
            current++;
        }
        while (current > qty) {
            click(decreaseButton);
            current--;
        }
        click(updateButton);
        // Wait until the subtotal reflects the recalculated value
        wait.until(d -> {
            try {
                return !d.findElement(subtotal).getText().equals(previousSubtotal);
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public int getSubtotal() {
        return parseRupiah(getSubtotalText());
    }

    public void removeFirstItem() {
        click(removeButton);
    }

    public void clearCart() {
        int count = getItemCount();
        while (count > 0) {
            click(removeButton);
            int remaining = count - 1;
            wait.until(d -> d.findElements(itemRow).size() == remaining);
            count = remaining;
        }
    }

    public boolean isEmpty() {
        wait.until(ExpectedConditions.numberOfElementsToBe(itemRow, 0));
        return driver.findElements(itemRow).isEmpty();
    }

    public void applyVoucher(String code) {
        visible(voucherInput).sendKeys(code);
        click(voucherApply);
    }

    public String getVoucherError() {
        return visible(voucherError).getText();
    }

    private String getSubtotalText() {
        return visible(subtotal).getText();
    }

    private int parseRupiah(String raw) {
        Matcher matcher = RUPIAH.matcher(raw);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No Rupiah amount found in: " + raw);
        }
        return Integer.parseInt(matcher.group(1).replaceAll("[^0-9]", ""));
    }
}
