package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dao.IntermediadorDadosDao;
import br.com.bdws.facebookRobot.dto.ContaFacebook;
import br.com.bdws.facebookRobot.dto.Pagina;
import br.com.bdws.facebookRobot.dto.PaginaCurtidaDto;
import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Curtidor implements ICommons {

    private DriverService driverService = DriverService.get();
    private IntermediadorDadosDao dao;
    private int indexAtual;
    private Pagina paginaAtual;
    private Integer posicaoAtual;
    private Integer posicaoAnterior;
    private boolean clicou;
    private int contadorPararDeCurtir;
    private int contadorExceptions = 0;
    private int contadorCurtidas;
    private List<Pagina> paginas;
    private int paginaEmAndamentoId = 0;
    private ContaFacebook conta;

    public void start(ContaFacebook contaFacebook) {
        conta = contaFacebook;
        dao = IntermediadorDadosDao.get(conta.getEmailComoPasta());
        recuperarPaginaEmAndamentoEReordenarPaginas();
        imprimirStrings("curtir", paginas.stream().map(o -> o.toString()).collect(Collectors.toList()));
        for (Pagina pagina : this.paginas) {
            if (!continuarRobo()) break;
            inicializarVariaveis(pagina);
            inserirSeNaoForPaginaEmAndamentoRecuperada();
            entrarNaPagina();
            percorrerPublicacoesECurtir();
            finalizarSeForContinuarRoboEZerarPaginaEmAndamentoId();
        }
    }

    private void inserirSeNaoForPaginaEmAndamentoRecuperada() {
        if (paginaEmAndamentoId == 0) {
            paginaEmAndamentoId = dao.inserirPaginaCurtida(conta.getEmail(), paginaAtual.getUrl());
        }
    }

    private void recuperarPaginaEmAndamentoEReordenarPaginas() {
        PaginaCurtidaDto paginaEmAndamento = dao.selecionarPaginaCurtidaEmAndamento(conta.getEmail());
        paginas = conta.getPaginas();
        if (paginaEmAndamento != null) {
            paginas = conta.getPaginasReordenadas(paginaEmAndamento.getUrl());
            paginaEmAndamentoId = paginaEmAndamento.getId();
        }
    }

    private void inicializarVariaveis(Pagina pagina) {
        paginaAtual = pagina;
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
            String pathFile = concat(roboRootFolder, "/Curtidor/", conta.getEmailComoPasta(), "/exception/", getDiaHoraMinutoSegundo(), ".png");
            tirarPrintScreen(driverService.getDriver(), pathFile);
        }
        if (contadorPararDeCurtir < 10 && continuarRobo() && contadorCurtidas < 50) {
            indexAtual++;
            percorrerPublicacoesECurtir();
        }
    }

    private void finalizarSeForContinuarRoboEZerarPaginaEmAndamentoId() {
        if (continuarRobo()) {
            dao.finalizarPaginaCurtida(paginaEmAndamentoId, contadorCurtidas, contadorPararDeCurtir);
        }
        paginaEmAndamentoId = 0;
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
            tirarPrintScreen(driverService.getDriver(), getNomeArquivoScreenshot());
            contadorCurtidas++;
            return true;
        }
        contadorPararDeCurtir++;
        return false;
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
        return concat(roboRootFolder, "/Curtidor/", conta.getEmailComoPasta(), "/", paginaAtual.getUrlComoPasta(),
                "/", getDiaHoraMinutoSegundo(), "_", getSomenteLetrasENumeros(paginaAtual.getNome()), "_", indexAtual, "_", posicaoAtual, ".png");
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
