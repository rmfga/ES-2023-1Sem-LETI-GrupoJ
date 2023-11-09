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

    private String testCsvFilePath;
    private String testHtmlFilePath;

    @Before
    public void setUp() {
        testCsvFilePath = "test-horario.csv";
        testHtmlFilePath = "test-output.html";
    }

    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
        // Cria um ficheiro CSV apenas usado para teste
        createSampleCSVFile(testCsvFilePath);

        // Resultado esperado do loadHorarioFromCSV_OK
        String expectedHtml = "<html><body><table border='1'><tr><td>Test</td></tr></table></body></html>";

        // Dar run ao loadHorarioFromCSV_OK e obtem o seu conteudo em String
        String actualHtml = HorarioLoader.loadHorarioFromCSV_OK(testCsvFilePath);

        // Compara o conteudo HTML com o esperado
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        // Criamos uma String de HTML
        String expectedHtml = "<html><body><p>Test content</p></body></html>";

        // Testamos a função de salvar o HTML num ficheiro
        HorarioLoader.saveHTMLToFile(testHtmlFilePath, expectedHtml);

        // Comparamos os conteudos
        String actualHtml = loadFileAsString(testHtmlFilePath);
        assertEquals(expectedHtml, actualHtml);
    }

    // Cria um ficheiro CSV para testar
    private void createSampleCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Test");
        }
    }

    // Cria uma String com o conteudo do ficheiro
    private String loadFileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}