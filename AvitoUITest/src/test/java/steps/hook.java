package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ElectronicsSearchPage;
import pages.MainPage;

import java.time.Duration;

import static util.Driver.*;

public class hook {

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        mainPage = new MainPage(driver);
        electronicsSearchPage = new ElectronicsSearchPage(driver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
