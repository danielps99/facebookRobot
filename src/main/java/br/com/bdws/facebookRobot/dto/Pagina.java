package br.com.bdws.facebookRobot.dto;

import java.util.List;

public class Pagina {

    private String nome;
    private String url;
    private String publicacoesXpath;
    private List<String> naoCurtirPalavras;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicacoesXpath() {
        return publicacoesXpath;
    }

    public void setPublicacoesXpath(String publicacoesXpath) {
        this.publicacoesXpath = publicacoesXpath;
    }

    public List<String> getNaoCurtirPalavras() {
        return naoCurtirPalavras;
    }

    public void setNaoCurtirPalavras(List<String> naoCurtirPalavras) {
        this.naoCurtirPalavras = naoCurtirPalavras;
    }

    @Override
    public String toString() {
        return "Página: ".concat(nome).concat(" - ").concat(url);
    }
}
