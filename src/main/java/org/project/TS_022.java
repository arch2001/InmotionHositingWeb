package org.project;



import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TS_022 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final By wordpressMenuLocator = By.xpath("//*[contains(text(), 'WordPress')]");
    private final By dropdownMenuLocator = By.xpath(
        "//*[@class='dropdown-menu' and preceding-sibling::*[contains(text(), 'WordPress')]]"
    );
    private String screenshotDirectory = "./screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // ensure screenshot directory exists
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // load once for all tests
        driver.get(baseUrl);
    }

    @Test(priority = 1)
    public void tc_080_testDropdownVisibilityAndPlacement() {
        try {
            WebElement wordpressMenu = driver.findElement(wordpressMenuLocator);
            new Actions(driver).moveToElement(wordpressMenu).perform();

            WebElement dropdownMenu = driver.findElement(dropdownMenuLocator);
            Assert.assertTrue(dropdownMenu.isDisplayed(), "Dropdown should appear on hover.");

            Point    menuLoc      = wordpressMenu.getLocation();
            Dimension menuSize    = wordpressMenu.getSize();
            Point    dropdownLoc  = dropdownMenu.getLocation();

            Assert.assertTrue(
                dropdownLoc.getY() >= menuLoc.getY(),
                "Dropdown Y should be below or equal to the menu."
            );
            Assert.assertTrue(
                Math.abs(dropdownLoc.getX() - menuLoc.getX()) < menuSize.getWidth(),
                "Dropdown X should be aligned within the menu width."
            );

        } catch (Exception e) {
            takeScreenshot("TC_080_PlacementFailed");
            Assert.fail("TC_080 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_081_testDropdownStylingAndHoverBehavior() {
        try {
            WebElement wordpressMenu = driver.findElement(wordpressMenuLocator);

            // before hover: cursor style
            String initialCursor = wordpressMenu.getCssValue("cursor");
            Assert.assertNotEquals(initialCursor, "default", "Cursor should not be 'default'.");

            // hover and check background
            new Actions(driver).moveToElement(wordpressMenu).perform();
            WebElement dropdownMenu = driver.findElement(dropdownMenuLocator);
            String bgColor = dropdownMenu.getCssValue("background-color");
            Assert.assertNotEquals(
                bgColor, 
                "rgba(0, 0, 0, 0)", 
                "Dropdown must have a non-transparent background."
            );

        } catch (Exception e) {
            takeScreenshot("TC_081_StylingHoverFailed");
            Assert.fail("TC_081 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_082_testDropdownResponsivenessAcrossDevices() {
        try {
            Dimension[] resolutions = {
                new Dimension(1920, 1080),
                new Dimension(768, 1024),
                new Dimension(375, 812)
            };

            for (Dimension res : resolutions) {
                driver.manage().window().setSize(res);
                System.out.println("Testing at: " + res.getWidth() + "x" + res.getHeight());

                WebElement wordpressMenu = driver.findElement(wordpressMenuLocator);
                new Actions(driver).moveToElement(wordpressMenu).perform();

                WebElement dropdownMenu = driver.findElement(dropdownMenuLocator);
                Assert.assertTrue(
                    dropdownMenu.isDisplayed(),
                    "Dropdown not visible at " + res.getWidth() + "x" + res.getHeight()
                );
            }

        } catch (Exception e) {
            takeScreenshot("TC_082_ResponsivenessFailed");
            Assert.fail("TC_082 Failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testCaseName) {
        try {
            File src  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dst  = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dst);
            System.out.println("Screenshot saved: " + dst.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
