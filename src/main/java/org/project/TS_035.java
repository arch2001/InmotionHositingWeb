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

public class TS_035 extends BaseClass {

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
    public void tc_124_verifyGetStartedButtonVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement getStartedBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(), 'Get Started')]")));

            Assert.assertTrue(getStartedBtn.isDisplayed(), "'Get Started' button is not visible.");
            Point location = getStartedBtn.getLocation();
            System.out.println("TC_124 - 'Get Started' button is visible at position: " + location);
        } catch (Exception e) {
            takeScreenshot("TC_124_Failed");
            Assert.fail("TC_124 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_125_verifyGetStartedButtonStylingAndHoverEffect() {
        try {
            driver.get(baseUrl);
            WebElement getStartedBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Get Started')]")));

            String initialColor = getStartedBtn.getCssValue("color");
            String initialBg = getStartedBtn.getCssValue("background-color");

            // Hover effect
            String hoverScript = "var evt = new MouseEvent('mouseover', { bubbles: true }); arguments[0].dispatchEvent(evt);";
            ((JavascriptExecutor) driver).executeScript(hoverScript, getStartedBtn);
            Thread.sleep(1000);

            String hoverColor = getStartedBtn.getCssValue("color");
            String hoverBg = getStartedBtn.getCssValue("background-color");

            Assert.assertNotEquals(initialColor, hoverColor, "Text color did not change on hover.");
            Assert.assertNotEquals(initialBg, hoverBg, "Background color did not change on hover.");

            System.out.println("TC_125 - 'Get Started' button hover effect verified.");
        } catch (Exception e) {
            takeScreenshot("TC_125_Failed");
            Assert.fail("TC_125 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_126_verifyGetStartedButtonFontColorAndSize() {
        try {
            driver.get(baseUrl);
            WebElement getStartedBtn = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Get Started')]")));

            String font = getStartedBtn.getCssValue("font-family");
            String fontSize = getStartedBtn.getCssValue("font-size");
            String color = getStartedBtn.getCssValue("color");

            Assert.assertNotNull(font, "Font not set.");
            Assert.assertTrue(fontSize.endsWith("px"), "Font size is not in px.");
            Assert.assertTrue(color.contains("rgb"), "Font color is not in RGB format.");

            System.out.println("TC_126 - Font: " + font + ", Size: " + fontSize + ", Color: " + color);
        } catch (Exception e) {
            takeScreenshot("TC_126_Failed");
            Assert.fail("TC_126 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_127_verifyGetStartedButtonResponsiveness() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement getStartedBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(), 'Get Started')]")));

            boolean visibleAtDesktop = getStartedBtn.isDisplayed();

            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile size
            Thread.sleep(1000);

            getStartedBtn = driver.findElement(By.xpath("//a[contains(text(), 'Get Started')]"));
            boolean visibleAtMobile = getStartedBtn.isDisplayed();

            Assert.assertTrue(visibleAtDesktop && visibleAtMobile, "'Get Started' button is not responsive.");

            System.out.println("TC_127 - 'Get Started' button responsive on desktop and mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_127_Failed");
            Assert.fail("TC_127 Failed: " + e.getMessage());
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
