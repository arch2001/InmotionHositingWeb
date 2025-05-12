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

public class TS_044 extends BaseClass {

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
    public void tc_158_verifyChatWithUsButtonVisibilityAndStyle() {
        try {
            driver.get(baseUrl);
            WebElement chatButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Chat With Us')]")));

            Assert.assertTrue(chatButton.isDisplayed(), "'Chat With Us' button is not visible.");

            String bgColor = chatButton.getCssValue("background-color");
            String fontColor = chatButton.getCssValue("color");

            Assert.assertNotNull(bgColor, "Background color not found.");
            Assert.assertNotNull(fontColor, "Font color not found.");

            System.out.println("TC_158 - Button visible with background: " + bgColor + ", text color: " + fontColor);
        } catch (Exception e) {
            takeScreenshot("TC_158_Failed");
            Assert.fail("TC_158 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_159_verifyChatWithUsButtonOpensChatWindow() {
        try {
            driver.get(baseUrl);

            // Wait for the 'Chat With Us' button to be clickable
            WebElement chatButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Chat With Us')]")));

            chatButton.click();
            
            // Wait for the potential chat pop-up or widget (increase sleep or wait for iframe)
            Thread.sleep(5000); // Increased wait time for chat window to open

            // Check if the chat iframe is opened
            boolean chatOpened = driver.findElements(By.tagName("iframe")).stream()
                .anyMatch(iframe -> iframe.getAttribute("title") != null && iframe.getAttribute("title").toLowerCase().contains("chat"));

            Assert.assertTrue(chatOpened, "Live chat window did not open after clicking the button.");

            System.out.println("TC_159 - Live chat window opened successfully.");
        } catch (Exception e) {
            takeScreenshot("TC_159_Failed");
            Assert.fail("TC_159 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 3)
    public void tc_160_verifyChatWithUsButtonOnMobile() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Simulate mobile
            driver.get(baseUrl);
            Thread.sleep(1000);

            WebElement chatButton = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Chat With Us')]")));

            Assert.assertTrue(chatButton.isDisplayed(), "'Chat With Us' button not visible on mobile.");
            System.out.println("TC_160 - 'Chat With Us' button is visible on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_160_Failed");
            Assert.fail("TC_160 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // reset to default size
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
