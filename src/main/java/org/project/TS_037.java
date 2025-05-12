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

public class TS_037 extends BaseClass {

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
    public void tc_132_verifyBestPlanCardsVisibility() {
        try {
            driver.get(baseUrl);
            List<WebElement> cards = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".plans .card, .pricing-card")));

            Assert.assertTrue(cards.size() >= 3, "Expected at least 3 Best Plan cards.");
            for (WebElement card : cards) {
                Assert.assertTrue(card.isDisplayed(), "One or more Best Plan cards are not visible.");
            }

            System.out.println("TC_132 - All Best Plan cards are visible.");
        } catch (Exception e) {
            takeScreenshot("TC_132_Failed");
            Assert.fail("TC_132 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_133_verifyCardStylingConsistency() {
        try {
            driver.get(baseUrl);
            List<WebElement> cards = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".plans .card, .pricing-card")));

            String bgColorRef = cards.get(0).getCssValue("background-color");
            String fontRef = cards.get(0).getCssValue("font-family");

            for (WebElement card : cards) {
                Assert.assertEquals(card.getCssValue("background-color"), bgColorRef, "Background color mismatch.");
                Assert.assertEquals(card.getCssValue("font-family"), fontRef, "Font mismatch.");
            }

            System.out.println("TC_133 - Best Plan cards have consistent styling.");
        } catch (Exception e) {
            takeScreenshot("TC_133_Failed");
            Assert.fail("TC_133 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_134_verifyHoverEffectOnBestPlanCards() {
        try {
            driver.get(baseUrl);
            List<WebElement> cards = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".plans .card, .pricing-card")));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (WebElement card : cards) {
                String initialShadow = card.getCssValue("box-shadow");
                js.executeScript("arguments[0].scrollIntoView(true);", card);
                js.executeScript("var evt = new MouseEvent('mouseover', { bubbles: true }); arguments[0].dispatchEvent(evt);", card);
                Thread.sleep(500);
                String hoverShadow = card.getCssValue("box-shadow");

                Assert.assertNotEquals(hoverShadow, initialShadow, "Hover effect not observed.");
            }

            System.out.println("TC_134 - Hover effects verified on Best Plan cards.");
        } catch (Exception e) {
            takeScreenshot("TC_134_Failed");
            Assert.fail("TC_134 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_135_verifyBestPlanCardsResponsiveLayout() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile view
            driver.get(baseUrl);
            List<WebElement> cards = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".plans .card, .pricing-card")));

            for (WebElement card : cards) {
                Assert.assertTrue(card.isDisplayed(), "Card not visible in mobile layout.");
                int width = card.getSize().getWidth();
                Assert.assertTrue(width < 400, "Card is not responsive (too wide on mobile).");
            }

            System.out.println("TC_135 - Best Plan cards are responsive on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_135_Failed");
            Assert.fail("TC_135 Failed: " + e.getMessage());
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
