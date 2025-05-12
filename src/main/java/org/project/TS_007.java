package org.project;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class TS_007 extends BaseClass {

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
    public void tc_030_verifyLoginButtonVisibility() {
        try {
            WebElement loginBtn = driver.findElement(By.id("submit")); // Replace ID if different
            Assert.assertTrue(loginBtn.isDisplayed(), "Login button is not visible.");
            System.out.println("TC_030 Passed: Login button is visible on the page");
        } catch (Exception e) {
            System.out.println("TC_030 Failed: " + e.getMessage());
            takeScreenshot("TC_030");
            Assert.fail();
        }
    }

    @Test
    public void tc_031_verifyLoginButtonAlignment() {
        try {
            WebElement loginBtn = driver.findElement(By.id("submit"));
            int x = loginBtn.getLocation().getX();
            int y = loginBtn.getLocation().getY();
            System.out.println("Login Button Position - X: " + x + ", Y: " + y);
            Assert.assertTrue(x >= 0 && y >= 0, "Login button alignment seems off.");
            System.out.println("TC_031 Passed: Login button alignment verified");
        } catch (Exception e) {
            System.out.println("TC_031 Failed: " + e.getMessage());
            takeScreenshot("TC_031");
            Assert.fail();
        }
    }

    @Test
    public void tc_032_verifyLoginButtonColorAndStyle() {
        try {
            WebElement loginBtn = driver.findElement(By.id("submit"));
            String bgColor = loginBtn.getCssValue("background-color");
            String font = loginBtn.getCssValue("font-family");
            String border = loginBtn.getCssValue("border");

            System.out.println("Login Button Styles: Background - " + bgColor + ", Font - " + font + ", Border - " + border);

            Assert.assertNotNull(bgColor, "Background color not defined.");
            Assert.assertTrue(font.contains("sans") || font.length() > 0, "Font style may be incorrect.");
            System.out.println("TC_032 Passed: Login button color and style verified");
        } catch (Exception e) {
            System.out.println("TC_032 Failed: " + e.getMessage());
            takeScreenshot("TC_032");
            Assert.fail();
        }
    }

    @Test
    public void tc_033_verifyLoginButtonHoverEffect() {
        try {
            WebElement loginBtn = driver.findElement(By.id("submit"));
            String originalColor = loginBtn.getCssValue("background-color");

            // You may need to use Actions class to perform hover
            Actions actions = new Actions(driver);
            actions.moveToElement(loginBtn).perform();

            Thread.sleep(1000); // Allow hover effect to apply
            String hoverColor = loginBtn.getCssValue("background-color");

            System.out.println("Original Color: " + originalColor + ", Hover Color: " + hoverColor);

            Assert.assertNotEquals(originalColor, hoverColor, "Hover effect not applied.");
            System.out.println("TC_033 Passed: Login button hover effect verified");
        } catch (Exception e) {
            System.out.println("TC_033 Failed: " + e.getMessage());
            takeScreenshot("TC_033");
            Assert.fail();
        }
    }

    @Test
    public void tc_034_verifyLoginButtonLabel() {
        try {
            WebElement loginBtn = driver.findElement(By.id("submit"));
            String btnText = loginBtn.getAttribute("value"); // If it's a <input type="submit">
            System.out.println("Login Button Text: " + btnText);

            Assert.assertTrue(btnText.equalsIgnoreCase("Login") || btnText.contains("Log"),
                    "Login button label incorrect.");
            System.out.println("TC_034 Passed: Login button is properly labeled");
        } catch (Exception e) {
            System.out.println("TC_034 Failed: " + e.getMessage());
            takeScreenshot("TC_034");
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
