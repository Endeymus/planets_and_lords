package com.endeymus.planets;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SeleniumTest {

    private static WebDriver driver;
    private final static String URL_INDEX = "http://localhost:8080/";
    private final static String URL_LIST_LORDS = "http://localhost:8080/lords";
    private final static String URL_LORD_ADD = "http://localhost:8080/lords/add";
    private final static String URL_PLANET_ADD = "http://localhost:8080/planets/add";
    private final static String URL_LORD_APPOINT = "http://localhost:8080/lords/appoint";
    private final static String URL_PLANET_DELETE = "http://localhost:8080/planets/delete";
    private final static String URL_LORD_LOUNGER = "http://localhost:8080/lords/lounger";
    private final static String URL_LORD_YOUNG = "http://localhost:8080/lords/young";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    @DisplayName(value = "Должна открыться главная страница")
    void openIndexPage() {
        driver.get(URL_INDEX);
        WebElement pElement = new WebDriverWait(driver, Duration.ofSeconds(10).getSeconds())
                .until(driver -> driver.findElement(By.tagName("p")));
        assertEquals("Вселенная исследована и поделена.\n" +
                "Верховный правитель назначает Повелителей Планет, общее количество которых исчисляется миллионами.\n" +
                "Опытные Повелители могут одновременно управлять несколькими Планетами. Никакой демократии, поэтому одной планетой может править только один Повелитель.\n" +
                "Поэтому была создана система учета и надзора за Повелителями.", pElement.getText());
    }

    @Test
    @DisplayName(value = "Должна открыться страница со списком всех Повелителей")
    void testListOfLords() {
        driver.get(URL_LIST_LORDS);
        List<WebElement> elements = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver -> driver.findElements(By.tagName("tr")));
        assertNotNull(elements);
        WebElement tr = elements.get(2);

        List<WebElement> tds = tr.findElements(By.tagName("td"));
        assertAll("tds",
                ()-> assertNotNull(tds),
                ()-> assertEquals("1", tds.get(0).getText()),
                ()-> assertEquals("John Mayer", tds.get(1).getText()),
                ()-> assertEquals("345", tds.get(2).getText())
        );
    }

    @Test
    @DisplayName(value = "Должен добавить нового Повелителя")
    @Order(1)
    void addNewLord() {
        driver.get(URL_LORD_ADD);
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver-> driver.findElement(By.id("name")));

        element.sendKeys("Adam");
        driver.findElement(By.id("age")).sendKeys("100");
        driver.findElement(By.tagName("button")).click();


        element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver -> driver.findElement(By.tagName("p")));
        WebElement finalElement = element;
        assertAll("element",
                ()-> assertNotNull(finalElement),
                ()-> assertEquals("success", finalElement.getText())
                );
    }

    @Test
    @DisplayName(value = "Должен добавить новую планету")
    @Order(2)
    void addNewPlanet() {
        driver.get(URL_LORD_ADD);
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver-> driver.findElement(By.id("name")));

        element.sendKeys("Марс");
        driver.findElement(By.tagName("button")).click();


        element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver -> driver.findElement(By.tagName("p")));
        WebElement finalElement = element;
        assertAll("element",
                ()-> assertNotNull(finalElement),
                ()-> assertEquals("success", finalElement.getText())
        );
    }

    @Test
    @DisplayName(value = "Должен назначить Правителя управлять планетой")
    @Order(3)
    void appointLord() {
        driver.get(URL_LORD_APPOINT);
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver-> driver.findElement(By.id("idLord")));
        Select idLord = new Select(element);
//        idLord.selectByVisibleText("Adam");
        idLord.selectByVisibleText("Mark");

        element = driver.findElement(By.id("id"));
        Select id = new Select(element);
//        id.selectByVisibleText("Марс");
        id.selectByVisibleText("Земля");

        driver.findElement(By.tagName("button")).click();

        element = new WebDriverWait(driver, Duration.ofSeconds(3).getSeconds())
                .until(driver -> driver.findElement(By.tagName("p")));
        WebElement finalElement = element;
        assertAll("element",
                ()-> assertNotNull(finalElement),
                ()-> assertEquals("success", finalElement.getText())
        );
    }
}
