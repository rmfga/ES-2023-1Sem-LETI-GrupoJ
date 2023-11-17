import org.junit.Test;

import com.opencsv.exceptions.CsvException;
import java.nio.file.Path;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HorarioLoaderTest {
	
	//Strings necessarias para testes
    private String testeCsvFile = "teste-horario.csv";
    private String testeHtmlFile = "teste-HTML.html";


    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
        // Cria um ficheiro CSV apenas usado para teste
        createCSVFile(testeCsvFile);

        // Resultado esperado do loadHorarioFromCSV
        String expectedHtml = "<html><body><table border='1'><tr><td>Teste JUnit Projeto</td></tr></table></body></html>";

        // Dar run ao loadHorarioFromCSV e obtem o seu conteudo em String
        String actualHtml = HorarioLoader.loadHorarioFromCSV(testeCsvFile);

        // Compara o conteudo HTML com o esperado
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        // Criamos uma String de HTML
        String expectedHtml = "<html><body><p>Test content</p></body></html>";

        // Testamos a função de salvar o HTML num ficheiro
        HorarioLoader.saveHTMLToFile(testeHtmlFile, expectedHtml);

        // Comparamos os conteudos
        String actualHtml = fileAsString(testeHtmlFile);
        assertEquals(expectedHtml, actualHtml);
    }
    

	// Verifica se o método loadHorarioFromCSV retorna
    //nulo quando o caminho para o arquivo CSV é nulo.
    @Test
    public void testLoadHorarioFromCSVWithNullCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(null));
    }

    //Verifica se o método loadHorarioFromCSV retorna
    //nulo quando o caminho para o arquivo CSV é vazio. 
    @Test
    public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(""));
    }

    //Verifica se o método saveHTMLToFile não cria 
    //um arquivo quando o conteúdo HTML é nulo. 
    @Test
    public void testSaveHTMLToFileWithNullHtmlContent() throws IOException {
        HorarioLoader.saveHTMLToFile("teste.html", null);
        // Verifica se o arquivo não foi criado
        assertFalse(new File("teste.html").exists());
    }

    //Verifica se o método saveHTMLToFile
    //não cria um arquivo quando o conteúdo HTML é vazio.
    @Test
    public void testSaveHTMLToFileWithEmptyHtmlContent() throws IOException {
        HorarioLoader.saveHTMLToFile("teste.html", "");
        // Verifica se o arquivo não foi criado
        assertFalse(new File("teste.html").exists());
    }



    // Cria um ficheiro CSV para testar
    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Teste JUnit Projeto");
        }
    }

    // Cria uma String com o conteudo do ficheiro
    private String fileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}