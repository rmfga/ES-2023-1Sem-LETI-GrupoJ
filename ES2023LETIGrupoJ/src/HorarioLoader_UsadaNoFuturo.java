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

import javax.swing.JButton;
import javax.swing.JFrame;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

// Classe vem fase de teste 

// No futuro iremos usar esta classe para implementar os filtros no HTML

public class HorarioLoader_UsadaNoFuturo {

	public static void main(String[] args) {
		JFrame frame = new JFrame("A Minha Aplicação");
		JButton button = new JButton("Mostrar Salas no Browser Web");
		button.setBounds(20, 20, 250, 50);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Carregar horário a partir de um arquivo CSV
					String csvFilePath = "/Users/pedrofs/ES2023LETIGrupoJ/horario-exemplo.csv";
					String htmlContent = loadHorarioFromCSV(csvFilePath);

					// Salvar o conteúdo HTML em um arquivo
					String htmlFilePath = "/Users/pedrofs/ES2023LETIGrupoJ/SalasDeAulaPorTiposDeSala_PFS.html";
					saveHTMLToFile(htmlFilePath, htmlContent);

					// Abrir o arquivo HTML em um navegador da web
					Desktop desk = Desktop.getDesktop();
					desk.browse(new java.net.URI("file://" + htmlFilePath));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				} catch (CsvException e1) {
					// TODO Auto-generated catch block
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

	public static String loadHorarioFromCSV(String csvFilePath) throws IOException, CsvException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(";");
		        records.add(Arrays.asList(values));
		    }
		}		
		
		
		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>\n"
				+ "	<head>\n"
				+ "		<meta charset='utf-8' />\n"
				+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\n"
				+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\n"
				+ "	</head>\n"
				+ "	<body>\n"
				+ "		<H1>Tipos de Salas de Aula</H1>	\n"
				+ "		<div id='example-table'></div>\n"
				+ "\n"
				+ "		<script type='text/javascript'>\n"
				+ "\n"
				+ "			var tabledata = [");
		
		for (Iterator rowIterator = records.iterator(); rowIterator.hasNext();) {
			List<String> row = (List<String>) rowIterator.next();
			htmlContent.append("{");
			for (Iterator columnIterator = row.iterator(); columnIterator.hasNext();) {
				String column = (String) columnIterator.next();
				htmlContent.append("coluna:").append(column).append(",");	
			}
			htmlContent.append("},");
		}
		htmlContent.append("			];\n"
				+ "			\n"
				+ "			var table = new Tabulator('#example-table', {\n"
				+ "				data:tabledata,\n"
				+ "				layout:'fitDatafill',\n"
				+ "				pagination:'local',\n"
				+ "				paginationSize:10,\n"
				+ "				paginationSizeSelector:[5, 10, 20, 40],\n"
				+ "				movableColumns:true,\n"
				+ "				paginationCounter:'rows',\n"
				+ "				initialSort:[{column:'building',dir:'asc'},],\n"
				+ "				columns:[\n"
				+ "					{title:'Curso', field:'colunaCurso', headerFilter:'input'},\n"
				+ "					{title:'Unidade Curricular', field:'colunaUnidade', headerFilter:'input'},\n"
				+ "					{title:'Turno', field:'colunaTurno', headerFilter:'input'},\n"
				+ "					{title:'Inscritos no turno', field:'colunaIscritos', headerFilter:'input'},\n"
				+ "					{title:'Dia da semana', field:'colunaDiadasemana', headerFilter:'input'},\n"
				+ "					{title:'Hora início da aula', field:'colunaHorainíciodaaula', headerFilter:'input'},\n"
				+ "					{title:'Hora fim da aula', field:'colunaHorafimdaaula', headerFilter:'input'},\n"
				+ "					{title:'Data da aula', field:'colunaDatadaaula', headerFilter:'input'},\n"
				+ "					{title:'Características da sala pedida para a aula', field:'colunaCaracteristicas', headerFilter:'input'},\n"
				+ "					{title:'Sala atribuída à aula', field:'colunaSalaatribuída', headerFilter:'input'},\n"
				+ "				],\n"
				+ "			});\n"
				+ "		</script>\n"
				+ "		\n"
				+ "	</body>\n"
				+ "</html>");


		return htmlContent.toString();
	}
	
	public static void saveHTMLToFile(String htmlFilePath, String htmlContent) throws IOException {
		try (CSVWriter writer = new CSVWriter(new FileWriter(htmlFilePath))) {
			writer.writeNext(new String[] { htmlContent });
		}
	}
}