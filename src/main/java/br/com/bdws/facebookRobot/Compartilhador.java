package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Compartilhavel;
import br.com.bdws.facebookRobot.dto.ContaFacebook;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Compartilhador implements ICommons {

    private Compartilhavel compartilhavel;
    private ChromeDriver driver = DriverService.get().getDriver();
    private ContaFacebook conta;

    public void start(ContaFacebook contaFacebook) {
        conta = contaFacebook;
        compartilhavel = conta.getCompartilhavel();
        imprirStrings("", compartilhavel.getNomesGrupos());
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
        try {
            buscarElementoPorTagEAriaLabel(driver, "div", "Envie isso para amigos ou publique na sua linha do tempo").click();
            sleep(5);

            buscarElementoPorTagETexto(driver, "span", "Compartilhar em um grupo").click();
            sleep(5);

            buscarElementoPorTagEAriaLabel(driver, "label", "Compartilhar como").click();
            sleep(5);

            buscarElementoPorTagETexto(driver, "span", compartilhavel.getCompartilharComo()).click();

            pesquisarESelecionarGrupo(nomeGrupo);
        } catch (Exception e) {
            //Preferi não fazer nada aqui, na próxima exception trato
        }

        SelecionarIncluirPublicacaoOriginalOuEscreverPublicacao();

        try {
            buscarElementoPorTagEAriaLabel(driver, "div", "Publicar").click();
            info("COMPARTILHOU CORRETAMENTE: " + nomeGrupo);
        } catch (Exception e) {
            info("NAO COMPARTILHOU: " + nomeGrupo);
        }
        String nomeArquivo = concat(roboRootFolder, "/Compartilhador/", conta.getEmailComoPasta(), "/", compartilhavel.getUrlComoPasta(), "/", getDiaHoraMinutoSegundo(), "_", nomeGrupo.replaceAll("\\W", "_"), ".png");
        tirarPrintScreen(driver, nomeArquivo);
        sleep(10);
    }

    private void SelecionarIncluirPublicacaoOriginalOuEscreverPublicacao() {
        try {
            buscarElementoPorTagETexto(driver, "span", "Incluir publicação original").click();
            sleep(5);
        } catch (NoSuchElementException e) {
            try {
                digitar(buscarElementoPorTagEAriaLabel(driver, "div", "Crie uma publicação aberta…"), compartilhavel.getTextoPublicacao());
                sleep(2);
            } catch (Exception ex) {
                //Preferi não fazer nada aqui, na próxima exception trato
            }
        }
    }

    private void pesquisarESelecionarGrupo(String nomeGrupo) {
        WebElement inputProcurarGrupos = buscarElementoPorTagEAriaLabel(driver, "input", "Procurar grupos");
        sleep(2);
        digitar(inputProcurarGrupos, nomeGrupo);
        sleep(5);

        buscarElementoPorTagETexto(driver, "span", nomeGrupo).click();
        sleep(10);
    }
}