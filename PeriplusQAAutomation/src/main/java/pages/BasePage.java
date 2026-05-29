package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final By PRELOADER = By.cssSelector("div.preloader");
    private static final By MODAL_BACKDROP = By.cssSelector("div.modal-backdrop");
    private static final By MODAL_DIALOG = By.cssSelector("div.modal-dialog");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Success modal wait
    protected void waitForPageReady() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(PRELOADER));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_BACKDROP));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_DIALOG));
    }

    protected WebElement click(By locator) {
        waitForPageReady();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        return element;
    }

    protected WebElement visible(By locator) {
        waitForPageReady();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
