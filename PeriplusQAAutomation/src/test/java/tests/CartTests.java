package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import utils.ConfigReader;

public class CartTests extends BaseTest {

    private static final String QUERY_JAVA = "Java";
    private static final String QUERY_HARRY_POTTER = "Harry Potter";
    private static final String QUERY_DUNE = "Dune";
    private static final String INVALID_VOUCHER = "INVALIDTEST2026";

    @Test
    public void testTC001_AddSingleItemToCart() {
        HomePage home = login();
        addProduct(home, QUERY_JAVA);
        home.waitForCartCount(1);
        Assert.assertEquals(home.goToCart().getItemCount(), 1, "Cart should contain 1 item");
    }

    @Test
    public void testTC002_AddMultipleUniqueItems() {
        HomePage home = login();
        addProduct(home, QUERY_HARRY_POTTER);
        home.waitForCartCount(1);
        addProduct(home, QUERY_DUNE);
        home.waitForCartCount(2);
        Assert.assertEquals(home.goToCart().getItemCount(), 2, "Cart should contain 2 items");
    }

    @Test
    public void testTC003_UpdateItemQuantity() {
        HomePage home = login();
        addProduct(home, QUERY_JAVA);
        home.waitForCartCount(1);
        CartPage cart = home.goToCart();
        // With a single item the subtotal equals the item's unit price
        int initialSubtotal = cart.getSubtotal();
        cart.setQuantity(2);
        Assert.assertEquals(cart.getSubtotal(), initialSubtotal * 2,
                "Subtotal should equal unit price times quantity");
    }

    @Test
    public void testTC004_RemoveItemFromCart() {
        HomePage home = login();
        addProduct(home, QUERY_JAVA);
        home.waitForCartCount(1);
        CartPage cart = home.goToCart();
        cart.removeFirstItem();
        Assert.assertTrue(cart.isEmpty(), "Cart should be empty after removing the item");
    }

    @Test
    public void testTC005_VerifySubtotalAccuracy() {
        HomePage home = login();
        addProduct(home, QUERY_HARRY_POTTER);
        home.waitForCartCount(1);
        addProduct(home, QUERY_DUNE);
        home.waitForCartCount(2);
        CartPage cart = home.goToCart();
        int priceA = cart.getItemPrice(0);
        int priceB = cart.getItemPrice(1);
        Assert.assertEquals(cart.getSubtotal(), priceA + priceB,
                "Subtotal should equal the sum of both item prices");
    }

    @Test
    public void testTC006_GuestToMemberCartMerge() {
        HomePage home = new HomePage(driver);
        // Confirm the session starts as an unauthenticated guest
        Assert.assertFalse(home.isUserLoggedIn(), "Session should start logged out");
        addProduct(home, QUERY_JAVA);
        home.waitForCartCount(1);
        home.goToLogin().login(
                ConfigReader.getProperty("testEmail"),
                ConfigReader.getProperty("testPassword"));
        Assert.assertTrue(home.goToCart().getItemCount() >= 1,
                "Guest cart item should be retained after login");
    }

    @Test
    public void testTC007_ApplyInvalidVoucher() {
        HomePage home = login();
        addProduct(home, QUERY_JAVA);
        home.waitForCartCount(1);
        home.goToCart();
        // Skip only the voucher application: the coupon UI is commented out on the live cart
        throw new SkipException("Voucher UI not present on live Periplus cart (coupon block is "
                + "commented out in the DOM); CartPage.applyVoucher/getVoucherError are ready for "
                + "when it is re-enabled. Target voucher: " + INVALID_VOUCHER);
    }

    private HomePage login() {
        HomePage home = new HomePage(driver).goToLogin().login(
                ConfigReader.getProperty("testEmail"),
                ConfigReader.getProperty("testPassword"));
        // Start each test from an empty cart since the account cart persists server-side
        home.goToCart().clearCart();
        return home;
    }

    private void addProduct(HomePage home, String query) {
        home.search(query).selectFirstProduct().addToCart();
    }
}
