package org.project;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import org.apache.commons.io.FileUtils;

public class TS_020 extends BaseClass {

    private WebDriver driver;
    private String baseUrl;
    private String screenshotDirectory = "./Screenshots/";
    private WebDriverWait wait; // Declare WebDriverWait

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://www.inmotionhosting.com/"; // Or the actual base URL
        driver.manage().window().maximize();
        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Initialize WebDriverWait
    }

    @Test(priority = 1)
    public void tc_074_testWebHostingDropdownVisibility() {
        try {
            driver.get(baseUrl);
            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(webHostingLink).perform(); // Hover over the element
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]"))); // Wait for dropdown
            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]"));
            Assert.assertEquals(dropdown.isDisplayed(), "\"Web Hosting\" dropdown is not displayed on hover.");
            System.out.println("TC_074 - \"Web Hosting\" dropdown visibility test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_074_Failed");
            Assert.fail("TC_074 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_075_testWebHostingDropdownNavigation() {
        try {
            driver.get(baseUrl);
            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(webHostingLink).perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]")));
            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]"));
            Assert.assertEquals(dropdown.isDisplayed(), "\"Web Hosting\" dropdown is displayed.");
            WebElement firstLink = dropdown.findElement(By.xpath(".//a"));
            String href = firstLink.getAttribute("href");
            Assert.assertNotNull(href, "First link in dropdown has no href attribute.");
            Assert.assertFalse(href.isEmpty(), "First link in dropdown has an empty href attribute.");
            firstLink.click();
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            Assert.assertNotEquals(currentUrl, baseUrl, "Clicking the dropdown link did not change the URL.");
            System.out.println("TC_075 - \"Web Hosting\" dropdown navigation test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_075_Failed");
            Assert.fail("TC_075 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_076_testWebHostingDropdownCollapse() {
        try {
            driver.get(baseUrl);
            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(webHostingLink).perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]")));
            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]"));
            Assert.assertEquals(dropdown.isDisplayed(), "\"Web Hosting\" dropdown is displayed.");
            WebElement firstLink = dropdown.findElement(By.xpath(".//a"));
            firstLink.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]")));
            Assert.assertFalse(isDropdownDisplayed(), "Dropdown did not collapse after selecting an option.");
            driver.get(baseUrl);
            webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            actions.moveToElement(webHostingLink).perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]")));
            dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]"));
            Assert.assertEquals(dropdown.isDisplayed(), "\"Web Hosting\" dropdown is displayed.");
            actions.moveByOffset(0, 300).click().perform();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]")));
            Assert.assertFalse(isDropdownDisplayed(), "Dropdown did not collapse after clicking outside.");
            System.out.println("TC_073 - \"Web Hosting\" dropdown collapse test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_076_Failed");
            Assert.fail("TC_076 Failed: " + e.getMessage());
        }
    }

    private boolean isDropdownDisplayed() {
        try {
            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]"));
            return dropdown.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testCaseName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(screenshotDirectory + testCaseName + ".png");
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken for: " + testCaseName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
}
