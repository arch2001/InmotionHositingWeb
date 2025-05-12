package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_036 extends BaseClass {

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
    public void tc_128_verifyNavigationOnGetStartedClick() {
        try {
            driver.get(baseUrl);
            WebElement getStartedBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Get Started')]")));

            getStartedBtn.click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.urlContains("hosting") // Adjust based on expected URL
            );

            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("hosting"), "Navigation failed after clicking 'Get Started'.");

            System.out.println("TC_128 - Navigated successfully to: " + currentUrl);
        } catch (Exception e) {
            takeScreenshot("TC_128_Failed");
            Assert.fail("TC_128 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_129_verifyPageLoadsAfterGetStartedClick() {
        try {
            driver.get(baseUrl);
            WebElement getStartedBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Get Started')]")));

            getStartedBtn.click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState").equals("complete"));

            Assert.assertTrue(driver.getTitle().length() > 0, "Page did not load completely.");
            System.out.println("TC_129 - Page loaded successfully after clicking 'Get Started'.");
        } catch (Exception e) {
            takeScreenshot("TC_129_Failed");
            Assert.fail("TC_129 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_130_verifyKeyboardAccessibilityOfGetStartedButton() {
        try {
            driver.get(baseUrl);
            WebElement body = driver.findElement(By.tagName("body"));
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.TAB).perform();

            boolean found = false;
            for (int i = 0; i < 10; i++) {
                WebElement active = driver.switchTo().activeElement();
                if (active.getText().equalsIgnoreCase("Get Started")) {
                    active.sendKeys(Keys.ENTER);
                    found = true;
                    break;
                }
                actions.sendKeys(Keys.TAB).perform();
            }

            Assert.assertTrue(found, "'Get Started' button was not reachable by keyboard.");
            System.out.println("TC_130 - 'Get Started' button is keyboard accessible.");
        } catch (Exception e) {
            takeScreenshot("TC_130_Failed");
            Assert.fail("TC_130 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_131_verifyGetStartedButtonFunctionalityOnMobile() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // iPhone 6/7/8 size
            driver.get(baseUrl);
            WebElement getStartedBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Get Started')]")));

            getStartedBtn.click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.urlContains("hosting"));

            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("hosting"), "Navigation failed on mobile.");

            System.out.println("TC_131 - 'Get Started' button works correctly on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_131_Failed");
            Assert.fail("TC_131 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
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

