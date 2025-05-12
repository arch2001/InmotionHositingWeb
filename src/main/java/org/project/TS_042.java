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

public class TS_042 extends BaseClass {

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
    public void tc_152_verifyLearnMoreButtonRedirection() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement learnMoreBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(), 'Learn More') and contains(@href, '/')]")));

            String originalUrl = driver.getCurrentUrl();
            learnMoreBtn.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(originalUrl)));

            String newUrl = driver.getCurrentUrl();
            Assert.assertNotEquals(newUrl, originalUrl, "Redirection failed after clicking Learn More.");
            System.out.println("TC_152 - Redirected to: " + newUrl);
        } catch (Exception e) {
            takeScreenshot("TC_152_Failed");
            Assert.fail("TC_152 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_153_verifyLearnMoreButtonClickAndHoverEffect() {
        try {
            driver.get(baseUrl);
            
            // Explicitly wait for the element to be clickable
            WebElement learnMoreBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Learn More') and contains(@href, '/')]")));

            // Get initial color before hover
            String initialColor = learnMoreBtn.getCssValue("color");

            // Use Actions class to hover over the button
            Actions actions = new Actions(driver);
            actions.moveToElement(learnMoreBtn).perform();
            Thread.sleep(1000); // Ensure hover effect takes place

            // Get color after hover
            String hoverColor = learnMoreBtn.getCssValue("color");

            // Verify hover effect is applied (color change)
            Assert.assertNotEquals(initialColor, hoverColor, "Hover effect is not applied.");
            System.out.println("TC_153 - Learn More button hover effect verified.");
            
        } catch (Exception e) {
            takeScreenshot("TC_153_Failed");
            Assert.fail("TC_153 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_154_verifyLearnMoreButtonOnMobile() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile screen
            driver.get(baseUrl);
            Thread.sleep(1000);

            WebElement learnMoreBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Learn More') and contains(@href, '/')]")));

            Assert.assertTrue(learnMoreBtn.isDisplayed(), "Learn More button not visible on mobile.");
            System.out.println("TC_154 - Learn More button visible on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_154_Failed");
            Assert.fail("TC_154 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // Reset window
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
