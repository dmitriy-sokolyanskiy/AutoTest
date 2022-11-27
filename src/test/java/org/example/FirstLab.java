package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;


public class FirstLab {
    private WebDriver driver;

    private static final String baseUrl = "https://www.nmu.org.ua/ua";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.driver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        // open main page
        driver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() { driver.quit(); }

    @Test
    public void testHeaderExists() {
        WebElement header = driver.findElement(By.id("heder"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudent() {
        WebElement forStudentButton = driver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));
        Assert.assertNotNull(forStudentButton);
        // verification page changed
        forStudentButton.click();
        Assert.assertNotEquals(driver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnStudentPage() {
        String studentPageUrl = "/content/student_life/students/";
        driver.get(baseUrl + studentPageUrl);
        WebElement searchField = driver.findElement(By.tagName("input"));
        // different params of searchField
        System.out.println(String.format("Name attribute: %s", driver.getCurrentUrl()) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition attribute: %dx%d", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize attribute: %dx%d", searchField.getSize().height, searchField.getSize().width)
        );
        // input value
        String inputValue = "I need info";
        searchField.sendKeys(inputValue);
        // verification test
        Assert.assertEquals(searchField.getText(), inputValue);
        // click Enter
        searchField.sendKeys(Keys.ENTER);
        // verification page changed
        Assert.assertNotEquals(driver.getCurrentUrl(), studentPageUrl);
    }

    @Test
    public void testSlider() {
        // find element by class name
        WebElement nextButton = driver.findElement(By.className("next"));
        // find element by css selector
        WebElement nextButtonByCss = driver.findElement(By.cssSelector("a.next"));
        // verification equality
        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousButton = driver.findElement(By.className("prev"));

        for (int i = 0; i < 20; i++) {
            if (nextButton.getAttribute("class").contains("disabled")) {
                previousButton.click();
                Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            } else {
                nextButton.click();
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            }
        }
    }

    @Test
    public void testCustomPage() {
        String customUrl = "https://hulkshop.com.ua/";
        driver.get(customUrl);
    }
}