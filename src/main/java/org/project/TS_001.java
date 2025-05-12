package org.project;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

public class TS_001 extends BaseClass {

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
    public void tc_001_verifyEmailTextboxVisibility() {
        // TC_001 - Verify the Email Address Text Box is visible
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            Assert.assertTrue(emailBox.isDisplayed(), "Email Text Box is not visible.");
            System.out.println("TC_001 Passed: Email Text Box is visible on the page");
        } catch (Exception e) {
            System.out.println("TC_001 Failed: " + e.getMessage());
            takeScreenshot("TC_001");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_002_verifyEmailTextboxAlignment() {
        // TC_002 - Verify the alignment of the Email Address Text Box
        try {
            WebElement emailBox = driver.findElement(By.id("email"));
            int x = emailBox.getLocation().getX();
            int y = emailBox.getLocation().getY();
            System.out.println("Textbox position - X: " + x + ", Y: " + y);
            Assert.assertTrue(x >= 0 && y >= 0, "Email Text Box is misaligned.");
            System.out.println("TC_002 Passed: Email Text Box alignment verified");
        } catch (Exception e) {
            System.out.println("TC_002 Failed: " + e.getMessage());
            takeScreenshot("TC_002");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    @Test
    public void tc_003_verifyAlphanumericInput() {
        // TC_003 - Verify input of alphanumeric values
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            String testInput = "archanabangera256@";
            emailBox.sendKeys(testInput);
            Assert.assertEquals(emailBox.getAttribute("value"), testInput);
            System.out.println("TC_003 Passed: Alphanumeric input accepted");
        } catch (Exception e) {
            System.out.println("TC_003 Failed: " + e.getMessage());
            takeScreenshot("TC_003");
            Assert.fail();
        }
    }


    @Test
    public void tc_004_verifySpecialCharactersInput() {
        // TC_004 - To Verify the Email Address text box accepts Special Characters like @, $
        try {
            WebElement emailBox = driver.findElement(By.id("username"));
            String testInput = "archana@$.com";
            emailBox.clear();
            emailBox.sendKeys(testInput);
            String actualValue = emailBox.getAttribute("value");
            Assert.assertEquals(actualValue, testInput);
            System.out.println("TC_004 Passed: Special characters @ and $ accepted in email input");
        } catch (Exception e) {
            System.out.println("TC_004 Failed: " + e.getMessage());
            takeScreenshot("TC_004");
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