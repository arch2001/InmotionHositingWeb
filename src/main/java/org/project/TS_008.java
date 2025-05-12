package org.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TS_008 extends BaseClass {

    @BeforeClass
    public static void browserOpen() {
        System.out.println("BeforeClass: Launching browser");
        Date d = new Date();
        System.out.println("Test started at: " + d);
    }

    @BeforeMethod
    public void beforeTestCase() {
        launchBrowser();
        windowMaximize();
        launchUrl("https://secure1.inmotionhosting.com/index/login");
    }

    @Test
    public void tc_035_verifyBlankCredentialsLogin() {
        try {
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();
            WebElement errorMsg = driver.findElement(By.xpath("//div[@class='alert alert-danger']")); // Updated error message locator
            Assert.assertTrue(errorMsg.isDisplayed(), "Error message should be displayed for blank login.");
            System.out.println("TC_035 Passed: Login failed with blank credentials");
        } catch (Exception e) {
            System.out.println("TC_035 Failed: " + e.getMessage());
            takeScreenshot("TC_035");
            Assert.fail("TC_035 Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_036_verifyInvalidCredentialsLogin() {
        try {
            driver.findElement(By.id("username")).sendKeys("invalid@example.com");
            driver.findElement(By.id("password")).sendKeys("wrongpass");
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();
            WebElement errorMsg = driver.findElement(By.xpath("//div[@class='alert alert-danger']")); // Updated error message locator
            Assert.assertTrue(errorMsg.isDisplayed(), "Error message should be displayed for invalid credentials.");
            System.out.println("TC_036 Passed: Login failed with invalid credentials");
        } catch (Exception e) {
            System.out.println("TC_036 Failed: " + e.getMessage());
            takeScreenshot("TC_036");
            Assert.fail("TC_036 Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_037_verifyValidCredentialsLogin() {
        try {
            driver.findElement(By.id("username")).sendKeys("validuser@example.com");
            driver.findElement(By.id("password")).sendKeys("validpassword");
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();
            Thread.sleep(3000); // Consider using WebDriverWait
            Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "Login with valid credentials should redirect to a URL containing 'dashboard'. Current URL: " + driver.getCurrentUrl());
            System.out.println("TC_037 Passed: Login successful with valid credentials");
        } catch (Exception e) {
            System.out.println("TC_037 Failed: " + e.getMessage());
            takeScreenshot("TC_037");
            Assert.fail("TC_037 Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_038_verifyButtonClickability() {
        try {
            WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]"));
            boolean isInitiallyDisabled = !loginBtn.isEnabled();

            driver.findElement(By.id("username")).sendKeys("test@example.com");
            driver.findElement(By.id("password")).sendKeys("password");

            boolean isEnabledAfterInput = loginBtn.isEnabled();

            Assert.assertTrue(isInitiallyDisabled && isEnabledAfterInput, "Login button should be initially disabled and enabled after filling credentials.");
            System.out.println("TC_038 Passed: Login button enabled only after both fields are filled");
        } catch (Exception e) {
            System.out.println("TC_038 Failed: " + e.getMessage());
            takeScreenshot("TC_038");
            Assert.fail("TC_038 Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_039_verifySessionAfterRedirect() {
        try {
            driver.findElement(By.id("username")).sendKeys("validuser@example.com");
            driver.findElement(By.id("password")).sendKeys("validpassword");
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();
            Thread.sleep(3000); // Consider using WebDriverWait
            driver.navigate().to("https://secure1.inmotionhosting.com/account/dashboard");
            Thread.sleep(2000); // Added a small wait after navigation
            driver.navigate().back();
            Thread.sleep(2000); // Added a small wait after navigating back
            // **UPDATE THIS LOCATOR TO FIND AN ELEMENT THAT CONFIRMS THE USER IS STILL LOGGED IN ON THE DASHBOARD**
            WebElement userInfo = driver.findElement(By.xpath("//h1[contains(text(), 'Dashboard')]")); // Example - update based on actual element
            Assert.assertTrue(userInfo.isDisplayed(), "User info should be displayed after redirection and navigating back, indicating the session is maintained.");
            System.out.println("TC_039 Passed: Session maintained after redirection");
        } catch (Exception e) {
            System.out.println("TC_039 Failed: " + e.getMessage());
            takeScreenshot("TC_039");
            Assert.fail("TC_039 Failed: " + e.getMessage());
        }
    }

    private void takeScreenshot(String testCaseName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File("./Screenshots/" + testCaseName + ".png");
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken for: " + testCaseName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterTestCase() {
        closeEntireBrowser();
    }
}