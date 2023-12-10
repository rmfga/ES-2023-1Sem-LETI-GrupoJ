package carregamento_de_horario;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.GUI;

/**
 * Classe que fornece métodos para salvar conteúdo HTML em um arquivo e abrir o arquivo salvo no navegador padrão.
 */
public class SaveFiles {

    /**
     * Salva o conteúdo HTML em um arquivo e abre o arquivo no navegador padrão.
     *
     * @param htmlContent O conteúdo HTML a ser salvo.
     */
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

    /**
     * Salva o conteúdo HTML em um arquivo.
     *
     * @param htmlFilePath O caminho do arquivo HTML.
     * @param htmlContent  O conteúdo HTML a ser salvo.
     * @throws IOException Se ocorrer um erro durante a escrita no arquivo.
     */
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
