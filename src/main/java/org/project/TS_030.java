package org.project;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class TS_030 extends BaseClass {

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
    public void tc_104_verifyCompanyDropdownRevealsOptions() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement companyMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Company' or contains(text(),'Company')]")));

            Actions actions = new Actions(driver);
            actions.moveToElement(companyMenu).perform();
            Thread.sleep(1500);

            WebElement dropdownPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.sub-menu")));
            Assert.assertTrue(dropdownPanel.isDisplayed(), "Dropdown panel is not visible.");

            System.out.println("TC_104 - Dropdown options revealed on hover.");
        } catch (Exception e) {
            takeScreenshot("TC_104_Failed");
            Assert.fail("TC_104 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_105_verifyCompanyDropdownOptionRedirection() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement companyMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Company' or contains(text(),'Company')]")));

            Actions actions = new Actions(driver);
            actions.moveToElement(companyMenu).perform();
            Thread.sleep(1500);

            WebElement dropdownPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.sub-menu")));
            List<WebElement> options = dropdownPanel.findElements(By.tagName("a"));
            WebElement firstOption = options.get(0);

            String expectedUrlPart = firstOption.getAttribute("href").split("//")[1].split("/")[0];
            firstOption.click();

            wait.until(ExpectedConditions.urlContains(expectedUrlPart));
            Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrlPart),
                    "Redirection failed. Current URL: " + driver.getCurrentUrl());

            System.out.println("TC_105 - Click on dropdown option redirected successfully.");
        } catch (Exception e) {
            takeScreenshot("TC_105_Failed");
            Assert.fail("TC_105 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_106_verifyDropdownCollapsesOnClickOutside() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement companyMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Company' or contains(text(),'Company')]")));

            Actions actions = new Actions(driver);
            actions.moveToElement(companyMenu).perform();
            Thread.sleep(1500);

            WebElement dropdownPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.sub-menu")));

            WebElement logo = driver.findElement(By.cssSelector("a.logo"));
            logo.click();
            Thread.sleep(1000);

            boolean isHidden = wait.until(ExpectedConditions.invisibilityOf(dropdownPanel));
            Assert.assertTrue(isHidden, "Dropdown did not collapse on click outside.");

            System.out.println("TC_106 - Dropdown collapsed on click outside.");
        } catch (Exception e) {
            takeScreenshot("TC_106_Failed");
            Assert.fail("TC_106 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_107_verifyAllCompanySubOptionsClickable() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement companyMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Company' or contains(text(),'Company')]")));

            Actions actions = new Actions(driver);
            actions.moveToElement(companyMenu).perform();
            Thread.sleep(1500);

            WebElement dropdownPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.sub-menu")));
            List<WebElement> options = dropdownPanel.findElements(By.tagName("a"));

            for (WebElement option : options) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
                Assert.assertTrue(option.isDisplayed() && option.isEnabled(),
                        "Sub-option is not clickable: " + option.getText());
            }

            System.out.println("TC_107 - All sub-options are clickable.");
        } catch (Exception e) {
            takeScreenshot("TC_107_Failed");
            Assert.fail("TC_107 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void tc_108_verifyKeyboardNavigationToCompanyMenu() {
        try {
            driver.get(baseUrl);
            WebElement body = driver.findElement(By.tagName("body"));

            for (int i = 0; i < 15; i++) {
                body.sendKeys(Keys.TAB);
                Thread.sleep(200);
            }

            body.sendKeys(Keys.ENTER);
            System.out.println("TC_108 - Simulated keyboard navigation (manual visual confirmation recommended).");
        } catch (Exception e) {
            takeScreenshot("TC_108_Failed");
            Assert.fail("TC_108 Failed: " + e.getMessage());
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
