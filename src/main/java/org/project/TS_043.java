package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_043 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) dir.mkdirs();
    }

    @Test(priority = 1)
    public void tc_155_verifyChatOurStoryVisibilityAndStyle() {
        try {
            driver.get(baseUrl);
            // Increase wait time to 20 seconds
            WebElement chatLink = new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), normalize-space('Chat Our Story'))]")));

            Assert.assertTrue(chatLink.isDisplayed(), "'Chat Our Story' link is not visible.");

            String color = chatLink.getCssValue("color");
            String fontSize = chatLink.getCssValue("font-size");

            Assert.assertNotNull(color, "Color not set.");
            Assert.assertTrue(fontSize.endsWith("px"), "Font size format is incorrect.");

            System.out.println("TC_155 - Link visible with color: " + color + " and font size: " + fontSize);
        } catch (Exception e) {
            takeScreenshot("TC_155_Failed");
            Assert.fail("TC_155 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_156_verifyChatOurStoryRedirection() {
        try {
            driver.get(baseUrl);
            WebElement chatLink = new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), normalize-space('Chat Our Story'))]")));

            String currentUrl = driver.getCurrentUrl();
            chatLink.click();

            new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

            String newUrl = driver.getCurrentUrl();
            Assert.assertNotEquals(newUrl, currentUrl, "Redirection failed for 'Chat Our Story'.");

            System.out.println("TC_156 - Redirected to: " + newUrl);
        } catch (Exception e) {
            takeScreenshot("TC_156_Failed");
            Assert.fail("TC_156 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_157_verifyChatOurStoryOnMobile() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile screen
            driver.get(baseUrl);
            Thread.sleep(2000); // Give page time to load and adjust

            WebElement chatLink = new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), normalize-space('Chat Our Story'))]")));

            Assert.assertTrue(chatLink.isDisplayed(), "'Chat Our Story' not visible on mobile.");
            System.out.println("TC_157 - Link is visible on mobile view.");
        } catch (Exception e) {
            takeScreenshot("TC_157_Failed");
            Assert.fail("TC_157 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
        }
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void takeScreenshot(String testCaseName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved for: " + testCaseName);
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
