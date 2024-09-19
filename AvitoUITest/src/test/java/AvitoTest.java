

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ElectronicsSearchPage;
import pages.MainPage;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.AllureUtil.saveAllureScreenshot;
import static util.AllureUtil.saveAshotScreenshotPNG;


public class AvitoTest {
    public static WebDriver driver;
    public static MainPage mainPage;
    public static ElectronicsSearchPage electronicsSearchPage;
    public static final String CITY = "Владивосток";
    public static final String THING_TO_FIND = "Принтер";
    public static final String URL_AVITO = "https://www.avito.ru/";
    public static final String EXPECTED_TITLE = "Авито: недвижимость, транспорт, работа, услуги, вещи";


    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    public static void tearDown() {
       driver.quit();
    }

    @Test
    @DisplayName("Get 3 most expensive " + THING_TO_FIND)
    public void get3MostExpensivePrinters() throws IOException {
        mainPage = new MainPage(driver);
        electronicsSearchPage = new ElectronicsSearchPage(driver);
        goToMainAndCheckTitle();
        goToOfficeEquipmentSearchPageAndSearch();
        changeLocation();
        selectAvitoDelivery();
        changeSortForMoreExpensiveFirst();
        getThreeMostExpensiveAndCheckPrice();
    }

    @Step("Going to the main page")
    public void goToMainAndCheckTitle() {
        System.out.println("Going to the main page");
        driver.get(URL_AVITO);
        System.out.println("Checking the title");
        assertEquals(driver.getTitle(), EXPECTED_TITLE,
                "Another page was opened: title is " + driver.getTitle() + ", but should be " + EXPECTED_TITLE);
        try {
            saveAshotScreenshotPNG(driver);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save screenshot PNG", e);
        }
    }

    @Step("Going to office equipment and searching for printer")
    public void goToOfficeEquipmentSearchPageAndSearch() {
        System.out.println("Clicking 'all categories'");
        mainPage.clickAllCategories();
        System.out.println("Clicking 'electronics'");
        mainPage.clickElectronics();
        System.out.println("Clicking 'office equipment'");
        mainPage.clickOfficeEquipment();
        System.out.println("Input " + THING_TO_FIND + " in the searh line");
        electronicsSearchPage.search(THING_TO_FIND);
        saveAllureScreenshot(driver);
    }

    @Step("Changing location to Vladivostok")
    public void changeLocation() {
        System.out.println("Changing location to " + CITY);
        electronicsSearchPage.changeLocation(CITY);
        saveAllureScreenshot(driver);
    }

    @Step("Selecting avito delivery")
    public void selectAvitoDelivery() {
        System.out.println("Selecting avito delivery to be active");
        electronicsSearchPage.selectAvitoDelivery();
        saveAllureScreenshot(driver);
    }

    @Step("Changing sort for more expensive first")
    public void changeSortForMoreExpensiveFirst() {
        System.out.println("Choosing sort for more expensive first");
        electronicsSearchPage.chooseMoreExpensiveSort();
        saveAllureScreenshot(driver);
    }

    @Step("Getting three most expensive printers and checking that price has ₽ symbol")
    public void getThreeMostExpensiveAndCheckPrice() {
        System.out.println("Getting three most expensive " + THING_TO_FIND);
        Map<String, String> result = electronicsSearchPage.getFirstThree();
        saveAllureScreenshot(driver);
        System.out.println("Checking that the prices contain ₽ symbol");
        List<String> prices = result.values().stream().toList();
        assertTrue(-1 != prices.get(0).indexOf('₽'),
                "Failed: first item price doesn't have the symbol");
        assertTrue(-1 != prices.get(1).indexOf('₽'),
                "Failed: second item price doesn't have the symbol");
        assertTrue(-1 != prices.get(2).indexOf('₽'),
                "Failed: third item price doesn't have the symbol");
        System.out.println("Three most expensive " + THING_TO_FIND +
                '\n' + result.entrySet());
    }
}
