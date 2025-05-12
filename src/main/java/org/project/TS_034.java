package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TS_034 extends BaseClass {

    private WebDriver driver;
    private final String baseUrl = "https://www.inmotionhosting.com/";
    private final String loginPageUrl = "https://www.inmotionhosting.com/login"; // Assuming this is the login page URL
    private final String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
      
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) dir.mkdirs();
    }

    @Test(priority = 1)
    public void tc_120_verifyLoginButtonRedirectsToLoginPage() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            loginBtn.click();

            // Wait for the login page to load and verify the URL
            wait.until(ExpectedConditions.urlToBe(loginPageUrl));

            Assert.assertEquals(driver.getCurrentUrl(), loginPageUrl, "Login button did not redirect to the login page.");

            System.out.println("TC_120 - Login button redirects to the login page successfully.");
        } catch (Exception e) {
            takeScreenshot("TC_120_Failed");
            Assert.fail("TC_120 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_121_verifyLoginPageLoadsCompletely() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            loginBtn.click();

            // Wait for the login page to load completely
            WebElement loginPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(), 'Login')]")));  // Assuming there's a login header

            Assert.assertNotNull(loginPage, "Login page did not load completely.");

            System.out.println("TC_121 - Login page loaded completely.");
        } catch (Exception e) {
            takeScreenshot("TC_121_Failed");
            Assert.fail("TC_121 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_122_verifyLoginButtonKeyboardAccessibility() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            // Use keyboard navigation to focus on the login button
            loginBtn.sendKeys(Keys.TAB); // Tab to focus the button
            Assert.assertTrue(loginBtn.equals(driver.switchTo().activeElement()), "Login button is not focused via keyboard navigation.");

            // Simulate pressing Enter to click the button
            loginBtn.sendKeys(Keys.ENTER);

            WebElement loginPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(), 'Login')]"))); // Assuming there's a login header
            Assert.assertNotNull(loginPage, "Login page did not load after keyboard navigation.");

            System.out.println("TC_122 - Login button is accessible via keyboard navigation.");
        } catch (Exception e) {
            takeScreenshot("TC_122_Failed");
            Assert.fail("TC_122 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_123_verifyLoginButtonBehaviorOnMobileDevices() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]")));

            Dimension desktopSize = driver.manage().window().getSize();
            boolean visibleAtDesktop = loginBtn.isDisplayed();

            // Simulate mobile viewport (iPhone 6/7/8 size)
            driver.manage().window().setSize(new Dimension(375, 667));
            Thread.sleep(1000);

            loginBtn = driver.findElement(By.xpath("//a[contains(@href, 'login') and contains(text(), 'Login')]"));
            boolean visibleAtMobile = loginBtn.isDisplayed();

            Assert.assertTrue(visibleAtDesktop && visibleAtMobile, "Login button is not responsive or behaves differently on mobile.");

            System.out.println("TC_123 - Login button behavior on mobile devices is verified.");
        } catch (Exception e) {
            takeScreenshot("TC_123_Failed");
            Assert.fail("TC_123 Failed: " + e.getMessage());
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
