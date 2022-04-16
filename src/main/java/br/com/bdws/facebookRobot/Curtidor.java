package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Pagina;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Curtidor implements ICommons {

    private DriverService driverService = DriverService.get();
    private int indexAtual;
    private Pagina paginaAtual;
    private Integer posicaoAtual = 2;
    private Integer posicaoAnterior = 0;
    //    private List<WebElement> publicacoes;
    private List<String> publicacoesUnicas;

    public void start(List<Pagina> paginas) {
        for (Pagina pagina : paginas) {
            paginaAtual = pagina;
            entrarNaPagina();
            indexAtual = 0;
            percorrerPublicacoes();
        }
    }

    private void entrarNaPagina() {
        driverService.getDriver().get(paginaAtual.getUrl());
        info(paginaAtual.getUrl());
        publicacoesUnicas = new ArrayList<>();
        sleep(30);
    }

    private void percorrerPublicacoes() {
        windowScrollToPosicaoAtual();
        List<WebElement> publicacoes = buscarPublicacoes();
        while (continuarPercorrendo(publicacoes)) {
            WebElement publicacao = publicacoes.get(indexAtual);
            atualizarPosicaoAtual(publicacao);
            if (canCurtir(publicacao)) {
                adicionarStringsPublicacoesUnicas(publicacao);
                info("------------" + indexAtual + "----------" + getTextoSemEspacoLowerCase(publicacao));
            }
            indexAtual++;
        }
        percorrerPublicacoes();
    }

    private void adicionarStringsPublicacoesUnicas(WebElement publicacao) {
        publicacoesUnicas.add(getTextoSemEspacoLowerCase(publicacao));
    }

    private void windowScrollToPosicaoAtual() {
        driverService.getDriver().executeScript("window.scrollTo(0, " + posicaoAtual + ");");
        sleep(15);
        info("posicaoAtual" + posicaoAtual);
    }

    private boolean continuarPercorrendo(List<WebElement> publicacoes) {
        return indexAtual < publicacoes.size();
    }

    private boolean canCurtir(WebElement publicacao) {
        String texto = getTextoSemEspacoLowerCase(publicacao);
        for (String naoCurtirTexto : paginaAtual.getNaoCurtirPalavras()) {
            if (texto.contains(naoCurtirTexto)) {
                return false;
            }
        }
        return texto.contains("compartilhouumapublicação")
                && texto.contains("curtircomentarcompartilhar")
                && !publicacoesUnicas.contains(texto);
    }

    private String getTextoSemEspacoLowerCase(WebElement publicacao) {
        return publicacao.getText()
                .replace(System.lineSeparator(), "")
                .replace(" ", "")
                .toLowerCase();
    }

    private List<WebElement> buscarPublicacoes() {
        WebElement wePublicacoes = driverService.getDriver().findElement(By.xpath(paginaAtual.getXpath()));
        return wePublicacoes.findElements(By.tagName("div"));
    }

    public void atualizarPosicaoAtual(WebElement webElement) {
        if (webElement.getLocation() != null && webElement.getLocation().getY() != 0) {
            posicaoAtual = webElement.getLocation().getY();
            int diferenca = posicaoAtual - posicaoAnterior;
            if (diferenca > 100) {
                posicaoAnterior = posicaoAtual;
//                info(posicaoAnterior + " - " + posicaoAtual);
            }
        }
    }
}
