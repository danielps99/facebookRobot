package br.com.bdws.facebookRobot.dto;

import java.util.ArrayList;
import java.util.List;

public class ContaFacebook {

    private String email;
    private String passwd;
    private List<Pagina> paginas;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public List<Pagina> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<Pagina> paginas) {
        this.paginas = paginas;
    }

    public List<Pagina> getPaginasReordenadas(String urlPrimeiraPagina) {
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
        primeiras.addAll(ultimas);
        return primeiras;
    }
}
