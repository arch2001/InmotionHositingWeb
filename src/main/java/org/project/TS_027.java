package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TS_027 extends BaseClass {

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
    public void tc_095_verifyDomainsLinkVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebElement domainsLink = driver.findElement(By.linkText("Domains"));
            Assert.assertTrue(domainsLink.isDisplayed(), "Domains link is not visible.");
            Point location = domainsLink.getLocation();
            Assert.assertTrue(location.getY() < 500, "Domains link may not be in expected header position.");

            System.out.println("TC_095 - Domains link is visible and placed correctly.");
        } catch (Exception e) {
            takeScreenshot("TC_095_Failed");
            Assert.fail("TC_095 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_096_verifyDomainsLinkStylingAndHover() {
        try {
            WebElement domainsLink = driver.findElement(By.linkText("Domains"));

            String initialColor = domainsLink.getCssValue("color");

            Actions action = new Actions(driver);
            action.moveToElement(domainsLink).perform();
            Thread.sleep(1000);

            String hoverColor = domainsLink.getCssValue("color");
            Assert.assertNotEquals(hoverColor, initialColor, "Hover color did not change.");

            System.out.println("TC_096 - Styling and hover effect verified.");
        } catch (Exception e) {
            takeScreenshot("TC_096_Failed");
            Assert.fail("TC_096 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_097_verifyDomainsLinkInMobileView() {
        try {
            Dimension mobileSize = new Dimension(375, 812); // iPhone X size
            driver.manage().window().setSize(mobileSize);

            driver.navigate().refresh();
            Thread.sleep(2000);

            // Attempt to open mobile menu if needed
            try {
                WebElement hamburger = driver.findElement(By.cssSelector(".hamburger-icon, .menu-toggle, button[aria-label='Open Menu']"));
                if (hamburger.isDisplayed()) hamburger.click();
                Thread.sleep(1000);
            } catch (NoSuchElementException ignored) {}

            WebElement domainsLink = driver.findElement(By.linkText("Domains"));
            Assert.assertTrue(domainsLink.isDisplayed(), "Domains link not visible in mobile view.");

            System.out.println("TC_097 - Domains link is visible and accessible in mobile view.");
        } catch (Exception e) {
            takeScreenshot("TC_097_Failed");
            Assert.fail("TC_097 Failed: " + e.getMessage());
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
