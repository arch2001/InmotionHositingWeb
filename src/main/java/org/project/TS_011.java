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

public class TS_011 extends BaseClass {

    private WebDriver driver;
    private String baseUrl;
    private String screenshotDirectory = "./Screenshots/";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://www.inmotionhosting.com/";
        driver.manage().window().maximize();
           // Create the screenshot directory if it doesn't exist
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_049_testLogoVisibility() {
        try{
        driver.get(baseUrl);
        WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));
        Assert.assertTrue(logo.isDisplayed(), "Logo is not visible on the page.");
        System.out.println("TC_049 - Logo visibility test passed.");
        } catch (Exception e) {
            takeScreenshot("TC_049_Failed");
            Assert.fail("TC_049 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_050_testLogoAlignment() {
        try{
        WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));
        WebElement header = driver.findElement(By.tagName("header"));

        int logoX = logo.getLocation().getX();
        int headerX = header.getLocation().getX();

        // Basic check to see if the logo starts within the header's horizontal bounds
        Assert.assertTrue(logoX >= headerX, "Logo is not aligned to the left within the header (basic check).");
        System.out.println("TC_050 - Logo alignment test passed (basic left alignment check).");

        // More advanced alignment checks might involve comparing CSS properties
        // For example:
        // String logoFloat = logo.getCssValue("float");
        // Assert.assertEquals(logoFloat, "left", "Logo is not floated left.");
        }  catch (Exception e) {
            takeScreenshot("TC_050_Failed");
            Assert.fail("TC_050 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_051_testLogoImageSourcePresence() {
        try{
        WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));
        String logoSrc = logo.getAttribute("src");
        Assert.assertNotNull(logoSrc, "Logo source attribute is missing.");
        Assert.assertFalse(logoSrc.isEmpty(), "Logo source attribute is empty.");
        System.out.println("TC_051 - Logo image source presence test passed.");
        }  catch (Exception e) {
            takeScreenshot("TC_051_Failed");
            Assert.fail("TC_051 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_052_testLogoHoverBehavior() {
        try{
        WebElement logo = driver.findElement(By.xpath("//img[@alt='InMotion Hosting Logo']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(logo).perform();
        // You would need to manually observe if there's a visual change (e.g., cursor change, tooltip).
        // Automation of cursor change detection is complex and browser-specific.
        System.out.println("TC_052 - Manually verify the hover behavior of the logo (check for cursor change or tooltip).");
        // Further automation might involve checking for changes in CSS properties on hover, if applicable.
        }  catch (Exception e) {
            takeScreenshot("TC_052_Failed");
            Assert.fail("TC_052 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void tc_053_testLogoKeyboardNavigation() {
        try{
        driver.findElement(By.tagName("body")).sendKeys("\t"); // Tab to the first focusable element
        // You would need to manually continue tabbing and observe if the logo receives focus.
        // Focus is usually indicated by a visual cue like a border or highlight.
        System.out.println("TC_053 - Manually verify keyboard navigation to the logo (check for focus on tab).");
        // For more robust accessibility testing, consider using specialized tools.
        } catch (Exception e) {
            takeScreenshot("TC_053_Failed");
            Assert.fail("TC_053 Failed: " + e.getMessage());
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
