package carregamento_de_horario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.opencsv.exceptions.CsvException;

/**
 * A classe HorarioLoader oferece funcionalidades para carregamento de horários a partir de um arquivo CSV.
 * Este código está implementado para suportar os pontos 1 e 2 do projeto.
 */
public class HorarioLoader {

    ////////////////////////////////////////////////////////////////////////////////

    // Este código corre quando o botão, dentro da TAB Carregamento_Horario é
    // pressionado
    // Abre em HTML um arquivo CSV contendo o Horário completo do ISCTE 

    ////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Esta função lê apenas o registro do horário, excluindo o cabeçalho (nome das colunas).
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Uma lista de listas de strings representando os registros do horário.
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

            // Ignora a linha do cabeçalho, já que esta função lê apenas os dados (registros) do horário sem o nome das colunas
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.asList(values));
            }
        }

        return records;
    }
	
    /**
     * Ao contrário da função anterior, esta função lê apenas a linha do cabeçalho (nome das colunas).
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Uma lista de strings que representa os nomes das colunas.
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
     * Carrega o horário a partir de um arquivo CSV e retorna o conteúdo em formato HTML.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @return Conteúdo HTML que representa o horário.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     * @throws CsvException Se ocorrer um erro durante o processamento do CSV.
     */
    public static String loadHorarioFromCSV(String csvFilePath) throws IOException, CsvException {
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("O caminho do arquivo CSV não pode ser nulo ou vazio.");
            return null;
        }

        List<List<String>> records = loadRegistrosFromCSV(csvFilePath);

        String[] headerColumns = lerNomesColunasDoCSV(csvFilePath).toArray(new String[0]);

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
                + "		<meta charset='utf-8' />\n"
                + "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
                + "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
                + "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>	\n"
                + "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n"
                + "\n" + "			var tabledata = [ \n");

        for (Iterator<List<String>> rowIterator = records.iterator(); rowIterator.hasNext();) {
            List<String> row = (List<String>) rowIterator.next();
            htmlContent.append("\t{");

            // Adiciona cada coluna ao objeto JavaScript
            Iterator<String> columnIterator = row.iterator();
            int columnIndex = 0;

            while (columnIterator.hasNext()) {
                String column = columnIterator.next();

                // Extrai o nome da coluna (por exemplo, colunaCurso) da lista de registros
                String columnName = "coluna" + columnIndex;

                // Adiciona a coluna ao objeto JavaScript
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

        // Ajuste: começamos a partir do segundo elemento
        for (int columnIndex = 0; columnIndex < headerColumns.length; columnIndex++) {
            htmlContent.append("					{title:'").append(headerColumns[columnIndex])
                    .append("', field:'coluna").append(columnIndex).append("', headerFilter:'input'},\n");
        }

        htmlContent.append(
                "				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

        return htmlContent.toString();
    }
}
