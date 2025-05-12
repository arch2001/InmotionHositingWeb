package org.project;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.util.concurrent.TimeUnit;

public class TS_015 extends BaseClass { //  Extends BaseClass

    private WebDriver driver;
    private String baseUrl = "https://www.inmotionhosting.com/";
    private String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        //  Initialize the driver and set up the screenshot directory.
        //  This is now in TS_012, assuming BaseClass doesn't handle this.
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_064_testChatNowLinkUIAndResponsiveness() { // Changed method name to follow convention
        // TC_058: Verify Chat Now link UI and responsiveness across different screen sizes.
        try {
            driver.get(baseUrl);

            // Define screen sizes
            int[][] screenSizes = {
                    {1920, 1080},
                    {1366, 768},
                    {768, 1024},
                    {1024, 768},
                    {375, 667},
                    {414, 736}
            };

            for (int[] size : screenSizes) {
                int width = size[0];
                int height = size[1];

                driver.manage().window().setSize(new Dimension(width, height));

                WebElement chatNowLink = driver.findElement(By.id("chat-now-button-id")); //  Replace with actual locator

                Assert.assertTrue(chatNowLink.isDisplayed(), "Chat Now link is not displayed at " + width + "x" + height);
                Assert.assertTrue(chatNowLink.isEnabled(), "Chat Now link is not enabled at " + width + "x" + height);

                //  Add more UI checks as needed (example: check font size, color)
                String fontSize = chatNowLink.getCssValue("font-size");
                String color = chatNowLink.getCssValue("color");

                Assert.assertNotNull(fontSize, "Font size is null at " + width + "x" + height);
                Assert.assertFalse(fontSize.isEmpty(), "Font size is empty at " + width + "x" + height);
                Assert.assertNotNull(color, "Color is null at " + width + "x" + height);
                Assert.assertFalse(color.isEmpty(), "Color is empty at " + width + "x" + height);
                System.out.println("Chat Now link is responsive and UI is correct at " + width + "x" + height);
            }
            System.out.println("Test Case TC_058 Passed: Chat Now link is responsive across different screen sizes.");

        } catch (Exception e) {
            takeScreenshot("TC_058_Failed");
            Assert.fail("Test Case TC_058 Failed: " + e.getMessage());
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
