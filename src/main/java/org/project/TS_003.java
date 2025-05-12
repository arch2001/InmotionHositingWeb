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

public class TS_003 extends BaseClass {

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
    public void tc_010_verifyPasswordTextboxVisibility() {
        try {
        	
       WebElement continueBtn = driver.findElement(By.xpath("//button[@type='submit']"));
             continueBtn.click();
            WebElement passwordBox = driver.findElement(By.id("password"));
            Assert.assertTrue(passwordBox.isDisplayed(), "Password Text Box is not visible.");
            System.out.println("TC_010 Passed: Password Text Box is visible on the page");
        } catch (Exception e) {
            System.out.println("TC_010 Failed: " + e.getMessage());
            TakeScreenshot("TC_010");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_011_verifyPasswordTextboxAlignment() {
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            int x = passwordBox.getLocation().getX();
            int y = passwordBox.getLocation().getY();
            System.out.println("Textbox position - X: " + x + ", Y: " + y);
            Assert.assertTrue(x >= 0 && y >= 0, "Password Text Box is misaligned.");
            System.out.println("TC_011 Passed: Password Text Box alignment verified");
        } catch (Exception e) {
            System.out.println("TC_011 Failed: " + e.getMessage());
            TakeScreenshot("TC_011");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_012_verifyPasswordMasking() {
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            String type = passwordBox.getAttribute("type");
            Assert.assertEquals(type, "password", "Password characters are not masked.");
            System.out.println("TC_012Passed: Password field masks characters properly");
        } catch (Exception e) {
            System.out.println("TC_012 Failed: " + e.getMessage());
            TakeScreenshot("TC_012");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    @Test
    public void tc_013_verifyShowPasswordToggle() {
        try {
            // Locate the password input field and the toggle button
            WebElement passwordBox = driver.findElement(By.id("password")); // Replace with actual password field ID
            WebElement toggleButton = driver.findElement(By.id("toggle-password")); // Replace with actual toggle button ID

            // First, check if the password is hidden by default
            String initialType = passwordBox.getAttribute("type");
            Assert.assertEquals(initialType, "password", "Password input is not initially hidden.");

            // Click the toggle to show the password
            toggleButton.click();
            
            // Verify that the password input type changes to "text"
            String typeAfterClick = passwordBox.getAttribute("type");
            Assert.assertEquals(typeAfterClick, "text", "Show password toggle did not work.");

            // Click the toggle again to hide the password
            toggleButton.click();

            // Verify that the password input type changes back to "password"
            String typeAfterSecondClick = passwordBox.getAttribute("type");
            Assert.assertEquals(typeAfterSecondClick, "password", "Hide password toggle did not work.");

            System.out.println("TC_013 Passed: Show/Hide toggle is functional");
        } catch (Exception e) {
            System.out.println("TC_013 Failed: " + e.getMessage());
            TakeScreenshot("TC_013");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    @Test
    public void tc_014_verifySpecialCharactersAccepted() {
        try {
            WebElement passwordBox = driver.findElement(By.id("password"));
            String testInput = "P@$$w0rd#2025";
            passwordBox.clear();
            passwordBox.sendKeys(testInput);
            Assert.assertEquals(passwordBox.getAttribute("value"), testInput, "Special characters were not accepted.");
            System.out.println("TC_014 Passed: Password field accepts special characters");
        } catch (Exception e) {
            System.out.println("TC_014 Failed: " + e.getMessage());
            TakeScreenshot("TC_014");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    private void TakeScreenshot(String testCaseName) {
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
