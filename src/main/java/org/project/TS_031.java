package org.project;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class TS_031 extends BaseClass {

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
    public void tc_109_verifySupportCenterLinkVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement supportLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.linkText("Support Center")));

            Assert.assertTrue(supportLink.isDisplayed(), "Support Center link is not visible.");

            Point location = supportLink.getLocation();
            System.out.println("TC_109 - Support Center link is visible at location: " + location);
        } catch (Exception e) {
            takeScreenshot("TC_109_Failed");
            Assert.fail("TC_109 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_110_verifySupportCenterLinkStylingAndHover() {
        try {
            WebElement supportLink = driver.findElement(By.linkText("Support Center"));

            String originalColor = supportLink.getCssValue("color");

            Actions actions = new Actions(driver);
            actions.moveToElement(supportLink).perform();

            Thread.sleep(1000); // allow hover effect to apply

            String hoverColor = supportLink.getCssValue("color");

            Assert.assertNotEquals(originalColor, hoverColor,
                    "Hover color did not change. Color: " + hoverColor);

            System.out.println("TC_110 - Hover effect applied. Original: " + originalColor + ", On Hover: " + hoverColor);
        } catch (Exception e) {
            takeScreenshot("TC_110_Failed");
            Assert.fail("TC_110 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_111_verifySupportCenterLinkFontSizeAndIconAlignment() {
        try {
            WebElement supportLink = driver.findElement(By.linkText("Support Center"));

            String fontSize = supportLink.getCssValue("font-size");
            Assert.assertNotNull(fontSize, "Font size not found.");
            System.out.println("TC_111 - Font size of Support Center link: " + fontSize);

            WebElement parent = supportLink.findElement(By.xpath(".."));
            List<WebElement> icons = parent.findElements(By.tagName("svg")); // or use "i" for icon fonts

            if (!icons.isEmpty()) {
                Dimension iconSize = icons.get(0).getSize();
                Point iconLocation = icons.get(0).getLocation();
                Point linkLocation = supportLink.getLocation();

                Assert.assertEquals(iconLocation.getY(), linkLocation.getY(), 5, "Icon not vertically aligned.");

                System.out.println("TC_111 - Icon is aligned with Support Center link. Icon size: " + iconSize);
            } else {
                System.out.println("TC_111 - No icon found near Support Center link.");
            }
        } catch (Exception e) {
            takeScreenshot("TC_111_Failed");
            Assert.fail("TC_111 Failed: " + e.getMessage());
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
