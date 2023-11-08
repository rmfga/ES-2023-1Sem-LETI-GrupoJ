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

public class HorarioLoader {

	// Classe funcional

	// Apenas contem funcional o ponto 1. e 2. do projeto

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
					// 'SalasDeAulaPorTiposDeSala.html'
					// De seguida o codigo grava no ficheiro 'SalasDeAulaPorTiposDeSala.html'
					// Para facilitar cada elemento do grupo pode comentar a sua diretoria aqui
					// PFS: 'horario-exemplo.csv' |  /Users/pedrofs/ES2023LETIGrupoJ/horario-exemplo.csv
					// PFS: 'SalasDeAulaPorTiposDeSala.html' |  /Users/pedrofs/ES2023LETIGrupoJ/SalasDeAulaPorTiposDeSala.html				

					Scanner scanner = new Scanner(System.in);

					System.out.print(
							"Indique na consola a diretoria onde contem do arquivo 'horario-exemplo.csv' na sua máquina local seguido de enter: ");
					String csvFilePath = scanner.nextLine();

					String htmlContent = loadHorarioFromCSV_OK(csvFilePath);

					// Salvar o conteúdo HTML em um arquivo

					System.out.print(
							"Indique na consola a diretoria onde contem do arquivo 'SalasDeAulaPorTiposDeSala.html' na sua máquina local: ");
					
					String htmlFilePath = scanner.nextLine();
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

	public static String loadHorarioFromCSV_OK(String csvFilePath) throws IOException, CsvException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
			}
		}

		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<html><body><table border='1'>");

		for (Iterator rowIterator = records.iterator(); rowIterator.hasNext();) {
			List<String> row = (List<String>) rowIterator.next();
			htmlContent.append("<tr>");
			for (Iterator columnIterator = row.iterator(); columnIterator.hasNext();) {
				String column = (String) columnIterator.next();
				htmlContent.append("<td>").append(column).append("</td>");
			}
			htmlContent.append("</tr>");
		}
		htmlContent.append("</table></body></html>");

		return htmlContent.toString();
	}

	public static void saveHTMLToFile(String htmlFilePath, String htmlContent) throws IOException {
		try (CSVWriter writer = new CSVWriter(new FileWriter(htmlFilePath))) {
			writer.writeNext(new String[] { htmlContent });
		}
	}
}