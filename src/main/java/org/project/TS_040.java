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

public class TS_040 extends BaseClass {

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
    public void tc_144_verifyButtonVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(),'See Managed Hosting Services')]")));

            Assert.assertTrue(button.isDisplayed(), "Button not visible.");
            Point location = button.getLocation();
            Dimension size = button.getSize();

            System.out.println("TC_144 - Button location: " + location + ", Size: " + size);
        } catch (Exception e) {
            takeScreenshot("TC_144_Failed");
            Assert.fail("TC_144 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_145_verifyStyling() {
        try {
            WebElement button = driver.findElement(By.xpath("//a[contains(text(),'See Managed Hosting Services')]"));

            String color = button.getCssValue("color");
            String font = button.getCssValue("font-family");
            String border = button.getCssValue("border");

            Assert.assertNotNull(color, "Color is not set.");
            Assert.assertNotNull(font, "Font is not set.");
            Assert.assertNotNull(border, "Border is not set.");

            System.out.println("TC_145 - Color: " + color + ", Font: " + font + ", Border: " + border);
        } catch (Exception e) {
            takeScreenshot("TC_145_Failed");
            Assert.fail("TC_145 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_146_verifyHoverEffect() {
        try {
            WebElement button = driver.findElement(By.xpath("//a[contains(text(),'See Managed Hosting Services')]"));

            // Get the initial background color
            String initialBg = button.getCssValue("background-color");

            // Create a JavascriptExecutor to simulate the hover event
            String hoverScript = "var evt = new MouseEvent('mouseover', { bubbles: true }); arguments[0].dispatchEvent(evt);";
            ((JavascriptExecutor) driver).executeScript(hoverScript, button);

            // Explicit wait for the background color to change (you can adjust the duration if needed)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(button, "background-color", initialBg)));

            // Get the background color after hover
            String hoverBg = button.getCssValue("background-color");

            // Assert that the background color has changed
            Assert.assertNotEquals(initialBg, hoverBg, "Background color did not change on hover.");

            System.out.println("TC_146 - Hover effect verified: " + initialBg + " → " + hoverBg);
        } catch (Exception e) {
            takeScreenshot("TC_146_Failed");
            Assert.fail("TC_146 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 4)
    public void tc_147_verifyResponsiveness() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile size
            Thread.sleep(2000);

            WebElement button = driver.findElement(By.xpath("//a[contains(text(),'See Managed Hosting Services')]"));
            Assert.assertTrue(button.isDisplayed(), "Button not visible in mobile view.");

            System.out.println("TC_147 - Button is responsive and visible in mobile view.");
        } catch (Exception e) {
            takeScreenshot("TC_147_Failed");
            Assert.fail("TC_147 Failed: " + e.getMessage());
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
