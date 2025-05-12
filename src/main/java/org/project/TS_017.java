package org.project;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TS_017 extends BaseClass {

    private WebDriver driver;
    private String baseUrl;
    private String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://www.inmotionhosting.com/"; // Or the actual base URL
        driver.manage().window().maximize();
        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    @Test(priority = 1)
    public void tc_067_testCallUsDropdownPlacement() {
        try {
            WebElement callUsLink = driver.findElement(By.xpath("//a[contains(text(),'Call Us')]")); // Corrected XPath
            WebElement header = driver.findElement(By.tagName("header")); // Or a more specific container if needed

            int callUsX = callUsLink.getLocation().getX();
            int headerX = header.getLocation().getX();
            int callUsY = callUsLink.getLocation().getY();
            int headerY = header.getLocation().getY();

            // Check if the "Call Us" link is within the header's bounds
            Assert.assertTrue(callUsX >= headerX, "\"Call Us\" link is not within the header's horizontal bounds.");
            Assert.assertTrue(callUsY >= headerY, "\"Call Us\" link is not within the header's vertical bounds.");
            System.out.println("TC_067 - \"Call Us\" link placement test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_067_Failed");
            Assert.fail("TC_067 Failed: " + e.getMessage());
        }
    }



    @Test(priority = 2)
    public void tc_068_testCallUsDropdownStyling() {
        try {
            WebElement callUsLink = driver.findElement(By.xpath("//a[contains(text(),'Call Us')]"));  // Corrected XPath
            Actions actions = new Actions(driver);
            actions.moveToElement(callUsLink).perform();
            Thread.sleep(1000);
            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'phone-menu')]")); // Changed selector

            // Example: Check background color.  Adjust the CSS property as needed.
            String backgroundColor = dropdown.getCssValue("background-color");
            Assert.assertNotNull(backgroundColor, "Dropdown background color is null.");
            Assert.assertFalse(backgroundColor.isEmpty(), "Dropdown background color is empty.");

            // Example: Check font color of the first phone number.
            WebElement phoneNumberLink = dropdown.findElement(By.xpath(".//a[starts-with(@href, 'tel:')]"));
            String phoneNumberFontColor = phoneNumberLink.getCssValue("color");
            Assert.assertNotNull(phoneNumberFontColor, "Phone number font color is null.");
            Assert.assertFalse(phoneNumberFontColor.isEmpty(), "Phone number font color is empty.");

            System.out.println("TC_068 - \"Call Us\" dropdown styling test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_068_Failed");
            Assert.fail("TC_068 Failed: " + e.getMessage());
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
