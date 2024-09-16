package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MainPage {
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;
    @FindBy(xpath = "//button[@data-marker='top-rubricator/all-categories']")
    private  WebElement allCategories;
    @FindBy(xpath = "//div[@data-marker='top-rubricator/root-category-26195']")
    private  WebElement electronics;
    @FindBy(xpath = "//*[contains(text(), 'Оргтехника и расходники')]")
    private  WebElement officeEquipment;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public  void getAllCategories() {
        allCategories.click();
    }

    public void getToElectronics() {
        actions.moveToElement(electronics);
        electronics.click();
    }

    public void getToOfficeEquipment() {
        wait.until(ExpectedConditions.visibilityOf(officeEquipment));
        actions.moveToElement(officeEquipment);
        officeEquipment.click();
    }
}
