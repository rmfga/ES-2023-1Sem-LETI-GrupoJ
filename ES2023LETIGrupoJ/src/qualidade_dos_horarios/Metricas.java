package qualidade_dos_horarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.Horario_ISCTE;

/**
 * Esta classe implementa os pontos 5, 6 e 7 do projeto.
 * O objetivo é que o utilizador escolha a métrica que pretende visualizar na Tabela HTML.
 */
public class Metricas {

    /**
     * Este método carrega o horário a partir de um arquivo CSV e exibe no HTML apenas as aulas que estão sobrelotadas.
     *
     * @param horarioISCTE  O horário do ISCTE.
     * @param caracterizacaoSalasMap Um mapa que caracteriza as salas.
     * @return Uma string que contem o conteúdo HTML da tabela.
     * @throws IOException   Se ocorrer um erro de leitura do arquivo.
     * @throws CsvException Se ocorrer um erro no processamento do CSV.
     */
    public static String loadHorarioFromCSV_button1(Horario_ISCTE horarioISCTE,
                                                     Map<String, Integer> caracterizacaoSalasMap) throws IOException, CsvException {
List<List<String>> horario = horarioISCTE.getHorario();
		final int[] count = { 0 }; // Contador de Salas Sobrelotadas

		// Filtra os registros que atendem à condição
		List<List<String>> filteredRecords = horario.stream().filter(row -> {
			if (row.size() >= 11) {
				String sala = row.get(10);
				int valorAssociado = caracterizacaoSalasMap.getOrDefault(sala, 0);
				int inscritos = Integer.parseInt(row.get(4));

				boolean conditionMet = inscritos > valorAssociado;

				if (conditionMet) {

					count[0]++; // Incrementa o contador sempre que uma sala sobrelotada é encontrada
				}

				return conditionMet;

			} else {
				// Lidar com os casos em que não temos salas atribuidas à aula.

				return false; // Se a linha não tiver sala atribuida é ignorada.
			}

		}).collect(Collectors.toList());

		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
				+ "		<meta charset='utf-8' />\n"
				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
				+ "	</head>\n" + "	<body>\n" + "		<H1>Cálculo das Métricas</H1>	\n"
				+ "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n" + "\n"
				+ "			var tabledata = [ \n");

		for (Iterator rowIterator = filteredRecords.iterator(); rowIterator.hasNext();) {
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

			// Adiciona a nova coluna com o resultado da operação
			int resultadoOperacao = Integer.parseInt(row.get(4)) - caracterizacaoSalasMap.getOrDefault(row.get(10), 0);
			htmlContent.append("coluna").append(columnIndex).append(": '").append(resultadoOperacao).append("',");

			htmlContent.append("},\n");
		}
		htmlContent.append("];\n" + "			var table = new Tabulator('#example-table', {\n"
				+ "				data:tabledata,\n" + "				layout:'fitDatafill',\n"
				+ "				pagination:'local',\n" + "				paginationSize:10,\n"
				+ "				paginationSizeSelector:[5, 10, 20, 40],\n" + "				movableColumns:true,\n"
				+ "				paginationCounter:'rows',\n"
				+ "				initialSort:[{column:'building',dir:'asc'},],\n" + "				columns:[\n");

		// Ajuste: começamos a partir do segundo elemento
		for (int columnIndex = 0; columnIndex < horarioISCTE.getHeaderColumns().size(); columnIndex++) {
			String columnName = horarioISCTE.getHeaderColumns().get(columnIndex);
			int columnIndexInArray = horarioISCTE.getColumnIndex(columnName);

			htmlContent.append("					{title:'").append(columnName).append("', field:'coluna")
					.append(columnIndexInArray).append("', headerFilter:'input'},\n");
		}

		// Adiciona a nova coluna ao final da lista de colunas
		htmlContent.append(" {title:'Número de Estudantes em Aulas com Sobrelotação', field:'coluna")
				.append(horarioISCTE.getHeaderColumns().size()).append("', headerFilter:'input'},\n");

		htmlContent
				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

		JOptionPane.showMessageDialog(null, "Nº de aulas em sobrelotação: " + count[0], "Aviso",
				JOptionPane.WARNING_MESSAGE);

		return htmlContent.toString();

	}

	////////////////////////////////////////////////////////////////////////////////

	// Este código corre quando o 3º botão, dentro da TAB Gestão_Das_Métricas é
	// pressionado
	// Abre no HTML apenas as aulas em que as caracteristicas pedidas foram
	// diferentes das que foram dadas para a aula

	////////////////////////////////////////////////////////////////////////////////    }

    /**
     * Este método carrega o horário a partir de um arquivo CSV e exibe no HTML apenas as aulas que têm características diferentes das especificadas.
     *
     * @param horarioISCTE O horário do ISCTE.
     * @return Uma string contendo o conteúdo HTML da tabela.
     * @throws IOException   Se ocorrer um erro de leitura do arquivo.
     * @throws CsvException  Se ocorrer um erro no processamento do CSV.
     */
    public static String loadHorarioFromCSV_button3(Horario_ISCTE horarioISCTE) throws IOException, CsvException {
        List<List<String>> horario = horarioISCTE.getHorario();
		final int[] count = { 0 }; // Contador de Salas com caracteristicas diferentes

		// Filtrar as linhas com a condição desejada
		List<List<String>> filteredRecords = horario.stream().filter(row -> row.size() >= 11
				&& (("Sala de Aulas normal".equals(row.get(9)) && row.get(10).startsWith("Auditório"))
						|| ("Laboratório de Informática".equals(row.get(9)) && row.get(10).startsWith("Auditório"))))
				.peek(row -> count[0]++) // Incrementa o contador para cada linha que passa pelo filtro
				.collect(Collectors.toList());

		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
				+ "		<meta charset='utf-8' />\n"
				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
				+ "	</head>\n" + "	<body>\n" + "		<H1>Cálculo das Métricas</H1>	\n"
				+ "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n" + "\n"
				+ "			var tabledata = [ \n");

		for (Iterator rowIterator = filteredRecords.iterator(); rowIterator.hasNext();) {
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
		for (int columnIndex = 0; columnIndex < horarioISCTE.getHeaderColumns().size(); columnIndex++) {
			String columnName = horarioISCTE.getHeaderColumns().get(columnIndex);
			int columnIndexInArray = horarioISCTE.getColumnIndex(columnName);

			htmlContent.append("					{title:'").append(columnName).append("', field:'coluna")
					.append(columnIndexInArray).append("', headerFilter:'input'},\n");
		}

		htmlContent
				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

		JOptionPane.showMessageDialog(null,
				"Nº de aulas realizadas em salas que não têm as características solicitadas: " + count[0], "Aviso",
				JOptionPane.WARNING_MESSAGE);

		return htmlContent.toString();
	}

	////////////////////////////////////////////////////////////////////////////////

	// Este código corre quando o 4º botão, dentro da TAB Gestão_Das_Métricas é
	// pressionado
	// Abre no HTML apenas as aulas que não têm sala atribuída

	////////////////////////////////////////////////////////////////////////////////
    

    /**
     * Este método carrega o horário a partir de um arquivo CSV e exibe no HTML apenas as aulas que não têm sala atribuída.
     *
     * @param horarioISCTE O horário do ISCTE.
     * @return Uma string contendo o conteúdo HTML da tabela.
     * @throws IOException   Se ocorrer um erro de leitura do arquivo.
     * @throws CsvException  Se ocorrer um erro no processamento do CSV.
     */
    public static String loadHorarioFromCSV_button4(Horario_ISCTE horarioISCTE) throws IOException, CsvException {
        List<List<String>> horario = horarioISCTE.getHorario();
		final int[] count = { 0 }; // Contador de Aulas sem Sala Atribuida

		// Filtrar as linhas com tamanho igual a 10, ou seja a coluna da sala atribuida
		// está vazia
		// Incrementa o contador para cada linha que passa pelo filtro
		List<List<String>> filteredRecords = horario.stream().filter(row -> row.size() == 10).peek(row -> count[0]++) 
				.collect(Collectors.toList());

		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n" + "	<head>\n"
				+ "		<meta charset='utf-8' />\n"
				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
				+ "	</head>\n" + "	<body>\n" + "		<H1>Cálculo das Métricas</H1>	\n"
				+ "		<div id='example-table'></div>\n" + "\n" + "		<script type='text/javascript'>\n" + "\n"
				+ "			var tabledata = [ \n");

		for (Iterator rowIterator = filteredRecords.iterator(); rowIterator.hasNext();) {
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
		for (int columnIndex = 0; columnIndex < 10; columnIndex++) {
			String columnName = horarioISCTE.getHeaderColumns().get(columnIndex);
			int columnIndexInArray = horarioISCTE.getColumnIndex(columnName);

			htmlContent.append("					{title:'").append(columnName).append("', field:'coluna")
					.append(columnIndexInArray).append("', headerFilter:'input'},\n");
		}

		htmlContent
				.append("				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

		JOptionPane.showMessageDialog(null, "Número de aulas sem sala atribuída: " + count[0], "Aviso",
				JOptionPane.WARNING_MESSAGE);

		return htmlContent.toString(); 
   }
}
