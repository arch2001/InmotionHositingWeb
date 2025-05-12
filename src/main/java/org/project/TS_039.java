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
import java.util.List;

public class TS_039 extends BaseClass {

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
    public void tc_140_verifySelectPlanNavigation() {
        try {
            driver.get(baseUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Locate the "Select Plan" buttons
            List<WebElement> selectButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//a[contains(text(),'Select Plan') or contains(text(),'Select')]")));

            Assert.assertTrue(selectButtons.size() > 0, "No 'Select Plan' buttons found.");

            // Click the first "Select Plan" button
            WebElement firstPlanButton = selectButtons.get(0);
            String originalWindow = driver.getWindowHandle();

            firstPlanButton.click();

            // Wait for the new window to be opened
            WebDriverWait windowWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            windowWait.until(ExpectedConditions.numberOfWindowsToBe(2));  // Wait until 2 windows are open

            // Switch to the new window
            for (String win : driver.getWindowHandles()) {
                if (!win.equals(originalWindow)) {
                    driver.switchTo().window(win);
                    break;
                }
            }

            // Wait for the URL to contain expected keywords (ensure page is loaded)
            WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            urlWait.until(ExpectedConditions.urlContains("sign-up"));
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("sign-up") || currentUrl.contains("cart"),
                    "Did not navigate to the expected signup/cart page. URL: " + currentUrl);

            System.out.println("TC_140 - Navigation to signup page successful: " + currentUrl);
        } catch (Exception e) {
            takeScreenshot("TC_140_Failed");
            Assert.fail("TC_140 Failed: " + e.getMessage());
        }
    }


    @Test(priority = 2)
    public void tc_141_verifyPlanPreselection() {
        try {
            // Assumes you're already on the signup/cart page
            WebElement selectedPlan = driver.findElement(By.cssSelector(".selected-plan, .plan-summary"));

            Assert.assertTrue(selectedPlan.isDisplayed(), "Selected plan not visible on signup page.");
            String planText = selectedPlan.getText();
            Assert.assertFalse(planText.isEmpty(), "Plan details are empty.");

            System.out.println("TC_141 - Pre-selected plan: " + planText);
        } catch (Exception e) {
            takeScreenshot("TC_141_Failed");
            Assert.fail("TC_141 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_142_verifySelectPlanButtonClickability() {
        try {
            driver.get(baseUrl);
            List<WebElement> selectButtons = driver.findElements(
                    By.xpath("//a[contains(text(),'Select Plan') or contains(text(),'Select')]"));

            for (WebElement btn : selectButtons) {
                Assert.assertTrue(btn.isDisplayed() && btn.isEnabled(), "'Select Plan' button is not clickable.");
                System.out.println("Button clickable: " + btn.getText());
            }

            System.out.println("TC_142 - All 'Select Plan' buttons are clickable.");
        } catch (Exception e) {
            takeScreenshot("TC_142_Failed");
            Assert.fail("TC_142 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_143_verifyMobileButtonBehavior() {
        try {
            driver.manage().window().setSize(new Dimension(375, 667)); // Mobile size
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> selectButtons = driver.findElements(
                    By.xpath("//a[contains(text(),'Select Plan') or contains(text(),'Select')]"));

            Assert.assertTrue(selectButtons.size() > 0, "No 'Select Plan' buttons visible on mobile view.");

            for (WebElement btn : selectButtons) {
                Assert.assertTrue(btn.isDisplayed(), "Button not visible in mobile view.");
            }

            System.out.println("TC_143 - 'Select Plan' buttons are visible and functional on mobile.");
        } catch (Exception e) {
            takeScreenshot("TC_143_Failed");
            Assert.fail("TC_143 Failed: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // Reset for further tests
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
