package br.com.bdws.facebookRobot.dto;

public class PaginaCurtidaDto {

    private int id;
    private String email;
    private String url;
    int curtidas;
    int pararDeCurtir;

    public int getId() {
        return id;
    }

    public PaginaCurtidaDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PaginaCurtidaDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PaginaCurtidaDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public PaginaCurtidaDto setCurtidas(int curtidas) {
        this.curtidas = curtidas;
        return this;
    }

    public int getPararDeCurtir() {
        return pararDeCurtir;
    }

    public PaginaCurtidaDto setPararDeCurtir(int pararDeCurtir) {
        this.pararDeCurtir = pararDeCurtir;
        return this;
    }
}
