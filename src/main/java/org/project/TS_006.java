package org.project;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;


public class TS_006 extends BaseClass {

	 @BeforeClass
	    public static void browserOpen() {
	        System.out.println("BeforeClass: Launching browser");
	    }

	    @BeforeMethod
	    public void beforeTestCase() {
	        launchBrowser();
	        windowMaximize();
	        launchUrl("https://secure1.inmotionhosting.com/index/login");
	    }

    @Test
    public void tc_025_verifyForgetPasswordLink() {
        try {
            WebElement forgetLink = driver.findElement(By.linkText("Forget Password?"));
            forgetLink.click();
            String url = driver.getCurrentUrl();
            Assert.assertTrue(url.contains("reset"));
            takeScreenshot("tc_025_pass");
            System.out.println("Test Case Passed: Forget Password link redirects to reset page.");
        } catch (Exception e) {
            takeScreenshot("tc_025_fail");
            System.out.println("Test Case Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_026_verifyEmailFieldPresent() {
        try {
        	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
             WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
            Assert.assertTrue(emailField.isDisplayed());
            takeScreenshot("tc_026_pass");
            System.out.println("Test Case Passed: Email field is present on reset password page.");
        } catch (Exception e) {
            takeScreenshot("tc_026_fail");
            System.out.println("Test Case Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_027_blankEmailError() {
        try {
            WebElement emailField = driver.findElement(By.name("email"));
            emailField.clear();
            driver.findElement(By.xpath("//button[contains(text(), 'Reset')]")).click();
            WebElement error = driver.findElement(By.className("error"));
            Assert.assertTrue(error.isDisplayed());
            takeScreenshot("tc_027_pass");
            System.out.println("Test Case Passed: Error message displayed for blank email.");
        } catch (Exception e) {
            takeScreenshot("tc_027_fail");
            System.out.println("Test Case Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_028_invalidEmailError() {
        try {
            WebElement emailField = driver.findElement(By.name("email"));
            emailField.clear();
            emailField.sendKeys("fakeemail@example.com");
            driver.findElement(By.xpath("//button[contains(text(), 'Reset')]")).click();
            WebElement error = driver.findElement(By.className("error"));
            Assert.assertTrue(error.isDisplayed());
            takeScreenshot("tc_028_pass");
            System.out.println("Test Case Passed: Error message displayed for unregistered email.");
        } catch (Exception e) {
            takeScreenshot("tc_028_fail");
            System.out.println("Test Case Failed: " + e.getMessage());
        }
    }

    @Test
    public void tc_029_validEmailReset() {
        try {
            WebElement emailField = driver.findElement(By.name("email"));
            emailField.clear();
            emailField.sendKeys("your_registered_email@example.com"); // Replace with actual test email
            driver.findElement(By.xpath("//button[contains(text(), 'Reset')]")).click();
            WebElement success = driver.findElement(By.className("success"));
            Assert.assertTrue(success.isDisplayed());
            takeScreenshot("tc_029_pass");
            System.out.println("Test Case Passed: Reset email sent successfully.");
        } catch (Exception e) {
            takeScreenshot("tc_029_fail");
            System.out.println("Test Case Failed: " + e.getMessage());
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

    @AfterClass
    public void closeBrowser() {
        driver.quit();
    }
}
