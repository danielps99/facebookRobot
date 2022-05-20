package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Pagina;
import br.com.bdws.facebookRobot.service.DriverService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Curtidor implements ICommons {

    private final String userHomeFolder = getUserHomeFolder();
    private DriverService driverService = DriverService.get();
    private int indexAtual;
    private Pagina paginaAtual;
    private Integer posicaoAtual;
    private Integer posicaoAnterior;
    private boolean clicou;
    private int contadorPararDeCurtir;
    private int contadorExceptions = 0;
    private int contadorCurtidas;

    public void start(List<Pagina> paginas) {
        for (Pagina pagina : paginas) {
            if (!continuarRobo()) {
                break;
            }
            paginaAtual = pagina;
            inicializarVariaveis();
            entrarNaPagina();
            percorrerPublicacoesECurtir();
        }
    }

    private List<Pagina> reordenar(List<Pagina> paginas, String urlPrimeiraPagina) {
        paginas.stream().forEach(pagina -> {
            System.out.println(pagina);
        });
        List<Pagina> primeiras = new ArrayList<>();
        List<Pagina> ultimas = new ArrayList<>();
        boolean encontrouPrimeira = false;
        for (Pagina pagina : paginas) {
            if (!encontrouPrimeira && urlPrimeiraPagina.equalsIgnoreCase(pagina.getUrl())) {
                encontrouPrimeira = true;
            }
            if (encontrouPrimeira) {
                primeiras.add(pagina);
            } else {
                ultimas.add(pagina);
            }
        }
        System.out.println("DEPOIS");
        primeiras.addAll(ultimas);
        return primeiras;
    }

    private void inicializarVariaveis() {
        indexAtual = 0;
        posicaoAtual = 1;
        posicaoAnterior = 0;
        contadorPararDeCurtir = 0;
        contadorCurtidas = 0;
    }

    private void entrarNaPagina() {
        driverService.getDriver().get(paginaAtual.getUrl());
        sleep(20);
        info(paginaAtual.toString());
    }

    private void percorrerPublicacoesECurtir() {
        sleep(40);
        try {
            validarConteudoECurtir();
            atualizarPosicaoAtual();
        } catch (Exception e) {
            errorComMensagem(e, concat("INDEX:", indexAtual, "_POSIÇÃO:", posicaoAtual, "_PARARDECURTIR:", contadorPararDeCurtir));
            contadorExceptions++;
            tirarPrintScreen(concat(userHomeFolder, "/Curtidor/exception/", getDiaHoraMinutoSegundo(), ".png"));
        }
        if (contadorPararDeCurtir < 10 && continuarRobo() && contadorCurtidas < 50) {
            indexAtual++;
            percorrerPublicacoesECurtir();
        }
    }

    private boolean continuarRobo() {
        return contadorExceptions < 3;
    }

    private void validarConteudoECurtir() {
        if (hasConteudoParaCurtir()) {
            clicou = validarEClicarNoCurtir();
            formatarEImprimirPublicacao();
        }
    }

    private boolean validarEClicarNoCurtir() {
        WebElement curtirBtn = getBtnCurtir();
        if (curtirBtn != null) {
            driverService.waitUntilBeClickable(curtirBtn);
            curtirBtn.click();
            tirarPrintScreen(getNomeArquivoScreenshot());
            contadorCurtidas++;
            return true;
        }
        contadorPararDeCurtir++;
        return false;
    }

    private void tirarPrintScreen(String pathAndNomeArquivo) {
        File scrFile = ((TakesScreenshot) driverService.getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(pathAndNomeArquivo));
        } catch (IOException e) {
            errorComMensagem(e, "takesScreenshot");
        }
    }

    public WebElement getBtnCurtir() {
        return getPublicacaoAtual()
                .findElements(By.tagName("div"))
                .stream()
                .filter(d ->
                        d.getText().equalsIgnoreCase("curtir") && d.getAttribute("aria-label") != null && d.getAttribute("aria-label").equalsIgnoreCase("curtir")
                ).findFirst()
                .orElse(null);
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
        return texto.contains("curtircomentar");
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

        StringBuilder sb = getInicioLinhaPublicacao();
        for (String linha : linhasTexto) {
            if (canMostarLinhaPublicacao(linha)) {
                sb.append(linha.trim()).append(System.lineSeparator());
            }
        }
        info(sb.toString());
    }

    private String getTextoSemEspacoLowerCase() {
        return getPublicacaoAtual().getText()
                .replace(System.lineSeparator(), "")
                .replace(" ", "")
                .toLowerCase();
    }

    private String getNomeArquivoScreenshot() {
        return concat(userHomeFolder, "/Curtidor/", getDiaHoraMinutoSegundo(), "_",
                getSomenteLetrasENumeros(paginaAtual.getNome()), "_", indexAtual, "_", posicaoAtual, ".png");
    }

    private StringBuilder getInicioLinhaPublicacao() {
        return concatSb("ATITUDE:", clicou ? "CURTIU" : "PULOU", "_INDEX:", indexAtual, "_POSIÇÃO:", posicaoAtual,
                "_PARARDECURTIR:", contadorPararDeCurtir, " - ");
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
