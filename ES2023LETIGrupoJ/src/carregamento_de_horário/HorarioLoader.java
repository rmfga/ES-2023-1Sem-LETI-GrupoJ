package carregamento_de_horário;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

//Este package contem implementado os pontos 1. e 2. do projeto.
//O objetivo é o carregamento de um horário a partir do ficheiro do horário CSV

public class HorarioLoader {

    ////////////////////////////////////////////////////////////////////////////////

    // Este código corre quando o botão, dentro da TAB Carregament_Horario é
    // pressionado
    // Abre em HTML um ficheiro CSV contendo o Horario completo do ISCTE 

    ////////////////////////////////////////////////////////////////////////////////

	public static String loadHorarioFromCSV(String csvFilePath) throws IOException, CsvException {
		 if (csvFilePath == null || csvFilePath.isEmpty()) {
	            System.out.println("O caminho do arquivo CSV não pode ser nulo ou vazio.");
	            return null;
	        }
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			String headerLine = br.readLine();
			String[] headerColumns = headerLine.split(";");
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
			}

			StringBuilder htmlContent = new StringBuilder();
			htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
					+ "		<meta charset='utf-8' />\n"
					+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
					+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
					+ "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>	\n"
					+ "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n"
					+ "\n" + "			var tabledata = [ \n");

			for (Iterator rowIterator = records.iterator(); rowIterator.hasNext();) {
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

	public static void saveHTMLToFile(String htmlFilePath, String htmlContent) throws IOException {
		if (htmlContent == null || htmlContent.isEmpty()) {
			System.out.println("O conteúdo do ficheiro HTML não pode ser nulo ou vazio.");
			return;
		}
		try (FileWriter writer = new FileWriter(htmlFilePath)) {
			writer.write(htmlContent);
		}
	}
}