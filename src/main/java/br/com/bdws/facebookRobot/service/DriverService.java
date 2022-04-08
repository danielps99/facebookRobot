package br.com.bdws.facebookRobot.service;

import br.com.bdws.facebookRobot.dto.Comando;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverService {

    private static ChromeDriver driver;
    private static ChromeOptions options;
    private static DriverService single;

    public DriverService() {
    }

    public static DriverService get() {
        if (single == null) {
            single = new DriverService();
        }
        return single;
    }

    public void configurarDriver(Comando comando) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        if (Comando.EXECUTAR_NAVEGADOR_FECHADO.equals(comando)) {
            options.addArguments("headless");
        }
    }

    public ChromeDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
        return driver;
    }
}