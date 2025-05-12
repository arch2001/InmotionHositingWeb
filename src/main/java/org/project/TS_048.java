package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class TS_048 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        // If using WebDriverManager, include setup line:
        // WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) dir.mkdirs();
    }

    @Test(priority = 1)
    public void tc_174_verifyFooterLinksRedirection() {
        try {
            driver.get(baseUrl);

            // Scroll to bottom to load footer
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000); // Wait for any lazy-loaded footer content

            // Find footer links using a broader XPath
            List<WebElement> footerLinks = driver.findElements(By.xpath("//a[contains(@href, '') and ancestor::*[contains(@class, 'footer') or local-name()='footer']]"));
            Assert.assertTrue(footerLinks.size() > 0, "No links found in the footer.");

            WebElement firstLink = footerLinks.get(0);
            String expectedUrl = firstLink.getAttribute("href");
            firstLink.click();

            // Wait for redirection
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlToBe(expectedUrl));

            Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Redirection failed for footer link.");
            System.out.println("TC_174 - Footer link redirection works.");
        } catch (Exception e) {
            takeScreenshot("TC_174_Failed");
            Assert.fail("TC_174 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 2)
    public void tc_175_verifyContactNumbersClick() {
        try {
            driver.get(baseUrl);

            // Scroll to footer to ensure it's loaded
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000); // Allow time for lazy-loaded content

            // Use flexible XPath to locate tel: links inside the footer
            List<WebElement> contactNumbers = driver.findElements(By.xpath("//a[contains(@href, 'tel:') and (ancestor::*[contains(@class, 'footer')] or ancestor::footer)]"));
            Assert.assertTrue(contactNumbers.size() > 0, "No contact numbers found in the footer.");

            WebElement contactNumber = contactNumbers.get(0);
            String hrefValue = contactNumber.getAttribute("href");

            Assert.assertTrue(hrefValue.startsWith("tel:"), "Link does not start with tel: protocol.");
            System.out.println("TC_175 - Contact number found with href: " + hrefValue);

            // Note: Clicking the link won't navigate the browser; simulate that it is clickable
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", contactNumber);

            // Optional: you may check that the href value is valid, not blank
            Assert.assertFalse(hrefValue.trim().equals("tel:"), "Contact number href is empty.");

            System.out.println("TC_175 - Contact number click simulation passed.");
        } catch (Exception e) {
            takeScreenshot("TC_175_Failed");
            Assert.fail("TC_175 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 3)
    public void tc_176_verifyCookieSettingsLink() {
        try {
            driver.get(baseUrl);

            // Scroll to footer to ensure all links are loaded
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000); // Allow time for dynamic content to load

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Locate the 'Cookie Settings' link using a robust XPath
            WebElement cookieSettingsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//footer//a[contains(text(), 'Cookie Settings')]")));

            Assert.assertTrue(cookieSettingsLink.isDisplayed(), "Cookie Settings link is not visible.");

            // Use JS click to avoid interception by overlays or sticky headers
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cookieSettingsLink);

            // Wait for either a URL change or a cookie modal/dialog
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("cookie"),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='cookie'], div[id*='cookie']"))
            ));

            // Final validation to ensure one of the conditions is satisfied
            boolean redirected = driver.getCurrentUrl().toLowerCase().contains("cookie");
            boolean modalDisplayed = driver.findElements(By.cssSelector("div[class*='cookie'], div[id*='cookie']")).size() > 0;

            Assert.assertTrue(redirected || modalDisplayed, "No redirection or modal for Cookie Settings link.");

            System.out.println("TC_176 - Cookie Settings link functionality works.");
        } catch (Exception e) {
            takeScreenshot("TC_176_Failed");
            Assert.fail("TC_176 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_177_verifyAccessibilitySettingsLink() {
        try {
            driver.get(baseUrl);

            // Scroll to the bottom to ensure the footer loads
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000); // Wait for dynamic content to load

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Locate the 'Accessibility Settings' link using robust XPath under footer
            WebElement accessibilitySettingsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//footer//a[contains(text(), 'Accessibility Settings')]")));

            Assert.assertTrue(accessibilitySettingsLink.isDisplayed(), "Accessibility Settings link is not visible.");

            // Use JS click in case of overlays or hidden links
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accessibilitySettingsLink);

            // Wait for either redirection or accessibility modal
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("accessibility"),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='accessibility'], div[id*='accessibility']"))
            ));

            boolean redirected = driver.getCurrentUrl().toLowerCase().contains("accessibility");
            boolean modalDisplayed = driver.findElements(By.cssSelector("div[class*='accessibility'], div[id*='accessibility']")).size() > 0;

            Assert.assertTrue(redirected || modalDisplayed, "No redirection or modal for Accessibility Settings link.");

            System.out.println("TC_177 - Accessibility Settings link functionality works.");
        } catch (Exception e) {
            takeScreenshot("TC_177_Failed");
            Assert.fail("TC_177 Failed: " + e.getMessage());
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
            FileHandler.copy(src, dest);
            System.out.println("Screenshot saved for: " + testCaseName);
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
