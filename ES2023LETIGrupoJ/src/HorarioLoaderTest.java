import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;
import java.nio.file.Path;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HorarioLoaderTest {
	
	//Strings necessarias
    private String testCsvFile;
    private String testHtmlFile;

    @Before
    public void setUp() {
        testCsvFile = "teste-horario.csv";
        testHtmlFile = "teste-HTML.html";
    }

    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
        // Cria um ficheiro CSV apenas usado para teste
        createCSVFile(testCsvFile);

        // Resultado esperado do loadHorarioFromCSV_OK
        String expectedHtml = "<html><body><table border='1'><tr><td>Teste JUnit Projeto</td></tr></table></body></html>";

        // Dar run ao loadHorarioFromCSV_OK e obtem o seu conteudo em String
        String actualHtml = HorarioLoader.loadHorarioFromCSV_OK(testCsvFile);

        // Compara o conteudo HTML com o esperado
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        // Criamos uma String de HTML
        String expectedHtml = "<html><body><p>Test content</p></body></html>";

        // Testamos a função de salvar o HTML num ficheiro
        HorarioLoader.saveHTMLToFile(testHtmlFile, expectedHtml);

        // Comparamos os conteudos
        String actualHtml = fileAsString(testHtmlFile);
        assertEquals(expectedHtml, actualHtml);
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