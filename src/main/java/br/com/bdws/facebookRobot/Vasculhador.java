package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.ContaFacebook;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Vasculhador implements ICommons {

    private int indexAtual = 0;
    private ChromeDriver driver = DriverService.get().getDriver();
    private ContaFacebook conta;

    public void start(ContaFacebook contaFacebook) {
        conta = contaFacebook;
        driver.get("https://www.facebook.com/groups/feed/");
        sleep(5);
        info(concat("---------------------------------- INICIO VASCULHAR GRUPOS ----------------------------------"));
        percorrerEImprimir();
        info(concat("------------------------------------ FIM VASCULHAR GRUPOS -----------------------------------"));
    }

    private void percorrerEImprimir() {
        sleep(2);
        try {
            WebElement grupoAtual = getGrupoAtual();
            String groupText = grupoAtual.getText();
            info(concat(groupText, System.lineSeparator(), grupoAtual.getAttribute("href"), System.lineSeparator()));
            try {
                grupoAtual.sendKeys(Keys.ARROW_DOWN);
            } catch (ElementNotInteractableException e) {

            }
            indexAtual++;
            sleep(2);
            percorrerEImprimir();
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebElement getGrupoAtual() {
        return driver.findElements(By.xpath(conta.getVasculharGruposXpath())).get(indexAtual);
    }
}