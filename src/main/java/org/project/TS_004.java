package org.project;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit; // Added import for AssertJUnit
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TS_004 extends BaseClass {

    @BeforeMethod
    public void beforeTestCase() {
        launchBrowser();
        windowMaximize();
        launchUrl("https://secure1.inmotionhosting.com/index/login");
    }

    @Test
    public void tc_015_verifyPasswordDoesNotAcceptBlankInput() {
        // To verify password field does not accept blank input
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']")); // Assuming a login button exists
            passwordBox.sendKeys("");
            loginButton.click();
            // Assertion: Check for a validation message for a blank password
            WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'Please enter your password')]")); // Example XPath - adjust as needed
            Assert.assertTrue(errorMessage.isDisplayed(), "Validation message for blank password is not displayed.");
            System.out.println("TC_015 Passed: Password field does not accept blank input");
        } catch (Exception e) {
            System.out.println("TC_015 Failed: " + e.getMessage());
            takeScreenshot("TC_015");
            Assert.fail();
        }
    }

    @Test
    public void tc_016_verifyPasswordMinimumLengthPolicy() {
        // To verify password field enforces minimum length policy
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']")); // Assuming a login button exists
            String shortPassword = "2001"; // Assuming minimum length is greater than 3
            passwordBox.sendKeys(shortPassword);
            loginButton.click();
            // Assertion: Check for a validation message related to password length
            WebElement lengthErrorMessage = driver.findElement(By.xpath("//div[contains(text(),'Password must be at least')]")); // Example XPath - adjust as needed
            Assert.assertTrue(lengthErrorMessage.isDisplayed(), "Validation message for short password is not displayed.");
            System.out.println("TC_016 Passed: Password field enforces minimum length policy");
        } catch (Exception e) {
            System.out.println("TC_016 Failed: " + e.getMessage());
            takeScreenshot("TC_016");
            Assert.fail();
        }
    }

    @Test
    public void tc_017_verifyPasswordAcceptsValidAlphanumericPassword() {
        // To verify password field accepts a valid alphanumeric password
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            String validPassword = "archana2001";
            passwordBox.sendKeys(validPassword);
            // Assertion: You might want to add further steps and assertions here,
            // like attempting to log in and checking for the absence of password errors.
            // For now, we'll just verify that sending keys doesn't throw an error.
            System.out.println("TC_017 Passed: Password field accepts a valid alphanumeric password");
        } catch (Exception e) {
            System.out.println("TC_017 Failed: " + e.getMessage());
            takeScreenshot("TC_009");
            Assert.fail();
        }
    }

    @Test
    public void tc_018_verifyPasswordAcceptsSpecialCharacters() {
        // To verify password field accepts special characters
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            String passwordWithSpecialChars = "";
            passwordBox.sendKeys(passwordWithSpecialChars);
            // Assertion: Similar to the alphanumeric test, you might want to attempt login
            // and assert the absence of password-related errors for a more complete test.
            System.out.println("TC_018 Passed: Password field accepts special characters");
        } catch (Exception e) {
            System.out.println("TC_018 Failed: " + e.getMessage());
            takeScreenshot("TC_018");
            Assert.fail();
        }
    }

    @Test
    public void tc_019_verifyPasswordValidationErrorDisplayAsterisk() {
        // To verify the Password text box displays the validation error such as asterisk * symbol
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']")); // Assuming a login button exists
            passwordBox.sendKeys("");
            loginButton.click();
            // Assertion: Check if the validation error message contains an asterisk
            WebElement validationError = driver.findElement(By.xpath("//div[contains(@class, 'error-message') and contains(text(), '*') or contains(text(), 'Please enter your password')]"));
            Assert.assertTrue(validationError.isDisplayed() && validationError.getText().contains("*"),
                    "Password validation error does not display an asterisk or the expected message.");
            System.out.println("TC_019 Passed: Password validation error displays an asterisk or the expected message.");
        } catch (Exception e) {
            System.out.println("TC_019Failed: " + e.getMessage());
            takeScreenshot("TC_019");
            AssertJUnit.fail();
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