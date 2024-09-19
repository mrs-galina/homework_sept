package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElectronicsSearchPage {
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;
    @FindBy(xpath = "//input[@data-marker='search-form/suggest/input']")
    private WebElement searchLine;
    @FindBy(xpath = "//button[@data-marker='search-form/submit-button']")
    private WebElement searchButton;
    @FindBy(xpath = "//div[@data-marker='search-form/change-location']")
    private WebElement changeLocation;
    @FindBy(xpath = "//*[@data-marker='popup-location/region']//*[@placeholder='Город или регион']")
    private WebElement inputLocation;
    @FindBy(xpath = "//button[@data-marker='popup-location/region/custom-option([object Object])']")
    private WebElement suggestedLocation;
    @FindBy(xpath = "//*[@data-marker='popup-location/popup']//button[@data-marker='popup-location/save-button']")
    private WebElement saveLocation;
    @FindBy(xpath = "//*[@role='checkbox']//*[contains(text(), 'С Авито Доставкой')]")
    private WebElement checkBoxWithAvitoDelivery;
    @FindBy(xpath = "//*[contains(text(),'Часто ищут')]")
    private WebElement oftenSearched;
    @FindBy(xpath = "//button[@data-marker='search-filters/submit-button']")
    private WebElement submitChangesButton;
    @FindBy(xpath = "//span[@data-marker='sort/title']")
    private WebElement sort;
    @FindBy(xpath = "//button[@data-marker='sort/custom-option(2)']")
    private WebElement sortMoreExpensive;
    private String xpathElements = "//*[@data-marker='item']//*[@class='iva-item-titleStep-pdebR']";
    private String xpathPrices = "//*[@data-marker='item']//*[@class='iva-item-priceStep-uq2CQ']";

    public ElectronicsSearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void search(String search) {
        searchLine.click();
        searchLine.sendKeys(search);
        searchButton.click();
    }

    public void changeLocation(String location) {
        changeLocation.click();
        inputLocation.click();
        inputLocation.clear();
        inputLocation.sendKeys(location);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-marker='popup-location/region/custom-option([object Object])']")));
        wait.until(ExpectedConditions.textToBePresentInElement(suggestedLocation, location));
        suggestedLocation.click();
        saveLocation.click();
    }

    public void selectAvitoDelivery() {
        actions.scrollToElement(oftenSearched).perform();
        actions.moveToElement(checkBoxWithAvitoDelivery).perform();
        wait.until(ExpectedConditions.elementToBeClickable(checkBoxWithAvitoDelivery));
        if (!checkBoxWithAvitoDelivery.isSelected()) {
            checkBoxWithAvitoDelivery.click();
        }
        submitChangesButton.click();
    }

    public void chooseMoreExpensiveSort() {
        sort.click();
        wait.until(ExpectedConditions.visibilityOf(sortMoreExpensive));
        sortMoreExpensive.click();
    }

    public LinkedHashMap<String, String> getFirstThree() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathElements)));
        LinkedHashMap<String, String> elementsAndPrices = new LinkedHashMap<>();
        elementsAndPrices.put(
                driver.findElements(By.xpath(xpathElements)).get(0).getText(),
                driver.findElements(By.xpath(xpathPrices)).get(0).getText()
        );
        elementsAndPrices.put(
                driver.findElements(By.xpath(xpathElements)).get(1).getText(),
                driver.findElements(By.xpath(xpathPrices)).get(1).getText()
        );
        elementsAndPrices.put(
                driver.findElements(By.xpath(xpathElements)).get(2).getText(),
                driver.findElements(By.xpath(xpathPrices)).get(2).getText()
        );
        return elementsAndPrices;
    }
}
