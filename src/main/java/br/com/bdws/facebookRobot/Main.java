package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Comando;

public class Main {

    public static void main(String[] args) {
        Comando comando = Comando.valueOf(args[0]);
        new App().start(comando);
    }
}
