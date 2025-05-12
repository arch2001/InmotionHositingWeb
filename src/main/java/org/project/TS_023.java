package org.project;



import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TS_023 extends BaseClass {

    private WebDriver driver;
    private String baseUrl;
    private String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://www.inmotionhosting.com/";
        driver.manage().window().maximize();

        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_083_testVPSLinkVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebElement vpsLink = driver.findElement(By.linkText("VPS Hosting"));
            Assert.assertTrue(vpsLink.isDisplayed(), "VPS Hosting link is not visible on the page.");
            Point loc = vpsLink.getLocation();
            System.out.println("TC_083 - VPS Hosting link location: " + loc);
        } catch (Exception e) {
            takeScreenshot("TC_083_Failed");
            Assert.fail("TC_083 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_084_testVPSLinkStyling() {
        try {
            WebElement vpsLink = driver.findElement(By.linkText("VPS Hosting"));

            String color      = vpsLink.getCssValue("color");
            String fontSize   = vpsLink.getCssValue("font-size");
            String fontWeight = vpsLink.getCssValue("font-weight");

            System.out.println("TC_055 - color: " + color 
                             + ", font-size: " + fontSize 
                             + ", font-weight: " + fontWeight);

            Assert.assertNotNull(color,      "Color CSS value missing");
            Assert.assertNotNull(fontSize,   "Font-size CSS value missing");
            Assert.assertNotNull(fontWeight, "Font-weight CSS value missing");
        } catch (Exception e) {
            takeScreenshot("TC_084_Failed");
            Assert.fail("TC_084 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_085_testVPSLinkResponsivePresence() {
        try {
            // Simulate mobile viewport (e.g. iPhone X)
            Dimension mobileView = new Dimension(375, 812);
            driver.manage().window().setSize(mobileView);
            driver.navigate().refresh();

            // If there's a hamburger/menu toggle:
            try {
                WebElement menuToggle = driver.findElement(By.cssSelector("button[aria-label='Toggle navigation']"));
                if (menuToggle.isDisplayed()) {
                    menuToggle.click();
                }
            } catch (NoSuchElementException ignored) { }

            WebElement vpsLink = driver.findElement(By.linkText("VPS Hosting"));
            Assert.assertTrue(vpsLink.isDisplayed(), "VPS Hosting link not visible in mobile view");
            System.out.println("TC_085 - VPS Hosting link is visible in responsive/mobile view.");
        } catch (Exception e) {
            takeScreenshot("TC_085_Failed");
            Assert.fail("TC_085 Failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testCaseName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source      = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved for: " + testCaseName);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}
