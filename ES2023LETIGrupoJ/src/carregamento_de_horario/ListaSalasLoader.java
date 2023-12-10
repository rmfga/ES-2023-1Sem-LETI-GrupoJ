package carregamento_de_horario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;

/**
 * Esta classe fornece funcionalidades para carregamento e processamento de dados relacionados às salas de aula.
 * Este pacote contém implementados os pontos 1 e 2 do projeto, com o objetivo de carregar um horário a partir do arquivo CSV do horário.
 */
public class ListaSalasLoader {

    /**
     * Carrega os registros de um arquivo CSV, excluindo o cabeçalho.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Lista de listas de strings representando os registros do arquivo CSV.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     * @throws CsvException Se ocorrer um erro durante o processamento do CSV.
     */
    public static List<List<String>> loadRegistrosFromCSV(String csvFilePath) throws IOException, CsvException {
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("O caminho do arquivo CSV não pode ser nulo ou vazio.");
            return null;
        }

        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.asList(values));
            }
        }

        return records;
    }

    /**
     * Lê os nomes das colunas de um arquivo CSV.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Lista de strings que representa os nomes das colunas.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     */
    public static List<String> lerNomesColunasDoCSV(String csvFilePath) throws IOException {
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("O caminho do arquivo CSV não pode ser nulo ou vazio.");
            return null;
        }

        List<String> headerColumns = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                String[] columns = headerLine.split(";");
                headerColumns.addAll(Arrays.asList(columns));
            }
        }

        return headerColumns;
    }

    /**
     * Processa a caracterização das salas a partir de um arquivo CSV.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Mapa que associa o nome da sala a um valor associado.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     */
    public static Map<String, Integer> processarCaracterizacaoDasSalas(String csvFilePath) throws IOException {
        Map<String, Integer> salaMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length >= 3) {
                    String sala = values[1].trim();
                    int valorAssociado = Integer.parseInt(values[2].trim());
                    salaMap.put(sala, valorAssociado);
                }
            }
        }

        return salaMap;
    }

    /**
     * Carrega o horário a partir de um arquivo CSV e gera conteúdo HTML.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Conteúdo HTML gerado a partir do horário.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     * @throws CsvException Se ocorrer um erro durante o processamento do CSV.
     */
    public static String loadHorarioFromCSV(String csvFilePath) throws IOException, CsvException {
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("O caminho do arquivo CSV não pode ser nulo ou vazio.");
            return null;
        }

        List<List<String>> records = loadRegistrosFromCSV(csvFilePath);

        String[] headerColumns = (lerNomesColunasDoCSV(csvFilePath)).toArray(new String[0]);

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
                + "		<meta charset='utf-8' />\n"
                + "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
                + "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
                + "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>	\n"
                + "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n" + "\n"
                + "			var tabledata = [ \n");

        for (Iterator<List<String>> rowIterator = records.iterator(); rowIterator.hasNext();) {
            List<String> row = (List<String>) rowIterator.next();
            htmlContent.append("\t{");

            Iterator<String> columnIterator = row.iterator();
            int columnIndex = 0;

            while (columnIterator.hasNext()) {
                String column = columnIterator.next();
                String columnName = "coluna" + columnIndex;

                htmlContent.append(columnName).append(": '").append(column).append("',");

                if (columnIterator.hasNext()) {
                    htmlContent.append("\n\t");
                }

                columnIndex++;
            }

            htmlContent.append("},\n");
        }
        htmlContent.append("];\n" + "			var table = new Tabulator('#example-table', {\n"
                + "				data:tabledata,\n" + "				layout:'fitDatafill',\n"
                + "				pagination:'local',\n" + "				paginationSize:10,\n"
                + "				paginationSizeSelector:[5, 10, 20, 40],\n" + "				movableColumns:true,\n"
                + "				paginationCounter:'rows',\n"
                + "				initialSort:[{column:'building',dir:'asc'},],\n" + "				columns:[\n");

        for (int columnIndex = 0; columnIndex < headerColumns.length; columnIndex++) {
            htmlContent.append("					{title:'").append(headerColumns[columnIndex])
                    .append("', field:'coluna").append(columnIndex).append("', headerFilter:'input'},\n");
        }

        htmlContent
                .append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

        return htmlContent.toString();
    }

}

