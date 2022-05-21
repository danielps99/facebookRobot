package br.com.bdws.facebookRobot.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static Connection conn;

    public static Connection getSqliteConnection(String caminhoArquivoBanco) throws SQLException {
        if (conn == null) {
            try {
                String url = "jdbc:sqlite:".concat(caminhoArquivoBanco);
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                if (conn != null) {
                    conn.close();
                }
                throw e;
            }
        }
        return conn;
    }
}