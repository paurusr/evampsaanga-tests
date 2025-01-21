package com.evampsaanga.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.aqanetics.AQAInfo;
import com.aqanetics.common.base.selenium.BasePage;
import com.aqanetics.common.base.selenium.SeleniumTest;

public class GoogleTest extends SeleniumTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTest.class);
  public static final String GOOGLE_URL = "https://google.com";
  public static final String CONFIRM_BUTTON = "//img[@alt='Google']/../../..//button[2]";

  GoogleTest() {
    super(true);
  }

  @Test
  @AQAInfo(name = "OpenGoogle", metadata = {"UI, Selenium"})
  public void openGoogleTest() {
    getDriver().get(GOOGLE_URL);
    LOGGER.info("Checking if landed on Google...");
    assertThat(getDriver().getTitle()).isEqualTo("Google").as("Not on Google's page.");
    BasePage googlePage = new BasePage(getDriver());

    LOGGER.info("Clicking on confirm button...");
    googlePage.waitAndClick(CONFIRM_BUTTON);
    LOGGER.info("Test case finished.");
  }
}
