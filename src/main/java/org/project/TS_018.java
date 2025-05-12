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

public class TS_018 extends BaseClass {

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
    public void tc_069_testCallUsDropdownDisplaysNumbers() {
        try {
            driver.get(baseUrl);
            WebElement callUsLink = driver.findElement(By.xpath("//a[contains(text(),'Call Us')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(callUsLink).perform();
            Thread.sleep(1000); // Explicit wait,  use WebDriverWait in production

            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'phone-menu')]"));
            Assert.assertTrue(dropdown.isDisplayed(), "Call Us dropdown is not displayed.");

            WebElement phoneNumberLink = dropdown.findElement(By.xpath(".//a[starts-with(@href, 'tel:')]"));
            Assert.assertNotNull(phoneNumberLink, "No phone number found in the dropdown.");
            System.out.println("TC_069 - \"Call Us\" dropdown displays contact numbers.");
        } catch (Exception e) {
            takeScreenshot("TC_069_Failed");
            Assert.fail("TC_069 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_070_testCallUsNumberClickable() {
        try {
            driver.get(baseUrl);
            WebElement callUsLink = driver.findElement(By.xpath("//a[contains(text(),'Call Us')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(callUsLink).perform();
            Thread.sleep(1000);

            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'phone-menu')]"));
            Assert.assertTrue(dropdown.isDisplayed(), "Call Us dropdown is not displayed.");
            
            //Find the first phone number
            WebElement phoneNumberLink = dropdown.findElement(By.xpath(".//a[starts-with(@href, 'tel:')]"));
            String phoneNumber = phoneNumberLink.getAttribute("href");
            
            Assert.assertTrue(phoneNumber.startsWith("tel:"),"The link doesn't start with tel:");
            System.out.println("Verified that the phone number link starts with 'tel:'.  Manual verification of call initiation is required.");

        } catch (Exception e) {
            takeScreenshot("TC_070_Failed");
            Assert.fail("TC_070 Failed: " + e.getMessage());
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
