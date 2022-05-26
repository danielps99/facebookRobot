package br.com.bdws.facebookRobot.dao;

import br.com.bdws.facebookRobot.ICommons;
import br.com.bdws.facebookRobot.dto.PaginaCurtidaDto;

import java.io.File;
import java.nio.file.Files;
import java.sql.*;

public class IntermediadorDadosDao implements ICommons {

    private final String caminhoArquivoBanco = getRoboExecucaoFolder().concat("/").concat("facebookRobot.db");

    public IntermediadorDadosDao() {
        if (Files.notExists(new File(caminhoArquivoBanco).toPath())) {
            criarTabelas();
        }
    }

    public int inserirPaginaCurtida(String email, String url) {
        String sql = "INSERT INTO paginacurtida (email, url) VALUES (?,?)";
        try {
            PreparedStatement ps = getSqliteConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, url);
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            error(e);
        }
        return 0;
    }

    public void finalizarPaginaCurtida(int id, int curtidas, int parardecurtir) {
        String sql = "UPDATE paginacurtida SET finalizado = 1, curtidas = ?, parardecurtir = ? WHERE Id = ?";
        try {
            PreparedStatement ps = getSqliteConnection().prepareStatement(sql);
            ps.setInt(1, curtidas);
            ps.setInt(2, parardecurtir);
            ps.setInt(3, id);
            ps.execute();
        } catch (SQLException e) {
            error(e);
        }
    }

    public PaginaCurtidaDto selecionarPaginaCurtidaEmAndamento(String email) {
        String sql = "SELECT id, url FROM paginacurtida where email = ? and finalizado = 0 order by id desc limit 1";
        try {
            PreparedStatement ps = getSqliteConnection().prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PaginaCurtidaDto()
                        .setId(rs.getInt("id"))
                        .setUrl(rs.getString("url"));
            }
        } catch (SQLException e) {
            error(e);
        }
        return null;
    }

    private void criarTabelas() {
        String createPaginaCurtida = concat("CREATE TABLE IF NOT EXISTS paginacurtida (",
                " id INTEGER PRIMARY KEY AUTOINCREMENT,",
                " email text NOT NULL,",
                " url text NOT NULL,",
                " curtidas INTEGER,",
                " parardecurtir INTEGER,",
                " finalizado INTEGER default 0)");
        try {
            Statement stmt = getSqliteConnection().createStatement();
            stmt.execute(createPaginaCurtida);
        } catch (SQLException e) {
            error(e);
        }
    }

    private Connection getSqliteConnection() throws SQLException {
        return Conexao.getSqliteConnection(caminhoArquivoBanco);
    }
}
