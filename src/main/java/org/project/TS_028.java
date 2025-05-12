package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class TS_028 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) dir.mkdirs();
    }

    @Test(priority = 1)
    public void tc_098_verifyDomainsLinkRedirection() {
        try {
            driver.get(baseUrl);
            String originalWindow = driver.getWindowHandle();

            WebElement domainsLink = driver.findElement(By.linkText("Domains"));
            domainsLink.click();

            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("domain") || currentUrl.contains("domains"),
                    "Redirection failed. Current URL: " + currentUrl);

            System.out.println("TC_098 - Domains link redirected correctly to: " + currentUrl);
        } catch (Exception e) {
            takeScreenshot("TC_098_Failed");
            Assert.fail("TC_098 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_099_verifyDomainsPageLoadsWithoutError() {
        try {
            // Assuming redirected from previous test
            String pageSource = driver.getPageSource().toLowerCase();
            Assert.assertFalse(pageSource.contains("404") || pageSource.contains("not found") || pageSource.contains("error"),
                    "Domains page loaded with an error.");
            Thread.sleep(3000);

            System.out.println("TC_099 - Domains page loaded successfully without error.");
        } catch (Exception e) {
            takeScreenshot("TC_099_Failed");
            Assert.fail("TC_099 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 3)
    public void tc_100_verifyDomainsLinkOpensInSameTab() {
        try {
            driver.get(baseUrl);
            String originalWindow = driver.getWindowHandle();
            Set<String> windowsBefore = driver.getWindowHandles();

            WebElement domainsLink = driver.findElement(By.linkText("Domains"));
            domainsLink.click();

            Thread.sleep(3000);

            Set<String> windowsAfter = driver.getWindowHandles();
            Assert.assertEquals(windowsBefore, windowsAfter, "Domains link opened in a new tab.");

            System.out.println("TC_100 - Domains link opened in the same tab.");
        } catch (Exception e) {
            takeScreenshot("TC_100_Failed");
            Assert.fail("TC_100 Failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void takeScreenshot(String testCaseName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved for: " + testCaseName);
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
