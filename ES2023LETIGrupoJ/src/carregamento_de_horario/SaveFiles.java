package carregamento_de_horario;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.GUI;

public class SaveFiles {

	public static void salvarArquivoHTML(String htmlContent) {
		if (htmlContent == null || htmlContent.isEmpty()) {
			System.out.println("O conteúdo do ficheiro HTML não pode ser nulo ou vazio.");
			return;
		}
		try {
			JFileChooser fileChooser = new JFileChooser();
			
			if (GUI.getDiretorioEscolhido() != null) {
                fileChooser.setCurrentDirectory(new File(GUI.getDiretorioEscolhido()));
            }


			// Configurar o local para salvar o arquivo HTML
			fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));
			fileChooser.setSelectedFile(new File("SalasDeAulaPorTiposDeSala.html"));

			int result = fileChooser.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				// Obter o caminho completo do arquivo HTML
				String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();

				// Salvar o conteúdo HTML no arquivo
				saveHTMLToFile(htmlFilePath, htmlContent);
				htmlFilePath = htmlFilePath.replace("\\", "/");

				// Abrir o arquivo HTML no navegador padrão
				Desktop desk = Desktop.getDesktop();
				desk.browse(new java.net.URI("file://" + htmlFilePath));
			}
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
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
