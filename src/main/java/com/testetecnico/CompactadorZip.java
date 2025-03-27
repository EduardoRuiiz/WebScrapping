package com.testetecnico;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompactadorZip {

    public static void compactarPasta(String pastaOrigem, String zipDestino) throws IOException {
        Path origemPath = Paths.get(pastaOrigem);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipDestino))) {
            Files.walk(origemPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(origemPath.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Erro ao adicionar arquivo: " + path + " -> " + e.getMessage());
                        }
                    });
        }
    }

    public static void compactarArquivo(String caminhoArquivo, String zipDestino) throws IOException {
        Path arquivo = Paths.get(caminhoArquivo);
        if (!Files.exists(arquivo) || Files.isDirectory(arquivo)) {
            throw new IllegalArgumentException("O caminho fornecido não é um arquivo válido.");
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipDestino))) {
            ZipEntry zipEntry = new ZipEntry(arquivo.getFileName().toString());
            zipOut.putNextEntry(zipEntry);
            Files.copy(arquivo, zipOut);
            zipOut.closeEntry();
        }
    }
}
