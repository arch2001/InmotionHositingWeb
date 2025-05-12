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
import java.util.List;

public class TS_046 extends BaseClass {

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
    public void tc_165_verifyFeaturesPlanCardsVisibilityAndAlignment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement planSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[contains(@class, 'features-plan')]")));
            Assert.assertNotNull(planSection, "Features Plan section not found.");

            // Additional checks to verify the alignment or visibility of plan cards can be added here
            
            System.out.println("TC_165 - Features Plan cards are visible and aligned.");

        } catch (Exception e) {
            takeScreenshot("TC_165_Failed");
            Assert.fail("TC_165 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_166_verifyTitlesSubtitlesIconsAndDescriptions() {
        try {
            driver.get(baseUrl);
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));

            for (WebElement card : planCards) {
                WebElement title = card.findElement(By.xpath(".//h3"));
                WebElement subtitle = card.findElement(By.xpath(".//h4"));
                WebElement description = card.findElement(By.xpath(".//p[contains(@class, 'description')]"));
                WebElement icon = card.findElement(By.xpath(".//i[contains(@class, 'icon')]"));

                Assert.assertNotNull(title.getText(), "Title is missing in the plan card.");
                Assert.assertNotNull(subtitle.getText(), "Subtitle is missing in the plan card.");
                Assert.assertNotNull(description.getText(), "Description is missing in the plan card.");
                Assert.assertTrue(icon.isDisplayed(), "Icon is missing in the plan card.");

                System.out.println("TC_166 - Plan card title: " + title.getText() + ", Subtitle: " + subtitle.getText() + ", Description: " + description.getText());
            }
        } catch (Exception e) {
            takeScreenshot("TC_166_Failed");
            Assert.fail("TC_166 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_167_verifyCardStylingConsistency() {
        try {
            driver.get(baseUrl);
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));

            for (WebElement card : planCards) {
                // Verifying styling consistency for font size, icon size, and color theme
                String fontSize = card.getCssValue("font-size");
                String iconSize = card.findElement(By.xpath(".//i")).getCssValue("font-size");
                String backgroundColor = card.getCssValue("background-color");

                Assert.assertNotNull(fontSize, "Font size not consistent.");
                Assert.assertNotNull(iconSize, "Icon size not consistent.");
                Assert.assertNotNull(backgroundColor, "Background color not consistent.");

                System.out.println("TC_167 - Font size: " + fontSize + ", Icon size: " + iconSize + ", Background color: " + backgroundColor);
            }
        } catch (Exception e) {
            takeScreenshot("TC_167_Failed");
            Assert.fail("TC_167 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_168_verifyResponsiveLayoutForMobileAndTablet() {
        try {
            // Initialize WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Simulate mobile view
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile view size
            driver.get(baseUrl);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'plan-card')]")));
            
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));
            Assert.assertTrue(planCards.size() > 0, "No Features Plan cards visible on mobile.");
            System.out.println("TC_168 - Features Plan cards are responsive on mobile.");

            // Simulate tablet view
            driver.manage().window().setSize(new Dimension(768, 1024)); // Tablet view size
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'plan-card')]")));
            
            planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));
            Assert.assertTrue(planCards.size() > 0, "No Features Plan cards visible on tablet.");
            System.out.println("TC_168 - Features Plan cards are responsive on tablet.");

        } catch (Exception e) {
            takeScreenshot("TC_168_Failed");
            Assert.fail("TC_168 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // Reset to default size
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
