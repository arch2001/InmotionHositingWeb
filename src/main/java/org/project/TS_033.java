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

public class TS_033 extends BaseClass {

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
    public void tc_116_verifyLoginButtonVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            Assert.assertTrue(loginBtn.isDisplayed(), "Login button is not visible.");
            Point location = loginBtn.getLocation();
            System.out.println("TC_116 - Login button is visible at position: " + location);
        } catch (Exception e) {
            takeScreenshot("TC_116_Failed");
            Assert.fail("TC_116 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_117_verifyLoginButtonStylingAndHoverEffect() {
        try {
            driver.get(baseUrl);
            WebElement loginBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            String initialColor = loginBtn.getCssValue("color");
            String initialBg = loginBtn.getCssValue("background-color");

            // Simulate hover using JavaScript
            String hoverScript = "var evt = new MouseEvent('mouseover', { bubbles: true }); arguments[0].dispatchEvent(evt);";
            ((JavascriptExecutor) driver).executeScript(hoverScript, loginBtn);
            WebDriverWait hoverWait = new WebDriverWait(driver, Duration.ofSeconds(1)); // Wait for hover effect
            hoverWait.until(ExpectedConditions.attributeToBeNotEmpty(loginBtn, "color")); // Wait for color change

            String hoverColor = loginBtn.getCssValue("color");
            String hoverBg = loginBtn.getCssValue("background-color");

            Assert.assertNotEquals(initialColor, hoverColor, "Text color did not change on hover.");
            Assert.assertNotEquals(initialBg, hoverBg, "Background color did not change on hover.");

            System.out.println("TC_117 - Login button hover effect verified.");
        } catch (Exception e) {
            takeScreenshot("TC_117_Failed");
            Assert.fail("TC_117 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_118_verifyLoginButtonFontAndSize() {
        try {
            driver.get(baseUrl);
            WebElement loginBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            String font = loginBtn.getCssValue("font-family");
            String fontSize = loginBtn.getCssValue("font-size");

            Assert.assertNotNull(font, "Font not set.");
            Assert.assertTrue(fontSize.endsWith("px"), "Font size is not in px.");

            System.out.println("TC_118 - Font: " + font + ", Size: " + fontSize);
        } catch (Exception e) {
            takeScreenshot("TC_118_Failed");
            Assert.fail("TC_118 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_119_verifyLoginButtonResponsiveness() {
        try {
            driver.get(baseUrl);
            WebElement loginBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            Dimension desktopSize = driver.manage().window().getSize();
            boolean visibleAtDesktop = loginBtn.isDisplayed();

            driver.manage().window().setSize(new Dimension(375, 667)); // iPhone 6/7/8 size
            Thread.sleep(1000);

            loginBtn = driver.findElement(By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]"));
            boolean visibleAtMobile = loginBtn.isDisplayed();

            Assert.assertTrue(visibleAtDesktop && visibleAtMobile, "Login button is not responsive.");

            System.out.println("TC_119 - Login button visible at desktop and mobile sizes.");
        } catch (Exception e) {
            takeScreenshot("TC_119_Failed");
            Assert.fail("TC_119 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // Reset window size
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
