package org.project;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class TS_010 extends BaseClass {

    private By needHelpLinkLocator = By.id("needHelpLink");
    private String loginPageUrl = "https://secure1.inmotionhosting.com/index/login";
    private String expectedHelpPageTitle = "Support Center";

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
        launchUrl(loginPageUrl);
    }

    @Test(priority = 1)
    public void tc_045_verifyLinkRedirection() {
        // TC_045: Verify that the link redirects the user to the support/help page
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            needHelpLink.click();

            // Switch to the new tab (if it opens in a new tab)
            String originalWindowHandle = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            // Get the actual title of the help page
            String actualHelpPageTitle = driver.getTitle();
            Assert.assertTrue(actualHelpPageTitle.contains(expectedHelpPageTitle),
                    "Link did not redirect to the expected support/help page. Expected title to contain: " + expectedHelpPageTitle + ", but got: " + actualHelpPageTitle);
            System.out.println("Test (priority = 1) - TC_045 Passed: Link redirects to the support/help page");

            driver.switchTo().window(originalWindowHandle); //switch back
        } catch (Exception e) {
            System.out.println("Test (priority = 1) - TC_045 Failed: " + e.getMessage());
            takeScreenshot("TC_045");
            Assert.fail("Test (priority = 1) - TC_045 Failed: Exception occurred - " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_046_verifySupportInformation() {
        // TC_046: To verify the correct support information is displayed
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            needHelpLink.click();

            // Switch to the new tab (if it opens in a new tab)
            String originalWindowHandle = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            // Verify some element that confirms correct support info.  *Correct this!*
            WebElement supportInfoElement = driver.findElement(By.id("support-header"));
            Assert.assertTrue(supportInfoElement.isDisplayed(), "Support information is not displayed correctly.");
            System.out.println("Test (priority = 2) - TC_046 Passed: Correct support information is displayed");
            driver.switchTo().window(originalWindowHandle); //switch back

        } catch (Exception e) {
            System.out.println("Test (priority = 2) - TC_046 Failed: " + e.getMessage());
            takeScreenshot("TC_046");
            Assert.fail("Test (priority = 2) - TC_046 Failed: Exception occurred - " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_047_verifyTabBehavior() {
        // TC_047: To verify external page opens in the same or new tab based on design
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            String originalWindowHandle = driver.getWindowHandle();
            needHelpLink.click();

            // Get the number of open tabs/windows
            int numberOfOpenWindows = driver.getWindowHandles().size();

            if (numberOfOpenWindows > 1) {
                System.out.println("Test (priority = 3) - TC_047 Passed: External page opened in a new tab");
            } else {
                System.out.println("Test (priority = 3) - TC_047 Passed: External page opened in the same tab");
            }
            driver.switchTo().window(originalWindowHandle);

        } catch (Exception e) {
            System.out.println("Test (priority = 3) - TC_047 Failed: " + e.getMessage());
            takeScreenshot("TC_047");
            Assert.fail("Test (priority = 3) - TC_047 Failed: Exception occurred - " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_048_verifyBackNavigation() {
        // TC_048: To verify back navigation after visiting help page
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            needHelpLink.click();

            // Switch to the new tab (if it opens in a new tab)
            String originalWindowHandle = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            driver.navigate().back();
            Thread.sleep(2000);

            // Verify that we are back on the login page.
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains(loginPageUrl), "Back navigation failed.  Expected URL to contain: " + loginPageUrl + " but got " + currentUrl);
            System.out.println("Test (priority = 4) - TC_048 Passed: Back navigation successful");
            driver.switchTo().window(originalWindowHandle); //switch back

        } catch (Exception e) {
            System.out.println("Test (priority = 4) - TC_048 Failed: " + e.getMessage());
            takeScreenshot("TC_048");
            Assert.fail("Test (priority = 4) - TC_048 Failed: Exception occurred - " + e.getMessage());
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
