
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ElectronicsSearchPage;
import pages.MainPage;

import java.time.Duration;


public class AvitoTest {
    public static WebDriver driver;
    public static MainPage mainPage;
    public static ElectronicsSearchPage electronicsSearchPage;
    public static final String city = "Владивосток";
    public static final String thingToFind = "Принтер";

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        mainPage = new MainPage(driver);
        electronicsSearchPage = new ElectronicsSearchPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.avito.ru/");
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Get 3 most expensive " + thingToFind)
    public void get3MostExpensivePrinters() {
        System.out.println("Clicking 'all categories'");
        mainPage.getAllCategories();
        System.out.println("Clicking 'electronics'");
        mainPage.getToElectronics();
        System.out.println("Clicking 'office equipment'");
        mainPage.getToOfficeEquipment();
        System.out.println("Input " + thingToFind + " in the searh line");
        electronicsSearchPage.search(thingToFind);
        System.out.println("Changing location to " + city);
        electronicsSearchPage.changeLocation(city);
        System.out.println("Selecting avito delivery to be active");
        electronicsSearchPage.selectAvitoDelivery();
        System.out.println("Choosing sort for more expensive first");
        electronicsSearchPage.chooseMoreExpensiveSort();
        System.out.println("Getting three most expensive " + thingToFind);
        electronicsSearchPage.getFirstThree();
    }
}
