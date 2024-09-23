package steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import util.CategorisOfElectronics;
import util.TypesOfSort;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Driver.driver;
import static util.Driver.mainPage;
import static util.Driver.electronicsSearchPage;
import static util.TypesOfSort.дешевле;
import static util.TypesOfSort.дороже;
import static util.TypesOfSort.по_дате;


public class StepsAvitoFindPrinters {

    @ParameterType(".*")
    public CategorisOfElectronics category(String category) {
        return CategorisOfElectronics.valueOf(category.trim());
    }

    @ParameterType(".*")
    public TypesOfSort sort(String sort) {
        return TypesOfSort.valueOf(sort.trim());
    }

    @Пусть("открыт ресурс авито")
    public static void goToMainPage() {
        System.out.println("Going to the main page");
        driver.get("https://www.avito.ru/");
    }

    @И("в выпадающем списке категорий выбрана {category}")
    public static void goToOfficeEquipmentSearchPage(CategorisOfElectronics category) {
        //to do - тут должна быть реализация разных категорий
            System.out.println("Clicking 'all categories'");
            mainPage.clickAllCategories();
            System.out.println("Clicking 'electronics'");
            mainPage.clickElectronics();
            System.out.println("Clicking 'office equipment'");
            mainPage.clickOfficeEquipment();
    }

    @И("в поле поиска введено значение {word}")
    public static void inputPrinterInSearchLine(String thing) {
        System.out.println("Input 'Принтер' in the searh line");
        electronicsSearchPage.search(thing);
    }

    @И("активирован чекбокс с авито доставкой")
    public static void clickAvitoDelivery() {
        System.out.println("Selecting avito delivery to be active");
        electronicsSearchPage.selectAvitoDelivery();
    }

    @Тогда("кликнуть по выпадаюшему списку региона")
    public static void clickReginPopUp() {
        System.out.println("Clicking region popup");
        electronicsSearchPage.clickLocationPopUp();
    }

    @Тогда("в поле регион введено значение {word}")
    public static void inputVladivostok(String city) {
        System.out.println("Input vladivostok");
        electronicsSearchPage.changeLocation(city);
    }

    @И("нажата кнопка показать объявления")
    public static void clickShowAds() {
        System.out.println("Clicking save city and show ads");
        electronicsSearchPage.clickSaveLocation();
    }

    @Тогда("открылась страница результаты по запросу {word}")
    public static void checkPageIsOpened(String thing) {
        System.out.println("Checking the page for " + thing + " is opened");
        assertEquals("https://www.avito.ru/vladivostok/orgtehnika_i_rashodniki?d=1&q=%D0%9F%D1%80%D0%B8%D0%BD%D1%82%D0%B5%D1%80", driver.getCurrentUrl(),
                "Another page was opened: " + driver.getCurrentUrl() + ", but should be " + "https://www.avito.ru/vladivostok/orgtehnika_i_rashodniki?d=1&q=%D0%9F%D1%80%D0%B8%D0%BD%D1%82%D0%B5%D1%80");
    }

    @И("в выпадающем списке сортировка выбрано значение {sort}")
    public static void chooseSortForMoreExpensiveFirst(TypesOfSort sort) {
        if (sort == дешевле) {
            System.out.println("Selecting sort for cheaper first");
            electronicsSearchPage.chooseCheaperSort();
        } else if (sort == по_дате) {
            System.out.println("Selecting sort for newer first");
            electronicsSearchPage.chooseNewerSort();
        } else if (sort == дороже)
        System.out.println("Choosing sort for more expensive first");
        electronicsSearchPage.chooseMoreExpensiveSort();
    }

    @И("в консоль выведено значение названия и цены {int} первых товаров")
    public static void showThreeMostExpensivePrinters(int num) {
        if (num == 3) {
            System.out.println("Getting three most expensive printers");
            Map<String, String> result = electronicsSearchPage.getFirstThree();
            System.out.println("Checking that the prices contain ₽ symbol");
            List<String> prices = result.values().stream().toList();
            assertTrue(-1 != prices.get(0).indexOf('₽'),
                    "Failed: first item price doesn't have the symbol");
            assertTrue(-1 != prices.get(1).indexOf('₽'),
                    "Failed: second item price doesn't have the symbol");
            assertTrue(-1 != prices.get(2).indexOf('₽'),
                    "Failed: third item price doesn't have the symbol");
            System.out.println("Three most expensive printers are: " +
                    '\n' + result.entrySet());
        }
    }
}
