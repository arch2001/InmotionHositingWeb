package org.project;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_005 extends BaseClass {

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
    public void tc_020_verifyForgotPasswordLinkRedirect() {
        // TC_020 - Verify the redirection when clicking on "Forgot Password?" link
        try {
        	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Password?")));
            forgotPasswordLink.click();
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("password-reset"), "Forgot Password link did not redirect to the correct page.");
            System.out.println("TC_020 Passed: Forgot Password link redirects to the correct page.");
        } catch (Exception e) {
            System.out.println("TC_020 Failed: " + e.getMessage());
            takeScreenshot("TC_020");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_021_verifyForgotPasswordLinkAlignment() {
        // TC_021 - Verify the alignment of the "Forgot Password?" link
        try {
            WebElement forgotPasswordLink = driver.findElement(By.linkText("Forgot Password?"));
            int x = forgotPasswordLink.getLocation().getX();
            int y = forgotPasswordLink.getLocation().getY();
            System.out.println("Link position - X: " + x + ", Y: " + y);
            Assert.assertTrue(x >= 0 && y >= 0, "Forgot Password link is misaligned.");
            System.out.println("TC_021 Passed: Forgot Password link alignment verified.");
        } catch (Exception e) {
            System.out.println("TC_021 Failed: " + e.getMessage());
            takeScreenshot("TC_021");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_022_verifyForgotPasswordLinkStyling() {
        // TC_022 - Verify the styling (font, color, size) of the "Forgot Password?" link
        try {
            WebElement forgotPasswordLink = driver.findElement(By.linkText("Forgot Password?"));
            String fontSize = forgotPasswordLink.getCssValue("font-size");
            String color = forgotPasswordLink.getCssValue("color");
            String fontFamily = forgotPasswordLink.getCssValue("font-family");

            System.out.println("Font Size: " + fontSize);
            System.out.println("Color: " + color);
            System.out.println("Font Family: " + fontFamily);

            Assert.assertNotNull(fontSize, "Font size should not be null");
            Assert.assertNotNull(color, "Font color should not be null");
            Assert.assertNotNull(fontFamily, "Font family should not be null");
            System.out.println("TC_022 Passed: Forgot Password link styling verified.");
        } catch (Exception e) {
            System.out.println("TC_022 Failed: " + e.getMessage());
            takeScreenshot("TC_022");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_023_verifyForgotPasswordLinkHoverEffect() {
        // TC_023 - Verify the hover effect of the "Forgot Password?" link
        try {
            WebElement forgotPasswordLink = driver.findElement(By.linkText("Forgot Password?"));
            String colorBefore = forgotPasswordLink.getCssValue("color");

            Actions action = new Actions(driver);
            action.moveToElement(forgotPasswordLink).perform();

            String colorAfter = forgotPasswordLink.getCssValue("color");
            System.out.println("Before Hover Color: " + colorBefore);
            System.out.println("After Hover Color: " + colorAfter);

            Assert.assertNotEquals(colorBefore, colorAfter, "Hover color should change.");
            System.out.println("TC_023 Passed: Forgot Password link hover effect verified.");
        } catch (Exception e) {
            System.out.println("TC_023 Failed: " + e.getMessage());
            takeScreenshot("TC_023");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_024_verifyForgotPasswordLinkAccessibility() {
        // TC_024 - Verify the accessibility features of the "Forgot Password?" link
        try {
            WebElement forgotPasswordLink = driver.findElement(By.linkText("Forgot Password?"));

            // Check if the link is focusable using keyboard (TAB)
            forgotPasswordLink.sendKeys(Keys.TAB);
            boolean isFocusable = (driver.switchTo().activeElement().equals(forgotPasswordLink));
            Assert.assertTrue(isFocusable, "Forgot Password link should be keyboard focusable.");

            // Check for accessible name (aria-label or visible text)
            String accessibleName = forgotPasswordLink.getAccessibleName();
            Assert.assertTrue(accessibleName != null && !accessibleName.isEmpty(), "Accessible name should be available.");
            System.out.println("TC_024 Passed: Forgot Password link accessibility verified.");
        } catch (Exception e) {
            System.out.println("TC_024 Failed: " + e.getMessage());
            takeScreenshot("TC_024");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    // Utility method to take a screenshot on test failure
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
