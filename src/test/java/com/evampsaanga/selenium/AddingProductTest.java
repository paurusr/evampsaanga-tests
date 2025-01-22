package com.evampsaanga.selenium;

import com.aqanetics.common.base.selenium.SeleniumTest;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import si.paurusr.aqa.AQAInfo;
import si.paurusr.aqa.Utils.Helpers;

public class AddingProductTest extends SeleniumTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddingProductTest.class);
  String assetPath = "phoneProductImage.png";
  String productName = "Test Product";

  public AddingProductTest() {
    super(true);
  }


  @Test
  @AQAInfo(
      metadata = {"WEBSHOP"},
      name = "addingProduct",
      description = "Login as a merchant and add a new product.")
  public void addingProductTest() throws InterruptedException {
    LOGGER.info("Navigating to the login page...");
    getDriver().navigate()
        .to("http://115.186.58.47/marketplace_prod/default/customer/account/login");

    Helpers helpers = new Helpers(getDriver());
    LOGGER.info("Using the Merchant user to login to the platform ...");
    //helpers.login(evampsaangaUserName, evampsaangaUserPassword);
    helpers.login("ali@evampsaanga.com", "Test123@");

    // Click the button
    LOGGER.info("Merchant is adding a new product...");
    LOGGER.info("Clicking on the \"Add Product\" button on the dashboard.");
    WebElement addProductButton = getDriver().findElement(By.className("btn-add-product"));
    addProductButton.click();

    // Wait for the dropdown to appear
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    WebElement attributeSetDropdown = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.id("attributeSetSelect")));

    // Select an option from the dropdown
    LOGGER.info("Selecting 'Digital Services' option from the dropdown...");
    Select select = new Select(attributeSetDropdown);
    select.selectByVisibleText("Digital Services");

    // Wait for the main form container to be visible
    Thread.sleep(5000);

    LOGGER.info("Digital services form is now loaded.");

    helpers.expandFormSection("General");
    Thread.sleep(500);

    LOGGER.info("Filling out the product details...");

    LOGGER.info("Filling out name.");
    helpers.fillInputField("name", productName);
    LOGGER.info("Filling out price.");
    helpers.fillInputField("price", "1");

    helpers.fillTinyMCEEditor("specifications_ifr", "Dubai Silicon Oasis");
    helpers.fillInputField("service_city", "Dubai");
    helpers.fillInputField("service_delivery_address", "P.O. Box: 341041");

    helpers.expandFormSection("Content");
    Thread.sleep(500);

    LOGGER.info("Filling out descriptions.");
    helpers.fillTinyMCEEditor("description_ifr", "Sample description text.");
    helpers.fillTinyMCEEditor("short_description_ifr", "Sample short description.");
    LOGGER.info("Attaching the Product image.");

    helpers.expandFormSection("Images");
    String filePath = helpers.getAbsolutePath(assetPath);
    helpers.uploadImage(ClassLoader.getSystemResource("phoneProductImage.png").getPath());
    Thread.sleep(2000);

    helpers.expandFormSection("Subscription Settings");
    helpers.selectOptionByValue("subscription_plans", "1");

    helpers.expandFormSection("Product Details");
    LOGGER.info("Setting up the correct product category.");
    helpers.selectOptionByTyping("select2-search__field", "Phones");
    Thread.sleep(500);

    // Submit the form and wait for the loader
    LOGGER.info("Submitting the product form...");
    helpers.clickSubmitAndConfirmSave("addBtn", "ringLoader");

    LOGGER.info("Verifying the product was successfully created...");

    helpers.assertProductCreated("Test Product", "AZN1.00");

    LOGGER.info("Verifying the product is present in the Managed products list ...");
    LOGGER.info("Verifying the product is present in the main marketplace ...");
    helpers.verifyProductPersence(productName);

  }

}
