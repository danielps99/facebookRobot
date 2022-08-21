package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Compartilhavel;
import br.com.bdws.facebookRobot.dto.ContaFacebook;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Compartilhador implements ICommons {

    private Compartilhavel compartilhavel;
    private ChromeDriver driver = DriverService.get().getDriver();
    private ContaFacebook conta;

    public void start(ContaFacebook contaFacebook) {
        conta = contaFacebook;
        compartilhavel = conta.getCompartilhavel();
        for (String nomeGrupo : compartilhavel.getNomesGrupos()) {
            irParaUrlCompartilhavel();
            compartilhar(nomeGrupo);
        }
    }

    private void irParaUrlCompartilhavel() {
        driver.get(compartilhavel.getUrl());
        sleep(5);
    }

    private void compartilhar(String nomeGrupo) {
        buscarElementoPorTagEAriaLabel(driver, "div", "Envie isso para amigos ou publique na sua linha do tempo").click();
        sleep(5);

        buscarElementoPorTagETexto(driver, "span", "Compartilhar em um grupo").click();
        sleep(5);

        buscarElementoPorTagEAriaLabel(driver, "label", "Compartilhar como").click();
        sleep(5);

        buscarElementoPorTagETexto(driver, "span", compartilhavel.getCompartilharComo()).click();

        WebElement inputProcurarGrupos = buscarElementoPorTagEAriaLabel(driver, "input", "Procurar grupos");
        sleep(2);
        digitar(inputProcurarGrupos, nomeGrupo);
        sleep(5);

        String clicado = selecionarGrupo(nomeGrupo);
        sleep(10);

//        if (compartilhavel.getIncluirPubOriginal()) {
//            WebElement checkIncluirPubOriginal = driver.findElement(By.xpath("//*[@class=\"oajrlxb2 rq0escxv f1sip0of hidtqoto nhd2j8a9 datstx6m kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x b5wmifdl lzcic4wl jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso pmk7jnqg j9ispegn kr520xx4 k4urcfbm\"]"));
//            checkIncluirPubOriginal.click();
//            sleep(5);
//        }

        String nomeArquivo = concat(roboRootFolder, "/Compartilhador/", conta.getEmailComoPasta(), "/", compartilhavel.getUrlComoPasta(), "/", getDiaHoraMinutoSegundo(), "_", nomeGrupo.replaceAll("\\W", "_"), ".png");
        tirarPrintScreen(driver, nomeArquivo);

        driver.findElement(By.xpath("//div[@aria-label='Publicar']")).click();
//        buscarElementoPorTagEAriaLabel(driver, "div", "Publicar").click();
        sleep(10);
        info(clicado.length() > 0 ? "COMPARTILHOU CORRETAMENTE: " + nomeGrupo : "NAO COMPARTILHOU");
    }

    private String selecionarGrupo(String nomeGrupo) {
        List<WebElement> grupos = driver.findElements(By.xpath("//*[@class=\"qi72231t o9w3sbdw nu7423ey tav9wjvu flwp5yud tghlliq5 gkg15gwv s9ok87oh s9ljgwtm lxqftegz bf1zulr9 frfouenu bonavkto djs4p424 r7bn319e bdao358l fsf7x5fv tgm57n0e jez8cy9q s5oniofx m8h3af8h l7ghb35v kjdc1dyq kmwttqpk dnr7xe2t aeinzg81 srn514ro oxkhqvkx rl78xhln nch0832m om3e55n1 cr00lzj9 rn8ck1ys s3jn8y49 g4tp4svg jl2a5g8c f14ij5to l3ldwz01 icdlwmnq h8391g91 m0cukt09 kpwa50dg ta68dy8c b6ax4al1\"]"));
        sleep(3);
        for (WebElement grupo : grupos) {
            if (grupo.getText().contains(nomeGrupo)) {
                grupo.click();
                return nomeGrupo;
            }
        }
        return "";
    }
}