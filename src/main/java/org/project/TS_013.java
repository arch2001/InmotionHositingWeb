package org.project;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TS_013 extends BaseClass {

    private WebDriver driver;
    private String baseUrl = "https://www.inmotionhosting.com/";
    private String screenshotDirectory = "Screenshots/";

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
    public void tc_058_testLanguageDropdownVisibility() {
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-switcher-control"));

            Assert.assertTrue(languageDropdown.isDisplayed(), "Language dropdown is not visible");
            System.out.println("Test Passed: Language dropdown is visible");
        } catch (Exception e) {
            takeScreenshot("LanguageDropdownVisibility_Failed");
            Assert.fail("Test Failed: Language dropdown visibility test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_059_testLanguageDropdownAlignmentAndStyling() {
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-switcher-control"));

            // Verify alignment
            String textAlign = languageDropdown.getCssValue("text-align");
            Assert.assertEquals(textAlign, "left", "Text alignment is not left. Actual: " + textAlign);

            // Verify font properties
            String fontFamily = languageDropdown.getCssValue("font-family");
            String fontSize = languageDropdown.getCssValue("font-size");
            String color = languageDropdown.getCssValue("color");

            Assert.assertTrue(fontFamily.contains("Arial"), "Font family is not as expected.  Actual: " + fontFamily);
            Assert.assertEquals(fontSize, "16px", "Font size is not 16px. Actual: " + fontSize);
            Assert.assertEquals(color, "rgba(51, 51, 51, 1)", "Color is not as expected. Actual: " + color);
            System.out.println("Test Passed: Language dropdown Alignment and Styling are correct");

        } catch (Exception e) {
            takeScreenshot("LanguageDropdownAlignmentAndStyling_Failed");
            Assert.fail("Test Failed: Language dropdown Alignment and Styling test failed: " + e.getMessage());

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
