package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.dto.Comando;

public class Main {

    public static void main(String[] args) {
        Comando comando = args.length > 0 ? Comando.valueOf(args[0]) : Comando.EXECUTAR_NAVEGADOR_ABERTO;
        String arquivoConta = args.length > 1 ? args[1] : "";
        new App().start(comando, arquivoConta);
    }
}
