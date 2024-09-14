public class CalculatorServiceImpl implements CalculatorService {
    private final IOService ioService;
    private static final double MAX_VALUE = 1_000_000_000_000.0;
    private static final double MIN_VALUE = -1_000_000_000_000.0;

    public CalculatorServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void readTwoDigitsAndDivide(String promt) {
        ioService.out(promt);
        double d1 = Double.parseDouble(ioService.readString());
        double d2 = Double.parseDouble(ioService.readString());
        if (d2 == 0) throw new ArithmeticException("Cannot divide by zero");
        checkNumber(d1, d2);
        ioService.out(String.format("%.2f / %.2f = %.2f", d1, d2, d1/d2));
    }

    @Override
    public double divideTwoDigits(String promt, String d1, String d2) {
        ioService.out(promt);
        double num1 = Double.parseDouble(d1);
        double num2 = Double.parseDouble(d2);
        checkNumber(num1, num2);
        return num1 / num2;
    }

    @Override
    public void checkNumber(double num1, double num2) {
        if (num2 == 0) throw new ArithmeticException("Cannot divide by zero");
        if (num1 < MIN_VALUE || num1 > MAX_VALUE) throw new CountException("Cannot be counted. Number is too large or too small");
        if (num2 < MIN_VALUE || num2 > MAX_VALUE) throw new CountException("Cannot be counted. Number is too large or too small");
    }
}
