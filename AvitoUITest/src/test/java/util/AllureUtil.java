package util;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AllureUtil {

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveAllureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    public static byte[] saveAshotScreenshotPNG(WebDriver driver) throws IOException {
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.simple())
                .takeScreenshot(driver);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenshot.getImage(), "png", baos);
        return baos.toByteArray();
    }
}
