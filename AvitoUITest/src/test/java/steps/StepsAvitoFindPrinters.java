package steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import util.TypesOfSort;

import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Driver.driver;
import static util.Driver.mainPage;
import static util.Driver.electronicsSearchPage;
import static util.TypesOfSort.дешевле;
import static util.TypesOfSort.дороже;
import static util.TypesOfSort.по_дате;


public class StepsAvitoFindPrinters {

    @ParameterType(".*")
    public TypesOfSort sort(String sort) {
        return TypesOfSort.valueOf(sort.trim());
    }

    @Пусть("открыть ресурс авито")
    public static void goToMainPage() {
        System.out.println("Going to the main page");
        driver.get("https://www.avito.ru/");
    }

    @И("в выпадающем списке выбрать категорию оргтехника")
    public static void goToOfficeEquipmentSearchPage() {
            System.out.println("Clicking 'all categories'");
            mainPage.clickAllCategories();
            System.out.println("Clicking 'electronics'");
            mainPage.clickElectronics();
            System.out.println("Clicking 'office equipment'");
            mainPage.clickOfficeEquipment();
    }

    @И("в поле поиска ввести значение {word}")
    public static void inputPrinterInSearchLine(String thing) {
        String input = thing.replaceAll(("\""),"");
        System.out.println("Input 'Принтер' in the searh line");
        electronicsSearchPage.search(input);
    }

    @И("активировать чекбокс с авито доставкой")
    public static void clickAvitoDelivery() {
        System.out.println("Selecting avito delivery to be active");
        electronicsSearchPage.selectAvitoDelivery();
    }

    @Тогда("кликнуть по выпадаюшему списку региона")
    public static void clickReginPopUp() {
        System.out.println("Clicking region popup");
        electronicsSearchPage.clickLocationPopUp();
    }

    @Тогда("в поле регион ввести значение {word}")
    public static void inputVladivostok(String city) {
        String input = city.replaceAll(("\""),"");
        System.out.println("Input vladivostok");
        electronicsSearchPage.changeLocation(input);
    }

    @И("нажать кнопку показать объявления")
    public static void clickShowAds() {
        System.out.println("Clicking save city and show ads");
        electronicsSearchPage.clickSaveLocation();
    }

    @Тогда("открыта страница результаты по запросу {word}")
    public static void checkPageIsOpened(String thing) {
        String input = thing.replaceAll(("\""),"");
        String xpath = "//*[@data-marker='breadcrumbs']//span[contains(text(), '" + input + "')]";
        assertTrue(electronicsSearchPage.elementExists(xpath), "Failed: wrong page is opened");
        }

    @И("в выпадающем списке сортировка выбрать значение {sort}")
    public static void chooseSortForMoreExpensiveFirst(TypesOfSort sort) {
        switch (sort) {
            case дороже -> {
                printSortInfo(дороже);
                electronicsSearchPage.chooseMoreExpensiveSort();
            }
            case дешевле -> {
                printSortInfo(дешевле);
                electronicsSearchPage.chooseCheaperSort();
            }
            case по_дате -> {
                printSortInfo(по_дате);
                electronicsSearchPage.chooseNewerSort();
            }
        }
    }

    @И("в консоль выведено значение названия и цены {word} первых товаров")
    public static void showThreeThings(String num) {

        int input = parseInt(num.replaceAll(("\""),""));
        System.out.println("Getting first things");
        Map<String, String> result = electronicsSearchPage.getFirstElements(input);
        System.out.println("Checking that the prices contain ₽ symbol");
        List<String> prices = result.values().stream().toList();
        for (int i = 0; i < input; i++) {
            assertTrue(-1 != prices.get(i).indexOf('₽'),
                    "Failed: item price doesn't have the symbol, item number: " + i);
        }
        System.out.println("The thing found: " +
                    '\n' + result.entrySet());
    }

    public static void printSortInfo (TypesOfSort sort) {
        System.out.println("Choosing sort type: " + sort.value);
    }
}
