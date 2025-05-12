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

public class TS_026 extends BaseClass {

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
    public void tc_092_testDropdownRevealsOnHoverOrClick() {
        try {
            driver.get(baseUrl);
            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));

            // Try hover
            Actions action = new Actions(driver);
            action.moveToElement(menu).perform();

            Thread.sleep(1000); // Allow dropdown to appear
            List<WebElement> dropdownItems = driver.findElements(By.cssSelector("a[href*='dedicated']"));
            Assert.assertTrue(dropdownItems.size() > 1, "Dropdown options not revealed on hover.");

            System.out.println("TC_092 - Dropdown options are visible on hover.");
        } catch (Exception e) {
            takeScreenshot("TC_092_Failed");
            Assert.fail("TC_092 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_093_testRedirectionAfterDropdownSelection() {
        try {
            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));
            Actions action = new Actions(driver);
            action.moveToElement(menu).perform();

            Thread.sleep(1000);
            WebElement firstOption = driver.findElement(By.cssSelector("a[href*='dedicated']"));
            String expectedUrl = firstOption.getAttribute("href");

            firstOption.click();
            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains(expectedUrl), "Redirection failed after selecting dropdown option.");

            System.out.println("TC_093 - Redirected to: " + currentUrl);
        } catch (Exception e) {
            takeScreenshot("TC_093_Failed");
            Assert.fail("TC_093 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_094_testDropdownCollapsesAfterSelectionOrOutsideClick() {
        try {
            driver.get(baseUrl);
            WebElement menu = driver.findElement(By.linkText("Dedicated Servers"));

            Actions action = new Actions(driver);
            action.moveToElement(menu).perform();

            Thread.sleep(1000);
            WebElement outside = driver.findElement(By.tagName("body"));
            outside.click();

            Thread.sleep(1000);
            List<WebElement> dropdownItems = driver.findElements(By.cssSelector("a[href*='dedicated']"));

            boolean anyVisible = dropdownItems.stream().anyMatch(WebElement::isDisplayed);
            Assert.assertFalse(anyVisible, "Dropdown did not collapse after outside click.");

            System.out.println("TC_094 - Dropdown collapsed successfully after clicking outside.");
        } catch (Exception e) {
            takeScreenshot("TC_094_Failed");
            Assert.fail("TC_094 Failed: " + e.getMessage());
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
