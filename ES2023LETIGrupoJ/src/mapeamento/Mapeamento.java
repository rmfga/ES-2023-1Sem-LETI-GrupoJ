package mapeamento;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import carregamento_de_horário.Horario_ISCTE;

//Este package contem implementado os pontos 3. e 4. do projeto.
//O objetivo é existir um mapeamento entre os campos dos ficheiros CSV e os campos de ordem definidos no ficheiro de Ordem.

public class Mapeamento {

	public static String loadHorarioFromCSV_CustomOrder(Horario_ISCTE horarioISCTE, List<String> customOrder)
			throws IOException {
		List<List<String>> horario = horarioISCTE.getHorario();

		// Mapa para mapear nomes originais para identificadores JS amigáveis
		Map<String, String> columnNameMap = horarioISCTE.generateColumnNameMap(customOrder);

		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
				+ "		<meta charset='utf-8' />\n"
				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
				+ "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>\n"
				+ "		<div id='example-table'></div>\n" + "		<script type='text/javascript'>\n"
				+ "			var tabledata = [ \n");

		for (Iterator<List<String>> rowIterator = horario.iterator(); rowIterator.hasNext();) {
			List<String> row = rowIterator.next();
			htmlContent.append("\t{");

			for (String columnName : customOrder) {
				int columnIndex = horarioISCTE.getColumnIndex(columnName);

				if (columnIndex != -1) {
					String columnValue = columnIndex < row.size() ? row.get(columnIndex) : ""; // Adiciona string
																								// vazia se não
																								// houver valor
					// Use o identificador JS mapeado ao criar a propriedade no HTML
					String jsFriendlyColumnName = columnNameMap.get(columnName);
					htmlContent.append(jsFriendlyColumnName).append(": '").append(columnValue).append("',");
					htmlContent.append("\n\t");
				}
			}

			htmlContent.append("},\n");
		}
		htmlContent.append("];\n" + "			var table = new Tabulator('#example-table', {\n"
				+ "				data:tabledata,\n" + "				layout:'fitDatafill',\n"
				+ "				pagination:'local',\n" + "				paginationSize:10,\n"
				+ "				paginationSizeSelector:[5, 10, 20, 40],\n" + "				movableColumns:true,\n"
				+ "				paginationCounter:'rows',\n"
				+ "				initialSort:[{column:'building',dir:'asc'},],\n" + "				columns:[\n");

		// Utilize a ordem personalizada aqui
		for (String columnName : customOrder) {
			String jsFriendlyColumnName = columnNameMap.get(columnName);

			htmlContent.append("    {title:'").append(columnName).append("', field:'").append(jsFriendlyColumnName)
					.append("', headerFilter:'input'},\n");
		}

		htmlContent
				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

		return htmlContent.toString();
	}

	////////////////////////////////////////////////////////////////////////////////

//	public static String[] readColumnNames(String csvFilePath) throws IOException {
//		List<String> columnNames = new ArrayList<>();
//		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//			String headerLine = br.readLine();
//			if (headerLine != null) {
//				String[] columns = headerLine.split(";");
//				columnNames.addAll(Arrays.asList(columns));
//			}
//		}
//		return columnNames.toArray(new String[0]);
//	}

	////////////////////////////////////////////////////////////////////////////////

//	public static boolean compareColumnOrder(String csvFilePath, String[] expectedColumnOrder) throws IOException {
//		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//			String headerLine = br.readLine();
//			if (headerLine != null) {
//				String[] actualColumnOrder = headerLine.split(";");
//				return Arrays.equals(actualColumnOrder, expectedColumnOrder);
//			}
//		}
//		return false;
//	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para obter os nomes das colunas a partir do horario-exemplo.csv

//	public static String[] getCommonColumnNames(String csvFilePath, String[] expectedColumnOrder) throws IOException {
//		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//			String headerLine = br.readLine();
//			if (headerLine != null) {
//				String[] actualColumnOrder = headerLine.split(";");
//				List<String> commonColumns = new ArrayList<>();
//				for (String columnName : actualColumnOrder) {
//					if (Arrays.asList(expectedColumnOrder).contains(columnName)) {
//						commonColumns.add(columnName);
//					}
//				}
//				return commonColumns.toArray(new String[0]);
//			}
//		}
//		return new String[0];
//	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para obter os nomes das colunas que são diferentes

//	public static String[] getDifferentColumnNames(String csvFilePath, String[] expectedColumnOrder)
//			throws IOException {
//		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//			String headerLine = br.readLine();
//			if (headerLine != null) {
//				String[] actualColumnOrder = headerLine.split(";");
//				List<String> differentColumns = new ArrayList<>();
//				for (String columnName : actualColumnOrder) {
//					if (!Arrays.asList(expectedColumnOrder).contains(columnName)) {
//						differentColumns.add(columnName);
//					}
//				}
//				return differentColumns.toArray(new String[0]);
//			}
//		}
//		return new String[0];
//	}
}