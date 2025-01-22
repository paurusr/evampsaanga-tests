package si.paurusr.aqa.Utils;

import com.aqanetics.common.base.selenium.BasePage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;



public class Helpers extends BasePage {

  private static final Logger LOGGER = LoggerFactory.getLogger(Helpers.class);

  public Helpers(WebDriver driver) {
    super(driver);
  }

  public void scrollPageToTop() {
    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
  }


  public void login(String username, String password) {
    try {

      // Find the username input field and enter the username
      WebElement usernameField = driver.findElement(By.id("email"));
      usernameField.clear(); // Clear any pre-filled data
      usernameField.sendKeys(username);

      // Find the password input field and enter the password
      WebElement passwordField = driver.findElement(By.id("pass"));
      passwordField.clear(); // Clear any pre-filled data
      passwordField.sendKeys(password);

      // Find and click the login button
      WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
      loginButton.click();

      // Optional: Add a small wait to allow the page to load
      Thread.sleep(2000);

      // Verify successful login
      if (driver.getCurrentUrl().contains("dashboard") || driver.getCurrentUrl()
          .contains("default")) {
        System.out.println("Login successful for user: " + username);
      } else {
        System.out.println("Login failed for user: " + username);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred during login for user: " + username);
    }
  }


  public void expandFormSection(String sectionLabel) {
    try {
      // Wait for the form container to be visible
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement formContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.className("formSections")
      ));

      // Locate the section header based on the provided label
      WebElement sectionHeader = formContainer.findElement(
          By.xpath(".//div[@class='itemHeader' and ./h2[text()='" + sectionLabel + "']]")
      );

      // Check if the section is collapsed (not expanded)
      WebElement parentSection = sectionHeader.findElement(By.xpath(".."));
      if (!parentSection.getAttribute("class").contains("expanded")) {
        // Click to expand the section
        waitAndClick(sectionHeader);
        System.out.println("Expanded the section: " + sectionLabel);
        Thread.sleep(1000);

      } else {
        System.out.println("Section already expanded: " + sectionLabel);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while expanding the section: " + sectionLabel);
    }
  }

  public void fillInputField(String inputId, String value) {
    try {
      // Locate the input field by its ID
      WebElement inputField = driver.findElement(By.id(inputId));

      // Clear any existing value
      waitAndClear(inputField);

      // Enter the new value
      waitAndSendKeys(inputField, value);
      Thread.sleep(500);

      System.out.println("Filled input field with ID: " + inputId + " with value: " + value);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while filling the input field with ID: " + inputId);
    }
  }

  public void fillTinyMCEEditor(String iframeId, String text) {
    try {

      // Switch to the TinyMCE iframe
      // Thread.sleep(2000);
      scrollPageToTop();
      driver.switchTo().frame(getAndWaitForElement("//*[@id='" + iframeId + "']", 15));

      // Locate the <body> element inside the TinyMCE editor
      WebElement editorBody = driver.findElement(By.tagName("body"));
      waitForElementToBeClickable(editorBody);

      // Clear any existing content
      waitAndClear(editorBody);

      // Enter the new content
      waitAndSendKeys(editorBody, text);

      System.out.println("Successfully entered text into the TinyMCE editor.");

      // Switch back to the main content
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while interacting with the TinyMCE editor.");
    }
  }

  public void selectOptionByValue(String dropdownId, String value) {
    try {
      // Locate the dropdown element by its ID
      WebElement dropdown = driver.findElement(By.id(dropdownId));

      // Use the Select class to interact with the dropdown
      Select select = new Select(dropdown);

      // Select the option by its value
      select.selectByValue(value);

      System.out.println("Successfully selected the option with value: " + value);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while selecting the option with value: " + value);
    }
  }

  public void selectOptionByTyping(String dropdownSearchFieldClass, String optionText) {
    try {
      // Locate the search field within the dropdown
      WebElement searchField = driver.findElement(By.className(dropdownSearchFieldClass));

      // Type the option text into the search field
      searchField.sendKeys(optionText);

      // Wait for the dropdown options to populate
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement option = wait.until(
          ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//li[contains(text(), '" + optionText + "')]"))
      );

      // Select the desired option
      option.click();

      System.out.println("Successfully selected option: " + optionText);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while selecting the option: " + optionText);
    }
  }


  public void clickSubmitAndConfirmSave(String submitButtonId, String loaderClass) {
    try {
      // Click the submit button
      WebElement submitButton = driver.findElement(By.id(submitButtonId));
      submitButton.click();
      System.out.println("Submit button clicked.");

      // Wait for the loader to appear (optional, to ensure loader is displayed)
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(loaderClass)));

      // Wait for the loader to disappear
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(loaderClass)));
      System.out.println("Loader disappeared. Form submission process completed.");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("An error occurred while waiting for the loader to disappear.");
    }
  }

  public void assertProductCreated(String productName, String productPrice) {
    // Wait for the product list to load
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Assert the product name is present
    WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//td[text()='" + productName + "']")
    ));
    Assert.assertTrue(nameElement.isDisplayed(),
        "Product name not found in the list: " + productName);

    // Assert the product price is present
    WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//td[text()='" + productPrice + "']")
    ));
    Assert.assertTrue(priceElement.isDisplayed(),
        "Product price not found in the list: " + productPrice);

    System.out.println(
        "Product successfully created: " + productName + " with price " + productPrice);
  }

  public void assertProductDetails(String productName, String productDescription) {
    // Wait for the product card to appear
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    // Verify the product name
    WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//p[@class='card-p-top' and text()='" + productName + "']")
    ));
    Assert.assertTrue(nameElement.isDisplayed(), "Product name not found: " + productName);

    // Verify the product description
    WebElement descriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath(
            "//div[@class='card-text text-trim']/p[contains(text(), '" + productDescription + "')]")
    ));
    Assert.assertTrue(descriptionElement.isDisplayed(),
        "Product description not found: " + productDescription);

    System.out.println("Product details verified: " + productName + " | " + productDescription);
  }

  public String extractFullOrderId() {
    try {
      // Locate the strong element containing the full order ID
      WebElement orderIdElement = driver.findElement(
          By.xpath("//p[@class='card-text-billing']/a[@class='order-number']/strong"));

      // Extract the text from the strong element
      String fullOrderId = orderIdElement.getText();

      System.out.println("Extracted Full Order ID: " + fullOrderId);
      return fullOrderId;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Failed to extract the full order ID.");
      return null;
    }
  }

  public void assertOrderPresent(String orderId) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

      // Wait for the specific order ID to appear in the table
      WebElement orderElement = wait.until(ExpectedConditions.presenceOfElementLocated(
          By.xpath("//table[@id='my-products-orders']//td[@class='orderIdCol']/a[text()='" + orderId
              + "']")
      ));

      // Assert that the order element is not null
      Assert.assertNotNull(orderElement, "Order ID " + orderId + " is not present in the table.");
      System.out.println("Assertion passed: Order ID " + orderId + " is present in the table.");
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Order ID " + orderId + " is not present in the table within the timeout.");
    }
  }

  public void uploadImage(String filePath) {
    try {
      // Locate the file input element
      ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
      WebElement fileInput = driver.findElement(By.name("Image Gallery[]"));

// Send the file path to the input element
      fileInput.sendKeys(filePath);

      System.out.println("File uploaded successfully: " + filePath);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Failed to upload file: " + filePath);
    }
  }

  public boolean waitForSpinner() {
    try {
      new WebDriverWait(driver, Duration.ofSeconds(20))
          .until(
              webDriver ->
                  ExpectedConditions.invisibilityOfElementLocated(
                          By.xpath(
                              "//*[contains(@class, 'spinner') or contains(@class, 'loading') and not(@style='display: none;')]"))
                      .apply(webDriver));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public String getResourcePath(String relativePath) {
    // Dynamically resolve the resource file path
    return new File(ClassLoader.getSystemResource(relativePath).getFile()).getAbsolutePath();
  }

  public String getAbsolutePath(String relativePath) {
    return System.getProperty("user.dir") + "/src/test/resources/" + relativePath;
  }

  public void checkAndDeleteAllProducts(String productName) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

      while (true) {
        // Search for the product name in the search input
        LOGGER.info("Searching for the correct product...");
        WebElement searchInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("search-input")));
        waitAndClear(searchInput);
        waitAndSendKeys(searchInput, productName);

        // Locate all rows containing the product name
        String xpathForRows =
            "//table[@id='my-products-table']//tr[td[contains(text(),'" + productName + "')]]";
        List<WebElement> productRows = driver.findElements(By.xpath(xpathForRows));

        if (productRows.isEmpty()) {
          LOGGER.info(
              "No products found with the name: " + productName + ". All products deleted.");
          break; // Exit the loop if no more rows are found
        }

        LOGGER.info("Found " + productRows.size() + " product(s) with the name: " + productName);

        // Iterate through each row and delete the product
        for (WebElement productRow : productRows) {
          try {
            // Re-fetch the delete button to avoid stale element
            WebElement deleteButton = productRow.findElement(By.id("remove-btn"));
            waitAndClick(deleteButton);

            // Wait for the confirmation dialog to appear
            WebElement confirmationDeleteButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("a.btn.delete-btn.btn-danger")));
            waitAndClick(confirmationDeleteButton);

            // Wait for the modal to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("div.modal-content")));

            LOGGER.info("Deleted a product with the name: " + productName);
          } catch (Exception e) {
            LOGGER.error("Failed to delete a product with the name: " + productName, e);
          }
        }

        // Refresh the page after each deletion to re-fetch rows
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-input")));
      }
    } catch (Exception e) {
      LOGGER.error("Error while attempting to delete products with the name: " + productName, e);
    }
  }

  public void verifyProductPersence(String ProductName) {
    return;
  }

  public void clickDeleteAndConfirmDeletion() {
  }


}
