package br.com.bdws.facebookRobot.service;

import br.com.bdws.facebookRobot.dto.ContaFacebook;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;

public class ReaderJsonService {

    private static ReaderJsonService single;

    public ReaderJsonService() {
    }

    public static ReaderJsonService get() {
        if (single == null) {
            single = new ReaderJsonService();
        }
        return single;
    }

    public ContaFacebook buscarContaFacebook() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JFileChooser fileChooser = new JFileChooser();
            configurarJFileChooser(fileChooser);
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                return mapper.readValue(fileChooser.getSelectedFile(), ContaFacebook.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void configurarJFileChooser(JFileChooser fileChooser) {
        fileChooser.setCurrentDirectory(new File("./../"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return ((!file.isDirectory() && file.getName().toLowerCase().endsWith(".json")));
            }

            @Override
            public String getDescription() {
                return "Mostrando somente arquivos.json";
            }
        });
    }
}