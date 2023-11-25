package mapeamento;

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

//Este package contem implementado os pontos 3. e 4. do projeto.
//O objetivo é existir um mapeamento entre os campos dos ficheiros CSV e os campos de ordem definidos no ficheiro de Ordem.

public class HorarioLoader {

	public static void main(String[] args) {
		JFrame frame = new JFrame("A Minha Aplicação");
		JButton button = new JButton("Mostrar Salas no Browser Web");
		button.setBounds(20, 20, 250, 50);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					// Carregar horário a partir de um arquivo CSV
					// O codigo seguinte pede ao utilizador que indique a diretoria onde contem o
					// ficheiro 'horario-exemplo.csv' e tambem para o ficheiro
					// SalasDeAulaPorTiposDeSala.html'
					// De seguida o codigo grava no ficheiro 'SalasDeAulaPorTiposDeSala.html'

					// Para facilitar cada elemento do grupo pode comentar a sua diretoria aqui:

					// PFS: 'horario-exemplo_OrdemColunas.csv' |
					// /Users/pedrofs/ISCTE/ES2023LETIGrupoJ/horario-exemplo_OrdemColunas.csv
					// PFS: 'horario-exemplo.csv' |
					// /Users/pedrofs/ISCTE/ES2023LETIGrupoJ/horario-exemplo.csv
					// PFS: 'horario-exemplo_Fenix+.csv' |
					// /Users/pedrofs/ISCTE/ES2023LETIGrupoJ/horario-exemplo_Fenix+.csv
					// PFS: 'SalasDeAulaPorTiposDeSala.html' |
					// /Users/pedrofs/ISCTE/ES2023LETIGrupoJ/SalasDeAulaPorTiposDeSala.html
					// PFS: 'CaracterizaçãoDasSalas.csv' |
					// /Users/pedrofs/ISCTE/ES2023LETIGrupoJ/CaracterizaçãoDasSalas.csv

					// Vasco: 'horario-exemplo.csv' |
					// C:/Users/vasco/OneDrive/Documentos/FicheirosES/horario-exemplo.csv
					// Vasco: 'SalasDeAulaPorTiposDeSala.html' |
					// C:/Users/vasco/OneDrive/Documentos/FicheirosES/SalasDeAulaPorTiposDeSala.html

					Scanner scanner = new Scanner(System.in);

					System.out.print(
							"Indique na consola a diretoria onde contem o arquivo CSV com a ordem das colunas (horario-exemplo_OrdemColunas.csv): ");
					String ordemColunasCsvFilePath = scanner.nextLine();

					// Adicionando a leitura do arquivo horario-exemplo_OrdemColunas.csv
					String[] colunaNames = readColumnNames(ordemColunasCsvFilePath);

					System.out.print(
							"Indique na consola a diretoria onde contem o arquivo CSV do horário (horario-exemplo.csv): ");
					String horarioCsvFilePath = scanner.nextLine();

					// Verificando se a ordem das colunas é a mesma
					if (compareColumnOrder(horarioCsvFilePath, colunaNames)) {
						// Se a ordem das colunas for igual, carregamos o horário usando a ordem
						// predefinida no ficheiro de ORDEM
						String htmlContent = loadHorarioFromCSV_EqualOrder(horarioCsvFilePath, colunaNames);

						// Imprimindo o mapeamento na consola
						System.out.println("O mapeamento foi: " + Arrays.toString(colunaNames));

						System.out.print(
								"Indique na consola a diretoria onde contem do arquivo (SalasDeAulaPorTiposDeSala.html): ");
						String htmlFilePath = scanner.nextLine();
						saveHTMLToFile(htmlFilePath, htmlContent);

						// Abrir o arquivo HTML em um navegador da web
						Desktop desk = Desktop.getDesktop();
						desk.browse(new java.net.URI("file://" + htmlFilePath));
					} else {
						// Se a ordem das colunas não for igual, carregamos o horario com uma nova
						// ORDEM.
						// O HTML vai abrir o novo ficheiro exportado pela plataforma Fenix +
						// Os nomes dos campos que não estiverem definidos no ficheiro de ORDEM aparecem
						// no terminal.

						String htmlContent = loadHorarioFromCSV_DiferentOrder(horarioCsvFilePath);

						// Imprimindo o mapeamento na consola
						System.out.println("O mapeamento foi: "
								+ Arrays.toString(getCommonColumnNames(horarioCsvFilePath, colunaNames)));
						System.out.println("Os novos campos foram abertos no HTML, são eles: "
								+ Arrays.toString(getDifferentColumnNames(horarioCsvFilePath, colunaNames)));

						System.out.print(
								"Indique na consola a diretoria onde contem do arquivo 'SalasDeAulaPorTiposDeSala.html' na sua máquina local: ");
						String htmlFilePath = scanner.nextLine();
						saveHTMLToFile(htmlFilePath, htmlContent);

						// Abrir o arquivo HTML em um navegador da web
						Desktop desk = Desktop.getDesktop();
						desk.browse(new java.net.URI("file://" + htmlFilePath));
					}
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				} catch (CsvException e1) {
					e1.printStackTrace();
				}
			}
		});

		frame.add(button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLayout(null);
		frame.setVisible(true);

		System.out.println("Working Directory = " + System.getProperty("user.dir"));
	}

	////////////////////////////////////////////////////////////////////////////////

	public static String[] readColumnNames(String csvFilePath) throws IOException {
		List<String> columnNames = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String headerLine = br.readLine();
			if (headerLine != null) {
				String[] columns = headerLine.split(";");
				columnNames.addAll(Arrays.asList(columns));
			}
		}
		return columnNames.toArray(new String[0]);
	}

	////////////////////////////////////////////////////////////////////////////////

	public static boolean compareColumnOrder(String csvFilePath, String[] expectedColumnOrder) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String headerLine = br.readLine();
			if (headerLine != null) {
				String[] actualColumnOrder = headerLine.split(";");
				return Arrays.equals(actualColumnOrder, expectedColumnOrder);
			}
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para obter os nomes das colunas a partir do horario-exemplo.csv

	public static String[] getCommonColumnNames(String csvFilePath, String[] expectedColumnOrder) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String headerLine = br.readLine();
			if (headerLine != null) {
				String[] actualColumnOrder = headerLine.split(";");
				List<String> commonColumns = new ArrayList<>();
				for (String columnName : actualColumnOrder) {
					if (Arrays.asList(expectedColumnOrder).contains(columnName)) {
						commonColumns.add(columnName);
					}
				}
				return commonColumns.toArray(new String[0]);
			}
		}
		return new String[0];
	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para obter os nomes das colunas que são diferentes

	public static String[] getDifferentColumnNames(String csvFilePath, String[] expectedColumnOrder)
			throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String headerLine = br.readLine();
			if (headerLine != null) {
				String[] actualColumnOrder = headerLine.split(";");
				List<String> differentColumns = new ArrayList<>();
				for (String columnName : actualColumnOrder) {
					if (!Arrays.asList(expectedColumnOrder).contains(columnName)) {
						differentColumns.add(columnName);
					}
				}
				return differentColumns.toArray(new String[0]);
			}
		}
		return new String[0];
	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para ler o ficherio 'horario-exemplo.csv' se este apresentar uma ordem
	// igual ao ficherio 'horario-exemplo_OrdemColunas.csv'

	public static String loadHorarioFromCSV_EqualOrder(String csvFilePath, String[] colunaNames)
			throws IOException, CsvException {

		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			// Vamos ler a primeira linha fora do loop para obter os nomes das colunas
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
					+ "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>\n"
					+ "		<div id='example-table'></div>\n" + "		<script type='text/javascript'>\n"
					+ "			var tabledata = [ \n");

			for (Iterator<List<String>> rowIterator = records.iterator(); rowIterator.hasNext();) {
				List<String> row = rowIterator.next();
				htmlContent.append("\t{");

				Iterator<String> columnIterator = row.iterator();
				int columnIndex = 0;

				// Ajuste: começamos a partir do segundo elemento

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

	////////////////////////////////////////////////////////////////////////////////

	// Método para ler o ficherio 'horario-exemplo.csv' se este apresentar uma ordem
	// diferente ao ficherio 'horario-exemplo_OrdemColunas.csv'

	public static String loadHorarioFromCSV_DiferentOrder(String csvFilePath) throws IOException, CsvException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			// Vamos ler a primeira linha fora do loop para obter os nomes das colunas
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
					+ "	</head>\n" + "	<body>\n" + "		<H1>Tipos de Salas de Aula</H1>\n"
					+ "		<div id='example-table'></div>\n" + "		<script type='text/javascript'>\n"
					+ "			var tabledata = [ \n");

			for (Iterator<List<String>> rowIterator = records.iterator(); rowIterator.hasNext();) {
				List<String> row = rowIterator.next();
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

			htmlContent.append(
					"				],\n" + "			});\n" + "		</script>\n" + "	</body>\n" + "</html>");

			return htmlContent.toString();
		}
	}

	////////////////////////////////////////////////////////////////////////////////

	// Método para gravar o HTML no ficherio 'SalasDeAulaPorTiposDeSala.html'

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