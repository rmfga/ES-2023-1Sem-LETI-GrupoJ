//package mapeamento;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import com.opencsv.exceptions.CsvException;
//
//import carregamento_de_horario.Horario_ISCTE;
//
////Este package contem implementado os pontos 3. e 4. do projeto.
////O objetivo é existir um mapeamento entre os campos dos ficheiros CSV e os campos de ordem definidos no ficheiro de Ordem.
//
//public class Mapeamento {
//
//	public static String loadHorarioFromCSV_CustomOrder(Horario_ISCTE horarioISCTE, List<String> customOrder)
//			throws IOException, CsvException {
//		List<List<String>> horario = horarioISCTE.getHorario();
//
//		// Mapa para mapear nomes originais para identificadores JS amigáveis
//		Map<String, String> columnNameMap = horarioISCTE.generateColumnNameMap(customOrder);
//
//		StringBuilder htmlContent = new StringBuilder();
//		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
//				+ "		<meta charset='utf-8' />\n"
//				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
//				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
//				+ "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>\n"
//				+ "		<div id='example-table'></div>\n" + "		<script type='text/javascript'>\n"
//				+ "			var tabledata = [ \n");
//
//		for (Iterator<List<String>> rowIterator = horario.iterator(); rowIterator.hasNext();) {
//			List<String> row = rowIterator.next();
//			htmlContent.append("\t{");
//
//			for (String columnName : customOrder) {
//				int columnIndex = horarioISCTE.getColumnIndex(columnName);
//
//				if (columnIndex != -1) {
//					String columnValue = columnIndex < row.size() ? row.get(columnIndex) : ""; // Adiciona string
//																								// vazia se não
//																								// houver valor
//					// Use o identificador JS mapeado ao criar a propriedade no HTML
//					String jsFriendlyColumnName = columnNameMap.get(columnName);
//					htmlContent.append(jsFriendlyColumnName).append(": '").append(columnValue).append("',");
//					htmlContent.append("\n\t");
//				}
//			}
//
//			htmlContent.append("},\n");
//		}
//		htmlContent.append("];\n" + "			var table = new Tabulator('#example-table', {\n"
//				+ "				data:tabledata,\n" + "				layout:'fitDatafill',\n"
//				+ "				pagination:'local',\n" + "				paginationSize:10,\n"
//				+ "				paginationSizeSelector:[5, 10, 20, 40],\n" + "				movableColumns:true,\n"
//				+ "				paginationCounter:'rows',\n"
//				+ "				initialSort:[{column:'building',dir:'asc'},],\n" + "				columns:[\n");
//
//		// Utilize a ordem personalizada aqui
//		for (String columnName : customOrder) {
//			String jsFriendlyColumnName = columnNameMap.get(columnName);
//
//			htmlContent.append("    {title:'").append(columnName).append("', field:'").append(jsFriendlyColumnName)
//					.append("', headerFilter:'input'},\n");
//		}
//
//		htmlContent
//				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");
//		
//		String htmlContent1 = htmlContent.toString();
//		
//
//		return htmlContent1;
//	}
//}

package mapeamento;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.Horario_ISCTE;

/**
 * Esta classe implementa os pontos 3 e 4 do projeto. O objetivo é criar um
 * mapeamento entre os campos dos arquivos CSV e os campos de ordem definidos no
 * arquivo de Ordem.
 */
public class Mapeamento {

	/**
	 * Carrega o horário a partir de um arquivo CSV com uma ordem personalizada.
	 *
	 * @param horarioISCTE Objeto Horario_ISCTE contendo o horário a ser carregado.
	 * @param customOrder  Lista contendo a ordem personalizada dos campos.
	 * @return Conteúdo HTML gerado a partir do horário carregado.
	 * @throws IOException  Exceção de E/S se houver um problema ao ler o arquivo
	 *                      CSV.
	 * @throws CsvException Exceção específica para problemas relacionados ao CSV.
	 */
	public static String loadHorarioFromCSV_CustomOrder(Horario_ISCTE horarioISCTE, List<String> customOrder)
			throws IOException, CsvException {
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
					// Usa o identificador JS mapeado ao criar a propriedade no HTML
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

// Utiliza a ordem personalizada aqui
		for (String columnName : customOrder) {
			String jsFriendlyColumnName = columnNameMap.get(columnName);

			htmlContent.append("    {title:'").append(columnName).append("', field:'").append(jsFriendlyColumnName)
					.append("', headerFilter:'input'},\n");
		}

		htmlContent
				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

		String htmlContent1 = htmlContent.toString();

		return htmlContent1;
	}
}