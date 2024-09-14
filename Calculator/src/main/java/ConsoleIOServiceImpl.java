import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIOServiceImpl implements IOService {
    private final PrintStream out;
    private final Scanner in;

    public ConsoleIOServiceImpl() {
        this.out = System.out;
        this.in = new Scanner(System.in);
    }

    @Override
    public void out(String message) {
        out.println(message);
    }

    @Override
    public String readString() {
        return in.nextLine();
    }
}
