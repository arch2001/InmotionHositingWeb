package org.project;

import org.apache.commons.io.FileUtils;
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
import java.util.List;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;

public class TS_014 extends BaseClass{

    private WebDriver driver;
    private String baseUrl = "https://www.inmotionhosting.com/";
    private String screenshotDirectory = "Screenshots/";

    @BeforeClass
    public void setUp() {
       // Replace with your chromedriver path
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        File dir = new File(screenshotDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test(priority = 1)
    public void tc_060_testLanguageDropdownMultipleOptions() {
        // Verify that the dropdown displays multiple language options
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-selector"));
            languageDropdown.click(); // Open the dropdown

            List<WebElement> languageOptions = driver.findElements(By.cssSelector("#language-menu-default li")); // Corrected selector
            Assert.assertTrue(languageOptions.size() > 1, "Language dropdown does not display multiple options.");
            System.out.println("Test Passed: Language dropdown displays multiple options.");
        } catch (Exception e) {
            takeScreenshot("LanguageDropdownMultipleOptions_Failed");
            Assert.fail("Test Failed: Language dropdown does not display multiple options: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void tc_061_testLanguageSelectionUpdatesContent() {
        // Verify that selecting a different language updates the page content
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-switcher-control"));
            languageDropdown.click(); // Open dropdown

            Select select = new Select(languageDropdown);
            select.selectByVisibleText("Español"); // Select Spanish.  Adjust as needed.

            //  Add a wait here if the page takes time to update.  Important!
            Thread.sleep(2000); //  Simplest wait, but use WebDriverWait for robustness

            // Verify that some text on the page has changed to Spanish.  This is highly site-specific!
            WebElement body = driver.findElement(By.tagName("body")); //get the body
            String pageText = body.getText();
            Assert.assertTrue(pageText.contains("Bienvenidos"), "Page content did not update to Spanish.");
            System.out.println("Test Passed: Selecting a different language updates the page content.");
        } catch (Exception e) {
            takeScreenshot("LanguageSelectionUpdatesContent_Failed");
            Assert.fail("Test Failed: Selecting a different language does not update the page content: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void tc_062_testDefaultLanguageSelection() {
        // Verify the default language selection
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-switcher-control"));
            String defaultLanguage = languageDropdown.getText(); //gets the selected text.
            Assert.assertTrue(defaultLanguage.contains("English"), "Default language is not English.");
            System.out.println("Test Passed: Default language is English.");
        } catch (Exception e) {
            takeScreenshot("DefaultLanguageSelection_Failed");
            Assert.fail("Test Failed: Default language is not English: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void tc_063_testKeyboardNavigationSupport() {
        // Verify keyboard navigation support for the dropdown
        try {
            driver.get(baseUrl);
            WebElement languageDropdown = driver.findElement(By.id("language-switcher-control"));
            languageDropdown.click(); //open
            languageDropdown.sendKeys(Keys.ARROW_DOWN);

            Select select = new Select(languageDropdown);
            String selectedLanguage = select.getFirstSelectedOption().getText();
            Assert.assertNotEquals(selectedLanguage, "English", "Arrow down key did not navigate the dropdown.");
            System.out.println("Test Passed: Keyboard navigation is supported.");
        } catch (Exception e) {
            takeScreenshot("KeyboardNavigationSupport_Failed");
            Assert.fail("Test Failed: Keyboard navigation is not supported: " + e.getMessage());
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
