package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import carregamento_de_horário.HorarioLoader;
import carregamento_de_horário.SaveFiles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe de teste para a classe {@link SaveFiles}.
 */
public class SaveFilesTest {

    private static final String HTML_FILE = "teste-HTML.html";

    /**
     * Testa o método {@link SaveFiles#salvarArquivoHTML(String)} com conteúdo nulo.
     * Deve imprimir uma mensagem indicando que o conteúdo do arquivo HTML não pode ser nulo ou vazio.
     */
    @Test
    public void testSalvarArquivoHTMLConteudoNulo() {
        // Redefine a saída padrão para capturar a saída do sistema
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Executa o método salvarArquivoHTML com conteúdo nulo
        SaveFiles.salvarArquivoHTML(null);

        // Restaura a saída padrão
        System.setOut(System.out);

        // Verifica se a saída contém a mensagem esperada
        assertTrue(outContent.toString().contains("O conteúdo do ficheiro HTML não pode ser nulo ou vazio."));
    }

    /**
     * Testa o método {@link SaveFiles#salvarArquivoHTML(String)} com conteúdo vazio.
     * Deve imprimir uma mensagem indicando que o conteúdo do arquivo HTML não pode ser nulo ou vazio.
     */
    @Test
    public void testSalvarArquivoHTMLConteudoVazio() {
        // Redefine a saída padrão para capturar a saída do sistema
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Executa o método salvarArquivoHTML com conteúdo vazio
        SaveFiles.salvarArquivoHTML("");

        // Restaura a saída padrão
        System.setOut(System.out);

        // Verifica se a saída contém a mensagem esperada
        assertTrue(outContent.toString().contains("O conteúdo do ficheiro HTML não pode ser nulo ou vazio."));
    }

    /**
     * Testa o método {@link SaveFiles#saveHTMLToFile(String, String)} ao salvar um conteúdo HTML em um arquivo
     * e compara com o arquivo HTML esperado.
     *
     * @throws IOException se ocorrer um erro de I/O durante o teste.
     */
    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        // Testa se, ao guardar o conteúdo em um arquivo, o arquivo tem o conteúdo esperado
        String expectedHtml = "<html><body><p>Linha de teste</p></body></html>";
        SaveFiles.saveHTMLToFile(HTML_FILE, expectedHtml);
        String actualHtml = fileAsString(HTML_FILE);
        assertEquals(expectedHtml, actualHtml);
    }

    /**
     * Testa o método {@link SaveFiles#saveHTMLToFile(String, String)} ao tentar salvar conteúdo HTML nulo.
     * Deve verificar se o arquivo não foi criado.
     *
     * @throws IOException se ocorrer um erro de I/O durante o teste.
     */
    @Test
    public void testSaveHTMLToFileWithNullHtmlContent() throws IOException {
        // Tenta guardar conteúdo nulo em um arquivo
        SaveFiles.saveHTMLToFile(HTML_FILE, null);
        assertFalse(new File(HTML_FILE).exists());
    }

    /**
     * Testa o método {@link SaveFiles#saveHTMLToFile(String, String)} ao tentar salvar conteúdo HTML vazio.
     * Deve verificar se o arquivo não foi criado.
     *
     * @throws IOException se ocorrer um erro de I/O durante o teste.
     */
    @Test
    public void testSaveHTMLToFileWithEmptyHtmlContent() throws IOException {
        // Tenta guardar conteúdo vazio em um arquivo
        SaveFiles.saveHTMLToFile(HTML_FILE, "");
        assertFalse(new File(HTML_FILE).exists());
    }

    /**
     * Lê o conteúdo de um arquivo como uma string.
     *
     * @param filePath o caminho do arquivo.
     * @return o conteúdo do arquivo como uma string.
     * @throws IOException se ocorrer um erro de I/O durante a leitura do arquivo.
     */
    private String fileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}
