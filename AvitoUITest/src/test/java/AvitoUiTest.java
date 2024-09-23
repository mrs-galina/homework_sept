import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src\\test\\java\\features",
        glue = "steps",
        tags = "@1"
)
public class AvitoUiTest {

    @org.junit.Test
    public void run(String[] args) {

    }
}
