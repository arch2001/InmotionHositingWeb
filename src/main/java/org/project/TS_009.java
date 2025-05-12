package org.project;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class TS_009 extends BaseClass {

    //  ***CRITICAL:  Replace this with the *correct* locator for your page!***
    private By needHelpLinkLocator = By.id("needHelpLink");  //  <--  *INCORRECT LOCATOR - FIX THIS*
    //  Example 1:  If the link has a class:
    //  private By needHelpLinkLocator = By.cssSelector(".help-link");
    //  Example 2:  If the link is an anchor tag with specific text:
    // private By needHelpLinkLocator = By.linkText("Need Help for Login?");
    //  Example 3: If the element is inside a div and then an anchor tag
    // private By needHelpLinkLocator = By.xpath("//div[@class='your-div-class']/a[contains(text(),'Need Help')]");

    @BeforeClass
    public static void browserOpen() {
        System.out.println("BeforeClass: Launching browser");
        Date d = new Date();
        System.out.println("Test started at: " + d);
    }

    @BeforeMethod
    public void beforeTestCase() {
        launchBrowser();
        windowMaximize();
        launchUrl("https://secure1.inmotionhosting.com/index/login");
    }

    @Test
    public void tc_040_verifyNeedHelpLinkVisibility() {
        // TC_040 - Verify the "Need Help for Login?" link is visible
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            Assert.assertTrue(needHelpLink.isDisplayed(), "Need Help Link is not visible.");
            System.out.println("TC_040 Passed: Need Help Link is visible on the page");
        } catch (Exception e) {
            System.out.println("TC_040 Failed: " + e.getMessage());
            takeScreenshot("TC_040");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_041_verifyNeedHelpLinkAlignmentAndPlacement() {
        // TC_041 - Verify the alignment and placement of the "Need Help for Login?" link
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            WebElement parentElement = needHelpLink.findElement(By.xpath(".."));

            int linkX = needHelpLink.getLocation().getX();
            int linkY = needHelpLink.getLocation().getY();
            int linkWidth = needHelpLink.getSize().getWidth();
            int linkHeight = needHelpLink.getSize().getHeight();
            int parentX = parentElement.getLocation().getX();
            int parentY = parentElement.getLocation().getY();
            int parentWidth = parentElement.getSize().getWidth();
            int parentHeight = parentElement.getSize().getHeight();

            Assert.assertTrue(linkX >= parentX && (linkX + linkWidth) <= (parentX + parentWidth),
                    "Need Help Link is not horizontally within its parent.");
            Assert.assertTrue(linkY >= parentY, "Need Help Link is not vertically below the top of its parent.");
            System.out.println("TC_041 Passed: Need Help Link alignment and placement verified");
        } catch (Exception e) {
            System.out.println("TC_041 Failed: " + e.getMessage());
            takeScreenshot("TC_041");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_042_verifyNeedHelpLinkFontAndColor() {
        // TC_042 - Verify the font, size, and color of the "Need Help for Login?" link
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            String fontFamily = needHelpLink.getCssValue("font-family");
            String fontSize = needHelpLink.getCssValue("font-size");
            String color = needHelpLink.getCssValue("color");

            Assert.assertTrue(fontFamily.contains("Arial"), "Font family is not Arial.  Actual: " + fontFamily);
            Assert.assertTrue(Integer.parseInt(fontSize.replace("px", "").trim()) >= 12, "Font size is less than 12px. Actual: " + fontSize);
            Assert.assertEquals(color, "rgba(0, 0, 255, 1)", "Link color is not blue. Actual: " + color);
            System.out.println("TC_042 Passed: Need Help Link font, size, and color verified");
        } catch (Exception e) {
            System.out.println("TC_042 Failed: " + e.getMessage());
            takeScreenshot("TC_042");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_043_verifyNeedHelpLinkHoverEffect() {
        // TC_043 - Verify the hover effect of the "Need Help for Login?" link
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            Actions actions = new Actions(driver);

            String initialCursor = needHelpLink.getCssValue("cursor");
            actions.moveToElement(needHelpLink).perform();
            String hoverCursor = needHelpLink.getCssValue("cursor");

            Assert.assertEquals(hoverCursor, "pointer", "Cursor should change to 'pointer' on hover. Initial cursor: " + initialCursor + ", Hover cursor: " + hoverCursor);
            System.out.println("TC_043 Passed: Need Help Link hover effect verified");
        } catch (Exception e) {
            System.out.println("TC_043 Failed: " + e.getMessage());
            takeScreenshot("TC_043");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void tc_044_verifyNeedHelpLinkAccessibility() {
        // TC_044 - Verify the accessibility of the "Need Help for Login?" link
        try {
            WebElement needHelpLink = driver.findElement(needHelpLinkLocator);
            needHelpLink.sendKeys("\t");
            String outline = needHelpLink.getCssValue("outline");
            Assert.assertTrue(!outline.equals("none"), "Link should have an outline when focused via tab. Actual outline: " + outline);
            System.out.println("TC_044 Passed: Need Help Link accessibility verified");
        } catch (Exception e) {
            System.out.println("TC_044 Failed: " + e.getMessage());
            takeScreenshot("TC_044");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    private void takeScreenshot(String testCaseName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File("./Screenshots/" + testCaseName + ".png");
        try {
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken for: " + testCaseName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterTestCase() {
        closeEntireBrowser();
    }
}
