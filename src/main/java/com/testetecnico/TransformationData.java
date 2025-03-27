package com.testetecnico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class TransformationData {

    public static void run() throws Exception {
        // Caminho do PDF de origem
        File pdfFile = new File("downloads/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf");

        // Carrega o PDF
        PDDocument document = PDDocument.load(pdfFile);

        // Extrator de conteúdo da página
        ObjectExtractor extractor = new ObjectExtractor(document);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

        // Caminho de saída do CSV
        String outputPath = "output/tabela-extraida.csv";
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8");

        // Percorrer todas as páginas
        for (int pageNumber = 1; pageNumber <= document.getNumberOfPages(); pageNumber++) {
            Page page = extractor.extract(pageNumber);
            List<Table> tables = sea.extract(page);

            for (Table table : tables) {
                fw.write("### Página " + pageNumber + " ###\n");

                for (List<RectangularTextContainer> row : table.getRows()) {
                    for (int i = 0; i < row.size(); i++) {
                        String cell = row.get(i).getText().replace(",", "").trim();
                        if (cell.equals("OD")) {
                            cell = "Seg. Odontológica";
                        }
                        if (cell.equals("AMB")) {
                            cell = "Seg. Ambulatorial";
                        }
                        fw.write(cell);
                        if (i < row.size() - 1) {
                            fw.write(",");
                        }
                    }
                    fw.write("\n");
                }

                fw.write("\n");
            }
        }

        fw.close();
        document.close();

        System.out.println("Tabelas extraídas para: " + outputPath);
        String projectPath = System.getProperty("user.dir");
        String downloadPath = projectPath + "/output/tabela-extraida.csv";
        CompactadorZip.compactarArquivo(downloadPath, projectPath + "/output/Teste_Eduardo_Ruiz.zip");
    }
}
