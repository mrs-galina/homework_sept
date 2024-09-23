package util;

import io.cucumber.core.cli.Main;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.ElectronicsSearchPage;
import pages.MainPage;

import java.time.Duration;

public class Driver {
    public static WebDriver driver = new ChromeDriver();
    public static MainPage mainPage;
    public static ElectronicsSearchPage electronicsSearchPage;
}
