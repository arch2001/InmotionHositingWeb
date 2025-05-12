package org.project;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_016 extends BaseClass {

    private WebDriver driver;
    private String websiteURL = "https://www.inmotionhosting.com/"; // Replace with the actual URL
    private String screenshotDirectory = "./Screenshots/";

    // Locators for the elements on the page
    private By chatNowLinkLocator = By.linkText("Chat Now"); //  Locator for the "Chat Now" link.  Correct this if needed.
    private By chatWindowLocator = By.xpath("//div[@class='chat-window']"); // Example locator,  Correct this.
    private By closeButtonLocator = By.xpath("//button[contains(text(), 'Close')]"); // Example, Correct this.

    @BeforeMethod
    public void setUp() {
        // Set up ChromeDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); //  Replace with your chromedriver path
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(websiteURL);
        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_065_testChatNowLinkAfterRefresh() {
        // TC_054: Verify that the "Chat Now" link works after a page refresh
        try {
            driver.get(websiteURL);
            WebElement chatNowLink = driver.findElement(chatNowLinkLocator);
            chatNowLink.click();
            takeScreenshot("testChatNowLinkAfterRefresh_step1"); //take screenshot

            driver.navigate().refresh();
            takeScreenshot("testChatNowLinkAfterRefresh_step2"); //take screenshot

            chatNowLink = driver.findElement(chatNowLinkLocator);  // Re-find the element after refresh
            chatNowLink.click();
            takeScreenshot("testChatNowLinkAfterRefresh_step3"); //take screenshot

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement chatWindow = wait.until(ExpectedConditions.visibilityOfElementLocated(chatWindowLocator));
            Assert.assertTrue(chatWindow.isDisplayed(), "Chat window did not appear after refresh and re-click.");
            System.out.println("Test Case 1 Passed: Chat Now link works after refresh");
        } catch (Exception e) {
            takeScreenshot("testChatNowLinkAfterRefresh_failed"); //take screenshot
            Assert.fail("Test Case 1 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_066_testCloseChatWindowButton() {
        // TC_055: Verify if the chat window closes when the "Close" button is clicked
        try {
            driver.get(websiteURL);
            WebElement chatNowLink = driver.findElement(chatNowLinkLocator);
            chatNowLink.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(closeButtonLocator));
            takeScreenshot("testCloseChatWindowButton_step1"); // take screenshot
            closeButton.click();
            takeScreenshot("testCloseChatWindowButton_step2"); // take screenshot

            wait.until(ExpectedConditions.invisibilityOfElementLocated(chatWindowLocator));
            System.out.println("Test Case 2 Passed: Chat window closed after clicking Close button.");
        } catch (Exception e) {
            takeScreenshot("testCloseChatWindowButton_failed");  // take screenshot
            Assert.fail("Test Case 2 Failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testCaseName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(screenshotDirectory + testCaseName + ".png");
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken for: " + testCaseName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
}
