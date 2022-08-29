package br.com.bdws.facebookRobot.dto;

import java.util.HashSet;

public class Compartilhavel {

    private String url;
    private Boolean incluirPubOriginal;
    private String textoPublicacao;
    private String compartilharComo;
    private HashSet<String> nomesGrupos;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIncluirPubOriginal() {
        return incluirPubOriginal;
    }

    public void setIncluirPubOriginal(Boolean incluirPubOriginal) {
        this.incluirPubOriginal = incluirPubOriginal;
    }

    public String getTextoPublicacao() {
        return textoPublicacao;
    }

    public void setTextoPublicacao(String textoPublicacao) {
        this.textoPublicacao = textoPublicacao;
    }

    public HashSet<String> getNomesGrupos() {
        return nomesGrupos;
    }

    public void setNomesGrupos(HashSet<String> nomesGrupos) {
        this.nomesGrupos = nomesGrupos;
    }

    public String getCompartilharComo() {
        return compartilharComo;
    }

    public void setCompartilharComo(String compartilharComo) {
        this.compartilharComo = compartilharComo;
    }

    public String getUrlComoPasta() {
        return url.replace("https://www.facebook.com/story.php?story_fbid=", "").replaceAll("\\W", "_");
    }
}
