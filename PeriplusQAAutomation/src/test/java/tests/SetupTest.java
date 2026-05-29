package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SetupTest extends BaseTest {

    @Test
    public void testBrowserOpensAndNavigates() {
        // Get the title of the current webpage
        String pageTitle = driver.getTitle();

        System.out.println("The page title is: " + pageTitle);

        Assert.assertTrue(pageTitle.contains("Periplus"), "The page title did not contain 'Periplus'");
    }
}