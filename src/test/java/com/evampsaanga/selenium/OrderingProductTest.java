package com.evampsaanga.selenium;

import com.aqanetics.common.base.selenium.SeleniumTest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import si.paurusr.aqa.AQAInfo;
import si.paurusr.aqa.Utils.Helpers;

public class OrderingProductTest extends SeleniumTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderingProductTest.class);

  public OrderingProductTest() {
    super(true);
  }


  @Test
  @AQAInfo(metadata = {
      "WEBSHOP"}, name = "orderingProduct", description = "Login as a customer and order a product.")
  public void orderingProductTest() throws InterruptedException {
    LOGGER.info("Navigating to the login page...");
    getDriver().navigate()
        .to("http://115.186.58.47/marketplace_prod/default/customer/account/login");

    Helpers helpers = new Helpers(getDriver());
    LOGGER.info("Using the Customer user to login to the platform ...");
    //helpers.login(evampsaangaUserName, evampsaangaUserPassword);
    helpers.login("shahzaib@evampsaanga.com", "Test123+");
    getDriver().navigate().to("http://115.186.58.47/marketplace_prod/default/products.html?cat=7");

    LOGGER.info("Searching for the correct product ...");

    helpers.assertProductDetails("Test Product", "Sample short description.");

    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
    WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
        "//div[@class='card-body card-body-grey']//p[text()='Test Product']/ancestor::div[contains(@class, 'card')]//a[@class='card-seller-img']")));

    LOGGER.info("Product found.");
    LOGGER.info("Opening the product ...");

    // Click on the product link
    helpers.waitForSpinner();
    helpers.waitAndClick(productLink);

    wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//a[@class='action towishlist' and @data-action='add-to-wishlist']")));

    LOGGER.info("Adding the product to the cart ...");

    WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//button[@id='product-addtocart-button' and @class='btn btn-add-cart']")));
    helpers.waitAndClick(addToCart);
    helpers.waitForSpinner();
    WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//span[@class='counter-number' and string-length(text()) > 0]")));
    helpers.waitAndClick(cart);
    helpers.waitForSpinner();

    LOGGER.info("Checking out the product ...");

    WebElement checkout = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("top-cart-btn-checkout")));
    helpers.waitAndClick(checkout);
    helpers.waitForSpinner();
    LOGGER.info("Setting the correct shipping option ...");

    WebElement shippingRadioButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//input[@type='radio' and @value='flatrate_flatrate']")));
    helpers.waitAndClick(shippingRadioButton);
    helpers.waitForSpinner();

    WebElement next = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("shipping-method-buttons-container")));
    helpers.waitAndClick(next);
    helpers.waitForSpinner();

    LOGGER.info("Setting the correct customer number ...");

    WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("okBtn")));
    helpers.waitForSpinner();
    helpers.waitAndClick(okButton);
    helpers.waitForSpinner();

    LOGGER.info("Submitting the customer details ...");

    WebElement submitButton = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("submitBtn")));
    helpers.waitForSpinner();
    helpers.waitAndClick(submitButton);
    helpers.waitForSpinner();

    WebElement proceedButton = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("paymentnext")));
    helpers.waitForSpinner();
    helpers.waitAndClick(proceedButton);
    helpers.waitForSpinner();

    LOGGER.info("Selecting the desired payment option ...");

    WebElement cashRadioButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//input[@type='radio' and @value='cashondelivery']")));
    helpers.waitForSpinner();
    helpers.waitAndClick(cashRadioButton);
    helpers.waitForSpinner();

    LOGGER.info("Placing the order ...");

    WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//button[@title='Place Order' and contains(@data-bind, 'enable')]")));
    helpers.waitForSpinner();
    helpers.waitAndClick(placeOrderButton);

    LOGGER.info("Waiting for the success message to appear ...");

    WebDriverWait waitForFinish = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
    waitForFinish.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "//div[@class='thank-you-div']//h3[@class='thank-you-text' and text()='Thank you for your purchase']")));

    LOGGER.info("Extracting the order id ...");

    String orderId = helpers.extractFullOrderId();

    getDriver().navigate()
        .to("http://115.186.58.47/marketplace_prod/default/user/account/mybookings");

    waitForFinish.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//table[@id='my-products-orders']")));

    Thread.sleep(2000);

    LOGGER.info("Verifying that the order is visible in My Orders table ...");
    helpers.assertOrderPresent(orderId);

    WebElement stripButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("toggle-1")));
    helpers.waitAndClick(stripButton);
    Thread.sleep(1000);

    WebElement logout = wait.until(
        ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Logout']")));
    helpers.waitAndClick(logout);

    Thread.sleep(3000);

    LOGGER.info("Logging in with the merchant ...");

    getDriver().navigate()
        .to("http://115.186.58.47/marketplace_prod/default/customer/account/login");
    helpers.login("ali@evampsaanga.com", "Test123@");

    LOGGER.info("Opening the manage orders page ...");

    getDriver().navigate()
        .to("http://115.186.58.47/marketplace_prod/default/merchant/account/booking/id/" + orderId);

    LOGGER.info("Verifying the order is visible in the manage orders table ...");

    LOGGER.info("Verifying the admin notification for the Merchant ... ");


    WebElement totalOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
        "//div[contains(@class, 'mer-orders-pay-meth') and .//h6[text()='Grand Total']]")));
    Assert.assertTrue(totalOrder.isDisplayed(), "The 'Grand Total' element is not visible.");

    LOGGER.info("Order with the correct id is found ...");

  }
}
