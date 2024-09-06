import org.mockito.InOrder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

public class CalculatorServiceImplTest {
    private static final String FIRST_DIGIT = "12";
    private static final String SECOND_DIGIT = "2";
    private static final String ZERO = "0";
    private static final String EXPECTED_OUTPUT = "12,00 / 2,00 = 6,00";
    private static final Double EXPECTED_RESULT = 6.00;
    private static final String TEST = "Test";
    private static final String TOO_LARGE_VALUE = "1000000000001.0";
    private static final String TOO_SMALL_VALUE = "-1000000000001.0";
    private InOrder inOrder;
    private IOService ioService;
    private CalculatorService calculatorService;

    @BeforeMethod
    void setUp() {
        ioService = mock(IOService.class);
        calculatorService = new CalculatorServiceImpl(ioService);
        inOrder = inOrder(ioService);
    }

    @DataProvider(name = "invalid data")
    public static Object[][] getData() {
        return new Object[][]{
                {""},
                {"A word"},
                {" "},
                {"!!!"},
                {"5,06"}
        };
    }

    @Test(testName = "Read two digits and divide")
    void shouldReadTwoDigitsAndDivide() {
        given(ioService.readString()).willReturn(FIRST_DIGIT).willReturn(SECOND_DIGIT);
        calculatorService.readTwoDigitsAndDivide(anyString());
        inOrder.verify(ioService, times(2)).readString();
        inOrder.verify(ioService, times(1)).out(EXPECTED_OUTPUT);
    }

    @Test (testName = "Enter everything (except numbers) - should throw not a number exception",
            expectedExceptions = NumberFormatException.class,
            dataProvider = "invalid data")
    void readAndDivideShouldThrowNumberFormatException(String input) {
        given(ioService.readString()).willReturn(input).willReturn(SECOND_DIGIT);
        calculatorService.readTwoDigitsAndDivide(anyString());
    }

    @Test (testName = "Divide two digits")
    void shouldDivideTwoDigits() {
        Assert.assertEquals(calculatorService.divideTwoDigits(TEST, FIRST_DIGIT, SECOND_DIGIT), EXPECTED_RESULT);
    }

    @Test (testName = "Use everything (except numbers) - should throw not a number exception",
            expectedExceptions = NumberFormatException.class,
            dataProvider = "invalid data")
    void divideShouldThrowNumberFormatException(String input) {
        calculatorService.divideTwoDigits(TEST, FIRST_DIGIT, input);
    }

    @Test (testName = "Divide by zero - should throw exception",
            expectedExceptions = ArithmeticException.class)
    void checkNumberShouldThrowArithmeticException() {
        calculatorService.checkNumber(Double.parseDouble(FIRST_DIGIT), Double.parseDouble(ZERO));
    }

    @Test (testName = "Too small number - should throw exception",
            expectedExceptions = CountException.class)
    void checkSmallNumberShouldThrowCountException() {
        calculatorService.checkNumber(Double.parseDouble(FIRST_DIGIT), Double.parseDouble(TOO_SMALL_VALUE));
    }

    @Test (testName = "Too large number - should throw exception",
            expectedExceptions = CountException.class)
    void checkLargeNumberShouldThrowCountException() {
        calculatorService.checkNumber(Double.parseDouble(TOO_LARGE_VALUE), Double.parseDouble(SECOND_DIGIT));
    }
}
