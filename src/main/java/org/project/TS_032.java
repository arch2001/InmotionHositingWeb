package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_032 extends BaseClass {

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
    public void tc_112_verifySupportCenterLinkNavigation() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement supportLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(), 'Support Center')]")));
            String expectedHref = supportLink.getAttribute("href");

            supportLink.click();
            wait.until(ExpectedConditions.urlToBe(expectedHref));

            Assert.assertEquals(driver.getCurrentUrl(), expectedHref, "Support Center did not open in same tab.");
            System.out.println("TC_112 - Support Center link navigated to the correct page.");
        } catch (Exception e) {
            takeScreenshot("TC_112_Failed");
            Assert.fail("TC_112 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_113_verifySupportCenterPageLoadsWithoutError() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            String pageSource = driver.getPageSource().toLowerCase();
            boolean hasError = pageSource.contains("404") || pageSource.contains("not found") || pageSource.contains("error");
            Assert.assertFalse(hasError, "Page contains error text.");

            System.out.println("TC_113 - Support Center page loaded without errors.");
        } catch (Exception e) {
            takeScreenshot("TC_113_Failed");
            Assert.fail("TC_113 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_114_verifySupportLinkOpensInSameTab() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement supportLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(text(), 'Support Center')]")));
            String targetAttr = supportLink.getAttribute("target");

            Assert.assertTrue(targetAttr == null || targetAttr.equals("_self"),
                    "Support Center link should open in the same tab.");
            System.out.println("TC_114 - Link is set to open in the same tab.");
        } catch (Exception e) {
            takeScreenshot("TC_114_Failed");
            Assert.fail("TC_114 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_115_verifyKeyboardAccessibilityToSupportLink() {
        try {
            driver.get(baseUrl);
            WebElement body = driver.findElement(By.tagName("body"));
            boolean found = false;

            for (int i = 0; i < 30; i++) {
                body.sendKeys(Keys.TAB);
                WebElement active = driver.switchTo().activeElement();
                if (active.getText().contains("Support Center")) {
                    found = true;
                    break;
                }
            }

            Assert.assertTrue(found, "Support Center link not reachable via keyboard.");
            System.out.println("TC_115 - Support Center is accessible via keyboard navigation.");
        } catch (Exception e) {
            takeScreenshot("TC_115_Failed");
            Assert.fail("TC_115 Failed: " + e.getMessage());
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
