import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleIOServiceImplTest {
    private static final String NEXT_LINE = System.lineSeparator();
    private static final String TEST_LINE = "This is a test line";
    private PrintStream backup;
    private ByteArrayOutputStream bos;
    private IOService ioService;

    @BeforeMethod
    void setUp() {
        backup = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        ioService = new ConsoleIOServiceImpl();
    }

    @AfterMethod
    void tearDown() {
        System.setOut(backup);
    }

    @Test(testName = "Should print: " + TEST_LINE)
    void shouldPrintTestLine() {
        ioService.out(TEST_LINE);
        Assert.assertEquals(bos.toString(), TEST_LINE + NEXT_LINE);
    }
}
