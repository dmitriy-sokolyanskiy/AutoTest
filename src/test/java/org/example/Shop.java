package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;


public class Shop {
    private WebDriver driver;

    private static final String baseUrl = "https://homestory.com.ua/";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--silent");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(20));
        this.driver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() { driver.get(baseUrl); }

    @AfterClass(alwaysRun = true)
    public void tearDown() { driver.quit(); }

    @Test
    public void testClickOnAboutUsTab() {
        WebElement aboutTab = driver.findElement(By.cssSelector("a.site-menu__link"));
        Assert.assertNotNull(aboutTab);
        aboutTab.click();
        System.out.println("testClickOnAboutUsTab ");
        Assert.assertNotEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchField() {
        String query = "Kansas";
        WebElement searchField = driver.findElement(By.xpath("//input[@placeholder='пошук товарів']"));
        Assert.assertNotNull(searchField);
        searchField.sendKeys(query);
    }
}