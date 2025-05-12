package org.project;



import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TS_025 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_089_testDedicatedServersDropdownVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));
            Assert.assertTrue(menu.isDisplayed(), "Dedicated Servers dropdown is not visible.");
            Point location = menu.getLocation();
            System.out.println("TC_089 - Dropdown location: " + location);
        } catch (Exception e) {
            takeScreenshot("TC_089_Failed");
            Assert.fail("TC_089 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_090_testDropdownStylingAndHoverEffect() {
        try {
            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));
            String color = menu.getCssValue("color");
            String bgColor = menu.getCssValue("background-color");

            Actions action = new Actions(driver);
            action.moveToElement(menu).perform();

            Thread.sleep(1000); // wait for hover effect to apply
            String hoverColor = menu.getCssValue("color");

            System.out.println("TC_090 - Normal Color: " + color + ", Hover Color: " + hoverColor + ", Background: " + bgColor);
            Assert.assertNotEquals(color, hoverColor, "Hover color should be different");

        } catch (Exception e) {
            takeScreenshot("TC_090_Failed");
            Assert.fail("TC_090 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_091_testDropdownResponsiveOnMobile() {
        try {
            Dimension mobile = new Dimension(375, 812); // iPhone X
            driver.manage().window().setSize(mobile);
            driver.navigate().refresh();

            try {
                WebElement toggle = driver.findElement(By.cssSelector("button[aria-label='Toggle navigation']"));
                if (toggle.isDisplayed()) toggle.click();
            } catch (NoSuchElementException ignored) {}

            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));
            Assert.assertTrue(menu.isDisplayed(), "Dedicated Servers dropdown not visible on mobile.");
            System.out.println("TC_091 - Dropdown is visible in mobile view.");
        } catch (Exception e) {
            takeScreenshot("TC_091_Failed");
            Assert.fail("TC_091 Failed: " + e.getMessage());
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
            File dest = new File(screenshotDirectory + testCaseName + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved for: " + testCaseName);
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
