package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Comando;
import br.com.bdws.facebookRobot.dto.ContaFacebook;
import br.com.bdws.facebookRobot.service.DriverService;
import br.com.bdws.facebookRobot.service.ReaderJsonService;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class App implements ICommons {

    private DriverService driverService = DriverService.get();
    private ReaderJsonService readerJsonService = ReaderJsonService.get();

    public void start(Comando comando, String arquivoConta) {
        ContaFacebook contaFacebook = readerJsonService.buscarContaFacebook(arquivoConta);
        if (contaFacebook == null) {
            return;
        }
        driverService.configurarDriver(comando);
        logar(contaFacebook);
        if (!Comando.SOMENTE_LOGAR.equals(comando)) {
            decidirAcao(contaFacebook);
        }
    }

    private void decidirAcao(ContaFacebook conta) {
        if (conta.getPaginas() != null && !conta.getPaginas().isEmpty()) {
            new Curtidor().start(conta);
        }
        if (conta.getCompartilhavel() != null) {
            new Compartilhador().start(conta);
        }
        if (conta.getVasculharGruposXpath() != null) {
            new Vasculhador().start(conta);
        }
    }

    private void logar(ContaFacebook conta) {
        ChromeDriver driver = driverService.getDriver();
        info("Conta escolhida: " + conta.getEmail());
        driver.get("https://www.facebook.com/");
        digitar(driver.findElement(By.id("email")), conta.getEmail());
        sleep(1);
        digitar(driver.findElement(By.id("pass")), conta.getPasswd());
        sleep(1);
        driver.findElement(By.tagName("button")).click();
        sleep(30);
    }
}
