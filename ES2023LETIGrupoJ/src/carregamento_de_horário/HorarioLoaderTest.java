//package carregamento_de_horário;
//import org.junit.Test;
//
//import com.opencsv.exceptions.CsvException;
//import java.nio.file.Path;
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//public class HorarioLoaderTest {
//	
//	
//	//Strings necessarias para testes
//    private String testeCsvFile = "teste-horario.csv";
//    private String testeHtmlFile = "teste-HTML.html";
//
//
//    @Test
//    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
//        // Cria um ficheiro CSV apenas usado para teste
//        createCSVFile(testeCsvFile);
//
//        // Resultado esperado do loadHorarioFromCSV
//        String expectedHtml = "<html><body><table border='1'><tr><td>Teste JUnit Projeto</td></tr></table></body></html>";
//
//        // Dar run ao loadHorarioFromCSV e obtem o seu conteudo em String
//        String actualHtml = HorarioLoader.loadHorarioFromCSV(testeCsvFile);
//
//        // Compara o conteudo HTML com o esperado
//        assertEquals(expectedHtml, actualHtml);
//    }
//
//    @Test
//    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
//        // Criamos uma String de HTML
//        String expectedHtml = "<html><body><p>Test content</p></body></html>";
//
//        // Testamos a função de salvar o HTML num ficheiro
//        HorarioLoader.saveHTMLToFile(testeHtmlFile, expectedHtml);
//
//        // Comparamos os conteudos
//        String actualHtml = fileAsString(testeHtmlFile);
//        assertEquals(expectedHtml, actualHtml);
//    }
//    
//
//	// Verifica se o método loadHorarioFromCSV retorna
//    //nulo quando o caminho para o arquivo CSV é nulo.
//    @Test
//    public void testLoadHorarioFromCSVWithNullCsvFilePath() throws IOException, CsvException {
//        assertNull(HorarioLoader.loadHorarioFromCSV(null));
//    }
//
//    //Verifica se o método loadHorarioFromCSV retorna
//    //nulo quando o caminho para o arquivo CSV é vazio. 
//    @Test
//    public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
//        assertNull(HorarioLoader.loadHorarioFromCSV(""));
//    }
//
//    //Verifica se o método saveHTMLToFile não cria 
//    //um arquivo quando o conteúdo HTML é nulo. 
//    @Test
//    public void testSaveHTMLToFileWithNullHtmlContent() throws IOException {
//        HorarioLoader.saveHTMLToFile("teste.html", null);
//        // Verifica se o arquivo não foi criado
//        assertFalse(new File("teste.html").exists());
//    }
//
//    //Verifica se o método saveHTMLToFile
//    //não cria um arquivo quando o conteúdo HTML é vazio.
//    @Test
//    public void testSaveHTMLToFileWithEmptyHtmlContent() throws IOException {
//        HorarioLoader.saveHTMLToFile("teste.html", "");
//        // Verifica se o arquivo não foi criado
//        assertFalse(new File("teste.html").exists());
//    }
//
//
//
//    // Cria um ficheiro CSV para testar
//    private void createCSVFile(String filePath) throws IOException {
//        try (FileWriter writer = new FileWriter(filePath)) {
//            writer.write("Teste JUnit Projeto");
//        }
//    }
//
//    // Cria uma String com o conteudo do ficheiro
//    private String fileAsString(String filePath) throws IOException {
//        Path path = Paths.get(filePath);
//        return Files.readString(path);
//    }
//}
package carregamento_de_horário;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class HorarioLoaderTest {

    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";

    @Before
    public void setUp() throws IOException {
        createCSVFile(CSV_FILE);
    }

    @After
    public void tearDown() {
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }

    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
    	String expectedHtml = "	<head>\r\n"
    			+ "		<meta charset='utf-8' />\r\n"
    			+ "		<link href='https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css' rel='stylesheet'>\r\n"
    			+ "		<script type='text/javascript' src='https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js'></script>\r\n"
    			+ "	</head>\r\n"
    			+ "	<body>\r\n"
    			+ "		<H1>Tipos de Salas de Aula</H1>	\r\n"
    			+ "		<div id='example-table'></div>\r\n"
    			+ "\r\n"
    			+ "		<script type='text/javascript'>\r\n"
    			+ "\r\n"
    			+ "			var tabledata = [ \r\n"
    			+ "];\r\n"
    			+ "			var table = new Tabulator('#example-table', {\r\n"
    			+ "				data:tabledata,\r\n"
    			+ "				layout:'fitDatafill',\r\n"
    			+ "				pagination:'local',\r\n"
    			+ "				paginationSize:10,\r\n"
    			+ "				paginationSizeSelector:[5, 10, 20, 40],\r\n"
    			+ "				movableColumns:true,\r\n"
    			+ "				paginationCounter:'rows',\r\n"
    			+ "				initialSort:[{column:'building',dir:'asc'},],\r\n"
    			+ "				columns:[\r\n"
    			+ "					{title:'Teste JUnit Projeto', field:'coluna0', headerFilter:'input'},\r\n"
    			+ "				],\r\n"
    			+ "			});\r\n"
    			+ "		</script>\r\n"
    			+ "	</body>";
        String actualHtml = HorarioLoader.loadHorarioFromCSV(CSV_FILE);
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testSaveHTMLToFileAndCompareWithHTMLFile() throws IOException {
        String expectedHtml = "<html><body><p>Test content</p></body></html>";
        HorarioLoader.saveHTMLToFile(HTML_FILE, expectedHtml);
        String actualHtml = fileAsString(HTML_FILE);
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testLoadHorarioFromCSVWithNullCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(null));
    }

    @Test
    public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(""));
    }

    @Test
    public void testSaveHTMLToFileWithNullHtmlContent() throws IOException {
        HorarioLoader.saveHTMLToFile("teste.html", null);
        assertFalse(new File("teste.html").exists());
    }

    @Test
    public void testSaveHTMLToFileWithEmptyHtmlContent() throws IOException {
        HorarioLoader.saveHTMLToFile("teste.html", "");
        assertFalse(new File("teste.html").exists());
    }

    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Teste JUnit Projeto");
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