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

public class TS_021 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final By wordpressMenuLocator = By.xpath("//*[contains(text(), 'WordPress')]");
    private final By dropdownMenuLocator = By.xpath(
        "//*[@class='dropdown-menu' and preceding-sibling::*[contains(text(), 'WordPress')]]"
    );
    private final String screenshotDirectory = "./screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);

        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_077_testDropdownVisibilityAndPlacement() {
        try {
            WebElement menu = driver.findElement(wordpressMenuLocator);
            new Actions(driver).moveToElement(menu).perform();

            WebElement dropdown = driver.findElement(dropdownMenuLocator);
            Assert.assertTrue(dropdown.isDisplayed(), "Dropdown should appear on hover.");

            Point menuLoc    = menu.getLocation();
            Dimension menuSz = menu.getSize();
            Point dropLoc    = dropdown.getLocation();

            Assert.assertTrue(
                dropLoc.getY() >= menuLoc.getY(),
                "Dropdown Y should be ≥ menu Y."
            );
            Assert.assertTrue(
                Math.abs(dropLoc.getX() - menuLoc.getX()) < menuSz.getWidth(),
                "Dropdown X should align under the menu."
            );
        } catch (Exception e) {
            takeScreenshot("TC_077_PlacementFailed");
            Assert.fail("TC_077 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_078_testDropdownStylingAndHoverBehavior() {
        try {
            WebElement menu = driver.findElement(wordpressMenuLocator);
            String cursor = menu.getCssValue("cursor");
            Assert.assertNotEquals(cursor, "default", "Cursor should change on hover.");

            new Actions(driver).moveToElement(menu).perform();
            WebElement dropdown = driver.findElement(dropdownMenuLocator);

            String bgColor = dropdown.getCssValue("background-color");
            Assert.assertNotEquals(
                bgColor,
                "rgba(0, 0, 0, 0)",
                "Dropdown must have a visible background color."
            );
        } catch (Exception e) {
            takeScreenshot("TC_078_StylingHoverFailed");
            Assert.fail("TC_078 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_079_testDropdownResponsivenessAcrossDevices() {
        try {
            Dimension[] deviceSizes = {
                new Dimension(1920, 1080),
                new Dimension(768,  1024),
                new Dimension(375,  812)
            };

            for (Dimension size : deviceSizes) {
                driver.manage().window().setSize(size);
                System.out.println("Testing at: " + size.getWidth() + "×" + size.getHeight());

                WebElement menu = driver.findElement(wordpressMenuLocator);
                new Actions(driver).moveToElement(menu).perform();
                WebElement dropdown = driver.findElement(dropdownMenuLocator);

                Assert.assertTrue(
                    dropdown.isDisplayed(),
                    "Dropdown not visible at " + size.getWidth() + "×" + size.getHeight()
                );
            }
        } catch (Exception e) {
            takeScreenshot("TC_079_ResponsivenessFailed");
            Assert.fail("TC_079 Failed: " + e.getMessage());
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
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dst = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dst);
            System.out.println("Screenshot saved: " + dst.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}

