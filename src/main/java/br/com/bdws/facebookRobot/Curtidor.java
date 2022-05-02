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
    private Integer posicaoAnterior = 0;
    private List<String> publicacoesCurtidas;
    private boolean clicou;

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
        sleep(20);
        info(paginaAtual.getUrl());
        publicacoesCurtidas = new ArrayList<>();
    }

    private void percorrerPublicacoesECurtir() {
        sleep(30);
        try {
            validarConteudoECurtir();
            atualizarPosicaoAtual();
        } catch (Exception e) {
            errorComMensagem(e, "INDEX:" + indexAtual + "_POSIÇÃO:" + posicaoAtual);
        }
        indexAtual++;
        percorrerPublicacoesECurtir();
    }

    private void validarConteudoECurtir() {
        if (hasConteudoParaCurtir()) {
//            clicou = validarEClicarNoCurtir();
            formatarEImprimirPublicacao();
        }
    }

    private void adicionarStringsPublicacoesCurtidas() {
        publicacoesCurtidas.add(getTextoSemEspacoLowerCase());
    }

    private void windowScrollToPosicaoAtual() {
        driverService.getDriver().executeScript("window.scrollTo(0, " + posicaoAtual + ");");
        sleep(3);
    }

    private boolean hasConteudoParaCurtir() {
        String texto = getTextoSemEspacoLowerCase();
        for (String naoCurtirTexto : paginaAtual.getNaoCurtirPalavras()) {
            if (texto.contains(naoCurtirTexto)) {
                return false;
            }
        }
        return (texto.contains("compartilhouumapublicação")
                || texto.contains("compartilhouumlink"))
                && texto.contains("curtircomentar")
                && !publicacoesCurtidas.contains(texto);
    }

    private String getTextoSemEspacoLowerCase() {
        return getPublicacaoAtual().getText()
                .replace(System.lineSeparator(), "")
                .replace(" ", "")
                .toLowerCase();
    }

    private WebElement getPublicacaoAtual() {
        return driverService.getDriver()
                .findElements(By.xpath(paginaAtual.getPublicacoesXpath()))
                .get(indexAtual);
    }

    public void atualizarPosicaoAtual() {
        WebElement webElement = getPublicacaoAtual();
        if (webElement.getLocation() != null && webElement.getLocation().getY() != 0) {
            posicaoAtual = webElement.getLocation().getY();
            if (posicaoAtual > posicaoAnterior) {
                posicaoAnterior = posicaoAtual;
                windowScrollToPosicaoAtual();
            }
        }
    }

    private void formatarEImprimirPublicacao() {
        List<String> linhasTexto = Arrays.stream(
                getPublicacaoAtual().getText().split(System.lineSeparator())
        ).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("ATITUDE:" + (clicou ? "CURTIU" : "PULOU"))
                .append("_INDEX:" + indexAtual + "_POSIÇÃO:" + posicaoAtual + " - ");
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
                .replace("ver tradução", "")
                .replace("escreva um comentário…", "")
                .replace("escreva um comentário público…", "")
                .replace("pressione enter para publicar.", "")
                .trim();
        return linhaLimpa.length() > 2;
    }
}
