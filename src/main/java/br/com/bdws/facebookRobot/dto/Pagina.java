package br.com.bdws.facebookRobot.dto;

import java.util.List;

public class Pagina {

    private String nome;
    private String url;
    private String publicacoesXpath;
    private String curtirComentarCompartilharXpath;
    private List<String> palavrasAIgnorar;
    private List<String> naoCurtirPalavras;
    private List<String> palavrasPreferidas;
    private int index;

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

    public String getCurtirComentarCompartilharXpath() {
        return curtirComentarCompartilharXpath;
    }

    public void setCurtirComentarCompartilharXpath(String curtirComentarCompartilharXpath) {
        this.curtirComentarCompartilharXpath = curtirComentarCompartilharXpath;
    }

    public List<String> getPalavrasAIgnorar() {
        return palavrasAIgnorar;
    }

    public void setPalavrasAIgnorar(List<String> palavrasAIgnorar) {
        this.palavrasAIgnorar = palavrasAIgnorar;
    }

    public List<String> getNaoCurtirPalavras() {
        return naoCurtirPalavras;
    }

    public void setNaoCurtirPalavras(List<String> naoCurtirPalavras) {
        this.naoCurtirPalavras = naoCurtirPalavras;
    }

    public List<String> getPalavrasPreferidas() {
        return palavrasPreferidas;
    }

    public void setPalavrasPreferidas(List<String> palavrasPreferidas) {
        this.palavrasPreferidas = palavrasPreferidas;
    }

    @Override
    public String toString() {
        return index + " - " + nome + " - " + url;
    }
}
