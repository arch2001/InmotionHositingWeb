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

public class TS_047 extends BaseClass {

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
    public void tc_169_verifyFooterSectionsVisibility() {
        try {
            // Navigate to the base URL
            driver.get(baseUrl);

            // Wait until the footer section is visible
            WebElement footerSection = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer")));
            
            // Assert that the footer section is visible
            Assert.assertTrue(footerSection.isDisplayed(), "Footer section is not visible.");

            // Verify that all the footer sections are visible
            List<WebElement> footerSections = driver.findElements(By.xpath("//footer//section"));
            Assert.assertTrue(footerSections.size() > 0, "No footer sections found.");

            // Log the success message
            System.out.println("TC_169 - Footer sections are visible.");

        } catch (Exception e) {
            // Take screenshot on failure (you need to implement this method)
            takeScreenshot("TC_169_Failed");
            
            // Print stack trace and fail the test
            e.printStackTrace();
            Assert.fail("TC_169 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_170_verifyTextAlignmentAndSpacing() {
        try {
            driver.get(baseUrl);
            WebElement footerSection = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer")));

            // Verify text alignment and spacing for footer sections
            List<WebElement> footerTextElements = driver.findElements(By.xpath("//footer//p | //footer//h3 | //footer//a"));
            for (WebElement textElement : footerTextElements) {
                String alignment = textElement.getCssValue("text-align");
                String margin = textElement.getCssValue("margin");

                Assert.assertTrue(alignment.equals("left") || alignment.equals("center") || alignment.equals("right"),
                        "Text alignment is not as expected.");
                Assert.assertTrue(!margin.equals("0px"), "Text margin or spacing is inconsistent.");

                System.out.println("TC_170 - Footer text element with alignment: " + alignment + " and margin: " + margin);
            }
        } catch (Exception e) {
            takeScreenshot("TC_170_Failed");
            Assert.fail("TC_170 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_171_verifyPhoneNumberIcons() {
        try {
            driver.get(baseUrl);
            List<WebElement> phoneIcons = driver.findElements(By.xpath("//footer//a[contains(@href, 'tel:')]"));
            Assert.assertTrue(phoneIcons.size() > 0, "Phone number icons not found in the footer.");

            for (WebElement phoneIcon : phoneIcons) {
                Assert.assertTrue(phoneIcon.isDisplayed(), "Phone number icon is not visible.");
                String iconClass = phoneIcon.getAttribute("class");
                Assert.assertTrue(iconClass.contains("phone") || iconClass.contains("icon"), "Phone icon class is incorrect.");
                System.out.println("TC_171 - Phone number icon is visible.");
            }
        } catch (Exception e) {
            takeScreenshot("TC_171_Failed");
            Assert.fail("TC_171 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_172_verifyIconVisibility() {
        try {
            driver.get(baseUrl);
            // Verify if all social media icons in the footer are visible
            List<WebElement> socialIcons = driver.findElements(By.xpath("//footer//i[contains(@class, 'icon')]"));
            Assert.assertTrue(socialIcons.size() > 0, "No social media icons found in the footer.");

            for (WebElement icon : socialIcons) {
                Assert.assertTrue(icon.isDisplayed(), "Icon is not visible.");
                System.out.println("TC_172 - Social media icon is visible.");
            }
        } catch (Exception e) {
            takeScreenshot("TC_172_Failed");
            Assert.fail("TC_172 Failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void tc_173_verifyFontConsistency() {
        try {
            driver.get(baseUrl);
            // Verify font consistency across footer elements
            List<WebElement> footerTextElements = driver.findElements(By.xpath("//footer//p | //footer//h3 | //footer//a"));

            String fontFamily = footerTextElements.get(0).getCssValue("font-family");
            String fontSize = footerTextElements.get(0).getCssValue("font-size");
            String fontWeight = footerTextElements.get(0).getCssValue("font-weight");

            for (WebElement textElement : footerTextElements) {
                Assert.assertEquals(textElement.getCssValue("font-family"), fontFamily, "Font family inconsistency in footer.");
                Assert.assertEquals(textElement.getCssValue("font-size"), fontSize, "Font size inconsistency in footer.");
                Assert.assertEquals(textElement.getCssValue("font-weight"), fontWeight, "Font weight inconsistency in footer.");
            }

            System.out.println("TC_173 - Font consistency verified in footer.");
        } catch (Exception e) {
            takeScreenshot("TC_173_Failed");
            Assert.fail("TC_173 Failed: " + e.getMessage());
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
