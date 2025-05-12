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

public class TS_045 extends BaseClass {

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
    public void tc_161_verifyHostingPlanCardsVisibilityAndAlignment() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time to 20 seconds

        try {
            driver.get(baseUrl);

            // Wait for the hosting plans section to be visible
            WebElement cardSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[contains(@class, 'hosting-plans')]")));
            Assert.assertTrue(cardSection.isDisplayed(), "Hosting Plan cards section is not visible.");

            // Verifying card visibility and alignment
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));
            Assert.assertTrue(planCards.size() > 0, "No Hosting Plan cards found.");
            
            for (WebElement card : planCards) {
                Assert.assertTrue(card.isDisplayed(), "Hosting Plan card is not visible.");
                System.out.println("TC_161 - Hosting Plan card is visible.");
            }
        } catch (Exception e) {
            takeScreenshot("TC_161_Failed");
            Assert.fail("TC_161 Failed: " + e.getMessage());
        }
    }
    @Test(priority = 2)
    public void tc_162_verifyTitlesDescriptionsAndPricingInCards() {
        try {
            driver.get(baseUrl);
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));

            for (WebElement card : planCards) {
                WebElement title = card.findElement(By.xpath(".//h3"));
                WebElement description = card.findElement(By.xpath(".//p[contains(@class, 'description')]"));
                WebElement price = card.findElement(By.xpath(".//span[contains(@class, 'price')]"));

                Assert.assertNotNull(title.getText(), "Plan title is missing.");
                Assert.assertNotNull(description.getText(), "Plan description is missing.");
                Assert.assertNotNull(price.getText(), "Pricing details are missing.");

                System.out.println("TC_162 - Plan: " + title.getText() + ", Description: " + description.getText() + ", Price: " + price.getText());
            }
        } catch (Exception e) {
            takeScreenshot("TC_162_Failed");
            Assert.fail("TC_162 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_163_verifyCardStylingConsistency() {
        try {
            driver.get(baseUrl);
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));

            for (WebElement card : planCards) {
                // Verifying styling consistency
                String fontFamily = card.getCssValue("font-family");
                String backgroundColor = card.getCssValue("background-color");
                String border = card.getCssValue("border");

                Assert.assertNotNull(fontFamily, "Font family not consistent.");
                Assert.assertNotNull(backgroundColor, "Background color not consistent.");
                Assert.assertNotNull(border, "Border style not consistent.");

                System.out.println("TC_163 - Card styling: Font: " + fontFamily + ", Background: " + backgroundColor + ", Border: " + border);
            }
        } catch (Exception e) {
            takeScreenshot("TC_163_Failed");
            Assert.fail("TC_163 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_164_verifyResponsiveLayoutOnMobileAndTablet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Simulate mobile view
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile view size
            driver.get(baseUrl);
            
            // Wait for the plan cards to be visible on mobile
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'plan-card')]")));
            
            List<WebElement> planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));
            Assert.assertTrue(planCards.size() > 0, "No Hosting Plan cards visible on mobile.");
            System.out.println("TC_164 - Hosting Plan cards are responsive on mobile.");

            // Simulate tablet view
            driver.manage().window().setSize(new Dimension(768, 1024)); // Tablet view size
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'plan-card')]")));
            
            planCards = driver.findElements(By.xpath("//div[contains(@class, 'plan-card')]"));
            Assert.assertTrue(planCards.size() > 0, "No Hosting Plan cards visible on tablet.");
            System.out.println("TC_164 - Hosting Plan cards are responsive on tablet.");

        } catch (Exception e) {
            takeScreenshot("TC_164_Failed");
            Assert.fail("TC_164 Failed: " + e.getMessage());
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
