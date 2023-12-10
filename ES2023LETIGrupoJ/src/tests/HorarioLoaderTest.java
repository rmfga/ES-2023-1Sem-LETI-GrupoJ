package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horário.HorarioLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HorarioLoaderTest {

    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";

    @Before
    public void setUp() throws IOException {
    	//cria ficheiro CSV com cabeçalho e uma linha de exemplo
        createCSVFile(CSV_FILE);
    }

    @After
    public void tearDown() {
    	//elimina ficheiros criados para os testes
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }

    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
    	//Verifica se o Horario carregado contém as informações necessárias
        String actualHtml = HorarioLoader.loadHorarioFromCSV(CSV_FILE);
        assertTrue(actualHtml.contains("<H1>Tipos de Salas de Aula</H1>"));
        assertTrue(actualHtml.contains("var table = new Tabulator('#example-table', {"));
    }

//    @Test
//    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
//    	//Testa se, ao guardar o conteudo num ficheiro, o ficheiro tem o conteudo esperado
//        String expectedHtml = "<html><body><p>Linha de teste</p></body></html>";
//        HorarioLoader.saveHTMLToFile(HTML_FILE, expectedHtml);
//        String actualHtml = fileAsString(HTML_FILE);
//        assertEquals(expectedHtml, actualHtml);
//    }

    @Test
    //Tenta dar load a um ficheiro num caminho nulo
    public void testLoadHorarioFromCSVWithNullCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(null));
    }

    @Test
    //Tenta dar load a um ficheiro num caminho vazio
    public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(""));
    }

//    @Test
//    public void testSaveHTMLToFileWithNullHtmlContent() throws IOException {
//    	//Tenta guardar conteudo nulo num ficheiro
//        HorarioLoader.saveHTMLToFile("teste.html", null);
//        assertFalse(new File("teste.html").exists());
//    }

//    @Test
//    public void testSaveHTMLToFileWithEmptyHtmlContent() throws IOException {
//    	//Tenta guardar conteudo vazio num ficheiro
//        HorarioLoader.saveHTMLToFile("teste.html", "");
//        assertFalse(new File("teste.html").exists());
//    }
    
    @Test
    public void testLoadRegistrosFromCSV() throws IOException, CsvException {
    	//verifica se é permitido carregar registros de ficheiro de caminho vazio ou nulo
    	assertNull(HorarioLoader.loadRegistrosFromCSV(""));
    	assertNull(HorarioLoader.loadRegistrosFromCSV(null));
    	
    	List<List<String>> records3 = HorarioLoader.loadRegistrosFromCSV(CSV_FILE);
    	//confirma que o numero de linhas de registos esperados vão ao encontro com o esperado
        assertEquals(1, records3.size()); 
        
        // Compara os registos carregados do ficheiro com os esperados
        assertEquals("Engenharia", records3.get(0).get(0));
        assertEquals("100", records3.get(0).get(1));
        assertEquals("Dr. Silva", records3.get(0).get(2));
    }

    @Test
    public void testLerNomesColunasDoCSV() throws IOException {
    	//Testa se é possivel ler nomes de colunas de um ficheiro de um
    	//caminho nulo ou vazio
    	assertNull(HorarioLoader.lerNomesColunasDoCSV(""));
    	assertNull(HorarioLoader.lerNomesColunasDoCSV(null));
        List<String> headerColumns = HorarioLoader.lerNomesColunasDoCSV(CSV_FILE);
        assertNotNull(headerColumns);
        
        //Confirma a quantidade de colunas
        assertEquals(3, headerColumns.size()); 
        
        //Confirma o nome das colunas
        assertEquals("Nome do Curso", headerColumns.get(0));
        assertEquals("Número de Alunos", headerColumns.get(1));
        assertEquals("Professor", headerColumns.get(2));
    }

    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Adiciona um cabeçalho ao arquivo CSV
            writer.write("Nome do Curso;Número de Alunos;Professor\n");
            // Adiciona uma linha de exemplo
            writer.write("Engenharia;100;Dr. Silva\n");
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private String fileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}
