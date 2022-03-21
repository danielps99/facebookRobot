package br.com.bdws.facebookRobot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {

    private ChromeDriver driver;

    public void start() {
        inicializarNavegador();
    }

    private void inicializarNavegador() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.facebook.com/");
    }
}
