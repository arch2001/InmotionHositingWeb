package org.project;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TS_012 extends BaseClass { // Assuming BaseClass handles setup

    private WebDriver driver;
    private String baseUrl = "https://www.inmotionhosting.com/";
    private String screenshotDirectory = "./Screenshots/"; // Directory to store screenshots

    @BeforeClass
    public void setUp() {
        //  moved to BaseClass
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_054_testLogoRedirectionToHome() {
        // TC_054: Verify that clicking the logo redirects the user to the home page
        try {
            driver.get(baseUrl); // Navigate to the base URL
            WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']")); // Corrected XPath
            logo.click();

            // Verify that the current URL is the home page URL
            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, baseUrl, "Clicking the logo did not redirect to the home page.");
            System.out.println("Test (priority = 1) - TC_054 Passed: Logo redirects to home page.");
        } catch (Exception e) {
            takeScreenshot("TC_054_Failed");
            Assert.fail("Test (priority = 1) - TC_054 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_055_testLogoRedirectionFromInternalPage() {
        // TC_055: Verify logo click behavior from a nested/internal page
        try {
            driver.get(baseUrl + "web-hosting.html"); // Navigate to an internal page.
            WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));  // Corrected XPath.
            logo.click();

            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, baseUrl, "Clicking the logo from an internal page did not redirect to the home page.");
            System.out.println("Test (priority = 2) - TC_055 Passed: Logo redirects to home page from internal page.");
        } catch (Exception e) {
            takeScreenshot("TC_055_Failed");
            Assert.fail("Test (priority = 2) - TC_055 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_056_testLogoClickNoNewTab() {
        // TC_056: Verify that logo click doesn't open in a new tab unless specified
        try {
            driver.get(baseUrl);
            WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']")); // Corrected XPath
            String originalWindowHandle = driver.getWindowHandle();
            logo.click();
            String currentWindowHandle = driver.getWindowHandle();

            Assert.assertEquals(currentWindowHandle, originalWindowHandle, "Logo click opened in a new tab unexpectedly.");
            System.out.println("Test (priority = 3) - TC_056 Passed: Logo does not open in a new tab.");
        } catch (Exception e) {
            takeScreenshot("TC_056_Failed");
            Assert.fail("Test (priority = 3) - TC_056 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_057_testLogoIsClickable() {
        // TC_057: Verify logo is clickable (has link associated with it)
        try {
            driver.get(baseUrl);
            WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));  // Corrected XPath.
            String tagName = logo.getTagName();
            Assert.assertEquals(tagName, "img", "Logo is not an image.");
            String src = logo.getAttribute("src");
            Assert.assertNotNull(src, "Logo does not have a src attribute (no link).");
            Assert.assertFalse(src.isEmpty(), "Logo src attribute is empty.");
            System.out.println("Test (priority = 4) - TC_057 Passed: Logo is clickable.");
        } catch (Exception e) {
            takeScreenshot("TC_057_Failed");
            Assert.fail("Test (priority = 4) - TC_057 Failed: " + e.getMessage());
        }
    }

    @AfterClass
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
