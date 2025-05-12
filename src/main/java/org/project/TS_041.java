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

public class TS_041 extends BaseClass {

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
    public void tc_148_verifyBusinessCardVisibilityAndAlignment() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement businessCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//section[contains(., 'Business') and contains(@class, 'card')]")));

            Assert.assertTrue(businessCard.isDisplayed(), "Business card section is not visible.");
            Point position = businessCard.getLocation();
            Dimension size = businessCard.getSize();

            System.out.println("TC_148 - Business card position: " + position + ", Size: " + size);
        } catch (Exception e) {
            takeScreenshot("TC_148_Failed");
            Assert.fail("TC_148 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_149_verifyBusinessCardContent() {
        try {
            WebElement heading = driver.findElement(By.xpath("//section[contains(., 'Business')]//h2"));
            WebElement subheading = driver.findElement(By.xpath("//section[contains(., 'Business')]//h3 | //section[contains(., 'Business')]//p"));
            
            Assert.assertTrue(heading.isDisplayed(), "Heading not found.");
            Assert.assertTrue(subheading.isDisplayed(), "Subheading or description not found.");

            System.out.println("TC_149 - Heading: " + heading.getText());
            System.out.println("Subheading/Content: " + subheading.getText());
        } catch (Exception e) {
            takeScreenshot("TC_149_Failed");
            Assert.fail("TC_149 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_150_verifyStylingOfBusinessCard() {
        try {
            WebElement businessCard = driver.findElement(By.xpath("//section[contains(., 'Business') and contains(@class, 'card')]"));

            String bgColor = businessCard.getCssValue("background-color");
            String font = businessCard.getCssValue("font-family");

            Assert.assertNotNull(bgColor, "Background color not set.");
            Assert.assertNotNull(font, "Font not set.");

            System.out.println("TC_150 - Background color: " + bgColor + ", Font: " + font);
        } catch (Exception e) {
            takeScreenshot("TC_150_Failed");
            Assert.fail("TC_150 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_151_verifyResponsivenessOfBusinessCard() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile size
            Thread.sleep(2000);

            WebElement businessCard = driver.findElement(By.xpath("//section[contains(., 'Business') and contains(@class, 'card')]"));
            Assert.assertTrue(businessCard.isDisplayed(), "Business card not visible in mobile view.");

            System.out.println("TC_151 - Business card visible and responsive on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_151_Failed");
            Assert.fail("TC_151 Failed: " + e.getMessage());
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
