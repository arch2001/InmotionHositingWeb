package org.project;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TS_019 extends BaseClass {

    private WebDriver driver;
    private String baseUrl;
    private String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://www.inmotionhosting.com/"; // Or the actual base URL
        driver.manage().window().maximize();
        // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_071_testWebHostingDropdownVisibilityAndPlacement() {
        try {
            driver.get(baseUrl);
            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            WebElement header = driver.findElement(By.tagName("header"));

            Assert.assertTrue(webHostingLink.isDisplayed(), "\"Web Hosting\" link is not visible.");

            int webHostingX = webHostingLink.getLocation().getX();
            int webHostingY = webHostingLink.getLocation().getY();
            int headerX = header.getLocation().getX();
            int headerY = header.getLocation().getY();

            // Basic placement check:  Link should be within header bounds.
            Assert.assertTrue(webHostingX >= headerX, "\"Web Hosting\" link is not within the header's horizontal bounds.");
            Assert.assertTrue(webHostingY >= headerY, "\"Web Hosting\" link is not within the header's vertical bounds.");
            System.out.println("TC_071 - \"Web Hosting\" dropdown visibility and placement test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_071_Failed");
            Assert.fail("TC_071 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_072_testWebHostingDropdownStylingAndHover() {
        try {
            driver.get(baseUrl);
            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(webHostingLink).perform();
            Thread.sleep(1000); // Add a short wait for the dropdown to appear.  Use WebDriverWait in real tests.

            WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class, 'dropdown-menu')]")); //  Corrected XPath
            Assert.assertTrue(dropdown.isDisplayed(), "\"Web Hosting\" dropdown is not displayed on hover.");

            // Example: Check background color of the dropdown
            String backgroundColor = dropdown.getCssValue("background-color");
            Assert.assertNotNull(backgroundColor, "Dropdown background color is null.");
            Assert.assertFalse(backgroundColor.isEmpty(), "Dropdown background color is empty.");

            // Further styling checks can be added here (e.g., font color, border, etc.)
            System.out.println("TC_072 - \"Web Hosting\" dropdown styling and hover behavior test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_072_Failed");
            Assert.fail("TC_072 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_073_testWebHostingDropdownResponsiveness() {
        try {
            driver.get(baseUrl);
            // This test requires you to simulate different screen sizes.
            // Selenium alone doesn't directly test *all* aspects of responsiveness.
            //  You'll likely need to combine this with manual testing and visual inspection.

            // Example:  Check how the menu behaves at a smaller screen size.
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024)); // Example: iPad size
            Thread.sleep(2000); // Allow time for re-rendering

            WebElement webHostingLink = driver.findElement(By.xpath("//a[contains(text(),'Web Hosting')]"));
            Assert.assertTrue(webHostingLink.isDisplayed(), "\"Web Hosting\" link is not visible at this size.");
            //  Add checks to see if the menu changes to a mobile-friendly format (e.g., hamburger menu).
            //  This will require inspecting the HTML/CSS for changes in element structure.

            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1280, 800)); // Restore to a larger size
            Thread.sleep(2000);

            System.out.println("TC_073 - \"Web Hosting\" dropdown responsiveness test passed (basic size check).  Further manual testing is needed.");
        } catch (Exception e) {
            takeScreenshot("TC_073_Failed");
            Assert.fail("TC_067 Failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testCaseName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(screenshotDirectory + testCaseName + ".png");
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken for: " + testCaseName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
}
