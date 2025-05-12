package org.project;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.asserts.Assertion;
import java.time.Duration;

public class TS_002 extends BaseClass {

    @BeforeClass
    public static void browserOpen() {
        System.out.println("BeforeClass: Launching browser for TS_002");
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
    public void tc_005_invalidEmailFormat() {
        // TC_005 - Verify Email field does not accept invalid formats (missing @)
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            emailBox.clear();
            emailBox.sendKeys("archanabangera256.com");
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();

            WebElement errorMsg = driver.findElement(By.id("username-error"));
            Assert.assertTrue(errorMsg.isDisplayed(), "Validation error not displayed for invalid email");
            System.out.println("TC_005 Passed: Invalid email format correctly rejected");
        } catch (Exception e) {
            System.out.println("TC_005 Failed: " + e.getMessage());
            takeScreenshot("TC_005");
            Assert.fail();
        }
    }

    @Test
    public void tc_006_emailMandatoryValidation() {
        // TC_006 - Verify email field shows validation error and asterisk (*)
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // **INSPECT THE ELEMENT AND UPDATE THIS XPATH IF NECESSARY**
            WebElement emailLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@for='username']")));
            Assert.assertTrue(emailLabel.getText().contains("*"), "Asterisk not displayed for mandatory email field");

            WebElement emailBox = driver.findElement(By.id("username"));
            emailBox.clear();
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();

            WebElement errorMsg = driver.findElement(By.id("username-error"));
            Assert.assertTrue(errorMsg.isDisplayed(), "Validation message not displayed for empty email field");

            System.out.println("TC_006 Passed: Asterisk and validation error displayed correctly");
        } catch (Exception e) {
            System.out.println("TC_006 Failed: " + e.getMessage());
            takeScreenshot("TC_006");
            Assert.fail();
        }
    }

    @Test
    public void tc_007_validEmailAccepted() {
        // TC_007 - Verify valid email input is accepted
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            String validEmail = "archana123@example.com";
            emailBox.clear();
            emailBox.sendKeys(validEmail);
            driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();

            Assert.assertEquals(emailBox.getAttribute("value"), validEmail);
            System.out.println("TC_007 Passed: Valid email accepted");
        } catch (Exception e) {
            System.out.println("TC_007 Failed: " + e.getMessage());
            takeScreenshot("TC_007");
            Assert.fail();
        }
    }

    @Test
    public void tc_008_maxLengthEmailField() {
        // TC_008 - Verify max length allowed in email field
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            String longEmail = "archana".repeat(40) + "@example.com"; // Very long email
            emailBox.clear();
            emailBox.sendKeys(longEmail);

            int enteredLength = emailBox.getAttribute("value").length();
            Assert.assertTrue(enteredLength <= 254, "Email length exceeded 254 characters");
            System.out.println("TC_008 Passed: Max length validated successfully");
        } catch (Exception e) {
            System.out.println("TC_008 Failed: " + e.getMessage());
            takeScreenshot("TC_008");
            Assert.fail();
        }
    }

    @Test
    public void tc_009_emailDomainFormats() {
        // TC_009- Verify email field accepts .in and .org domains
        try {
            String[] validDomains = {"user@example.in", "admin@ngo.org"};

            for (String email : validDomains) {
                WebElement emailBox = driver.findElement(By.id("username"));
                emailBox.clear();
                emailBox.sendKeys(email);
                driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Log in')]")).click();

                Assert.assertEquals(emailBox.getAttribute("value"), email);
                System.out.println("TC_009 Passed: Domain accepted - " + email);
            }
        } catch (Exception e) {
            System.out.println("TC_009 Failed: " + e.getMessage());
            takeScreenshot("TC_009");
            Assert.fail();
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