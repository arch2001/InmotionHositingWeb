package org.project;



import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TS_024 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final By vpsLinkLocator = By.linkText("VPS Hosting");
    private final String expectedUrlSubstring = "vps-hosting";
    private final String screenshotDirectory = "./screenshots/";

    @BeforeClass
    public void setUp() {   // from BaseClass
        driver.manage().window().maximize();
        driver.get(baseUrl);

        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_086_verifyVPSHostingLinkRedirectsCorrectly() {
        try {
            WebElement vpsLink = driver.findElement(vpsLinkLocator);
            vpsLink.click();

            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains(expectedUrlSubstring),
                    "VPS Hosting link should redirect to correct URL. Found: " + currentUrl);
        } catch (Exception e) {
            takeScreenshot("TC_086_VPS_Redirect");
            Assert.fail("TC_086 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_087_verifyNoErrorsOnVPSHostingPage() {
        try {
            String pageSource = driver.getPageSource();
            Assert.assertFalse(pageSource.contains("404") || pageSource.contains("500"),
                    "VPS Hosting page should not contain HTTP error messages.");
        } catch (Exception e) {
            takeScreenshot("TC_087_VPS_ErrorCheck");
            Assert.fail("TC_087 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_088_verifyVPSHostingOpensInSameTab() {
        try {
            driver.get(baseUrl); // Go back to homepage
            String originalWindow = driver.getWindowHandle();

            WebElement vpsLink = driver.findElement(vpsLinkLocator);
            vpsLink.click();

            Thread.sleep(2000); // allow time for navigation
            String currentWindow = driver.getWindowHandle();

            Assert.assertEquals(originalWindow, currentWindow, "VPS Hosting should open in same tab.");
        } catch (Exception e) {
            takeScreenshot("TC_088_VPS_SameTab");
            Assert.fail("TC_088 Failed: " + e.getMessage());
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
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
