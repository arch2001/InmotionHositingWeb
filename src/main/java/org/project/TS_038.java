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

public class TS_038 extends BaseClass {

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
    public void tc_136_verifyPlanNamesAreDisplayed() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            List<WebElement> planNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".pricing-card .title, .plan-title")));

            Assert.assertTrue(planNames.size() > 0, "No plan names found.");

            for (WebElement name : planNames) {
                String text = name.getText().trim();
                Assert.assertFalse(text.isEmpty(), "Empty plan name found.");
                System.out.println("Plan name found: " + text);
            }
        } catch (Exception e) {
            takeScreenshot("TC_136_Failed");
            Assert.fail("TC_136 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_137_verifyPricingIsVisible() {
        try {
            List<WebElement> prices = driver.findElements(By.cssSelector(".pricing-card .price, .plan-price"));

            Assert.assertTrue(prices.size() > 0, "Pricing information not found.");

            for (WebElement price : prices) {
                String priceText = price.getText().trim();
                Assert.assertTrue(priceText.matches(".*\\$[0-9]+.*"), "Invalid or missing price format: " + priceText);
                System.out.println("Price found: " + priceText);
            }
        } catch (Exception e) {
            takeScreenshot("TC_137_Failed");
            Assert.fail("TC_137 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_138_verifyKeyFeaturesListed() {
        try {
            List<WebElement> featureLists = driver.findElements(By.cssSelector(".pricing-card ul, .plan-features"));

            Assert.assertTrue(featureLists.size() > 0, "No feature lists found.");

            for (WebElement ul : featureLists) {
                List<WebElement> features = ul.findElements(By.tagName("li"));
                Assert.assertTrue(features.size() > 0, "No features listed in one of the cards.");

                for (WebElement feature : features) {
                    String text = feature.getText().trim();
                    Assert.assertFalse(text.isEmpty(), "Empty feature text found.");
                }
            }

            System.out.println("TC_138 - All features are listed correctly.");
        } catch (Exception e) {
            takeScreenshot("TC_138_Failed");
            Assert.fail("TC_138 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 4)
    public void tc_139_verifyTextGrammarConsistency() {
        try {
            List<WebElement> cardTexts = driver.findElements(By.cssSelector(".pricing-card, .plan-section"));

            for (WebElement card : cardTexts) {
                String content = card.getText();
                Assert.assertFalse(content.contains("lorem ipsum"), "Placeholder text found.");
                Assert.assertFalse(content.matches(".*\\s{2,}.*"), "Extra spaces found.");
            }

            System.out.println("TC_139 - Spelling/grammar seems consistent (basic check).");
        } catch (Exception e) {
            takeScreenshot("TC_139_Failed");
            Assert.fail("TC_139 Failed: " + e.getMessage());
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
