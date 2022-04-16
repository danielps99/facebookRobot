package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Pagina;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Curtidor implements ICommons {

    private DriverService driverService = DriverService.get();
    private int indexAtual;
    private Pagina paginaAtual;
    private Integer posicaoAtual = 2;
    private List<String> publicacoesCurtidas;

    public void start(List<Pagina> paginas) {
        for (Pagina pagina : paginas) {
            paginaAtual = pagina;
            entrarNaPagina();
            indexAtual = 0;
            percorrerPublicacoesECurtir();
        }
    }

    private void entrarNaPagina() {
        driverService.getDriver().get(paginaAtual.getUrl());
        info(paginaAtual.getUrl());
        publicacoesCurtidas = new ArrayList<>();
        sleep(30);
    }

    private void percorrerPublicacoesECurtir() {
        windowScrollToPosicaoAtual();
        List<WebElement> publicacoes = buscarPublicacoes();
        while (continuarPercorrendo(publicacoes)) {
            WebElement publicacao = publicacoes.get(indexAtual);
            atualizarPosicaoAtual(publicacao);
            validarECurtir(publicacao);
            indexAtual++;
        }
        percorrerPublicacoesECurtir();
    }

    private void validarECurtir(WebElement publicacao) {
        if (canCurtir(publicacao)) {
            adicionarStringsPublicacoesCurtidas(publicacao);
            formatarEMostarPublicacaoCurtida(publicacao);
        }
    }

    private void adicionarStringsPublicacoesCurtidas(WebElement publicacao) {
        publicacoesCurtidas.add(getTextoSemEspacoLowerCase(publicacao));
    }

    private void windowScrollToPosicaoAtual() {
        driverService.getDriver().executeScript("window.scrollTo(0, " + posicaoAtual + ");");
        sleep(15);
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
                && !publicacoesCurtidas.contains(texto);
    }

    private String getTextoSemEspacoLowerCase(WebElement webElement) {
        return webElement.getText()
                .replace(System.lineSeparator(), "")
                .replace(" ", "")
                .toLowerCase();
    }

    private List<WebElement> buscarPublicacoes() {
        WebElement wePublicacoes = driverService.getDriver().findElement(By.xpath(paginaAtual.getPublicacoesXpath()));
        return wePublicacoes.findElements(By.tagName("div"));
    }

    public void atualizarPosicaoAtual(WebElement webElement) {
        if (webElement.getLocation() != null && webElement.getLocation().getY() != 0) {
            posicaoAtual = webElement.getLocation().getY();
        }
    }

    private void formatarEMostarPublicacaoCurtida(WebElement publicacao) {
        List<String> linhasTexto = Arrays.stream(
                publicacao.getText()
                        .split(System.lineSeparator())
        ).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder(posicaoAtual + " - ");
        for (String linha : linhasTexto) {
            if (canMostarLinhaPublicacao(linha)) {
                sb.append(linha.trim()).append(System.lineSeparator());
            }
        }
        info(sb.toString());
    }

    private boolean canMostarLinhaPublicacao(String linha) {
        String linhaLimpa = linha.toLowerCase()
                .replace("curtir", "")
                .replace("comentar", "")
                .replace("compartilhar", "")
                .replace("online", "")
                .replace("Escreva um comentário público…", "")
                .replace("Pressione Enter para publicar.", "")
                .trim();
        return linhaLimpa.length() > 2;
    }
}
