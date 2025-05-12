package org.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TS_029 extends BaseClass {

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
    public void tc_101_verifyCompanyDropdownVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebElement companyDropdown = driver.findElement(By.xpath("//li[@id='menu-item-10530']/a"));
            Assert.assertTrue(companyDropdown.isDisplayed(), "'Company' dropdown menu is not visible.");

            Point location = companyDropdown.getLocation();
            Assert.assertTrue(location.getX() > 0 && location.getY() > 0,
                    "'Company' dropdown menu is not placed correctly (off-screen). Location: " + location);

            System.out.println("TC_101 - 'Company' dropdown menu is visible and placed correctly at: " + location);
        } catch (NoSuchElementException e) {
            takeScreenshot("TC_101_Failed_ElementNotFound");
            Assert.fail("TC_101 Failed: 'Company' dropdown element not found - " + e.getMessage());
        } catch (AssertionError e) {
            takeScreenshot("TC_101_Failed_Assertion");
            Assert.fail("TC_101 Failed: " + e.getMessage());
        } catch (Exception e) {
            takeScreenshot("TC_101_Failed_General");
            Assert.fail("TC_101 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_102_verifyCompanyDropdownStylingAndHoverEffect() {
        try {
            driver.get(baseUrl);
            WebElement companyLink = driver.findElement(By.xpath("//li[@id='menu-item-10530']/a"));
            String initialBackgroundColor = companyLink.getCssValue("background-color");
            String initialTextColor = companyLink.getCssValue("color");
            String initialFontWeight = companyLink.getCssValue("font-weight");

            Actions action = new Actions(driver);
            action.moveToElement(companyLink).perform();
            Thread.sleep(500); // Allow time for hover effect

            String hoverBackgroundColor = companyLink.getCssValue("background-color");
            String hoverTextColor = companyLink.getCssValue("color");
            String hoverFontWeight = companyLink.getCssValue("font-weight");

            Assert.assertNotEquals(initialBackgroundColor, hoverBackgroundColor,
                    "'Company' dropdown background color did not change on hover.");
            Assert.assertNotEquals(initialTextColor, hoverTextColor,
                    "'Company' dropdown text color did not change on hover.");
            // You can add more assertions for font-weight or other style changes

            System.out.println("TC_102 - 'Company' dropdown styling and hover effect verified. Initial BG: " + initialBackgroundColor + ", Hover BG: " + hoverBackgroundColor + ". Initial Text Color: " + initialTextColor + ", Hover Text Color: " + hoverTextColor + ". Initial Font Weight: " + initialFontWeight + ", Hover Font Weight: " + hoverFontWeight + ".");
        } catch (NoSuchElementException e) {
            takeScreenshot("TC_102_Failed_ElementNotFound");
            Assert.fail("TC_102 Failed: 'Company' dropdown element not found - " + e.getMessage());
        } catch (AssertionError e) {
            takeScreenshot("TC_102_Failed_Assertion");
            Assert.fail("TC_102 Failed: " + e.getMessage());
        } catch (Exception e) {
            takeScreenshot("TC_102_Failed_General");
            Assert.fail("TC_102 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_103_verifyCompanyDropdownResponsivenessMobileView() {
        try {
            // Simulate a mobile screen size
            driver.manage().window().setSize(new Dimension(480, 800));
            driver.get(baseUrl);

            // You might need to interact with a mobile menu button to reveal the "Company" dropdown
            WebElement mobileMenuButton = driver.findElement(By.xpath("//button[contains(@class, 'navbar-toggle')]"));
            mobileMenuButton.click();
            Thread.sleep(1000); // Wait for the menu to open

            WebElement companyDropdownMobile = driver.findElement(By.xpath("//ul[@id='primary-menu']/li[@id='menu-item-10530']/a"));
            Assert.assertTrue(companyDropdownMobile.isDisplayed(), "'Company' dropdown is not visible in mobile view.");

            // Optionally, you can check the placement or styling in mobile view
            Point locationMobile = companyDropdownMobile.getLocation();
            Assert.assertTrue(locationMobile.getX() >= 0 && locationMobile.getX() < 480,
                    "'Company' dropdown is not within the mobile screen width. Location X: " + locationMobile.getX());

            System.out.println("TC_103 - 'Company' dropdown is visible and positioned correctly in mobile view at: " + locationMobile);

            // Reset the browser size
            driver.manage().window().maximize();

        } catch (NoSuchElementException e) {
            takeScreenshot("TC_103_Failed_ElementNotFound");
            Assert.fail("TC_103 Failed: Could not find 'Company' dropdown or mobile menu elements - " + e.getMessage());
        } catch (AssertionError e) {
            takeScreenshot("TC_103_Failed_Assertion");
            Assert.fail("TC_103 Failed: " + e.getMessage());
        } catch (Exception e) {
            takeScreenshot("TC_103_Failed_General");
            Assert.fail("TC_103 Failed: " + e.getMessage());
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