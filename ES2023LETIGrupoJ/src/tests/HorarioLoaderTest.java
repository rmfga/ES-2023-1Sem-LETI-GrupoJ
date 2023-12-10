package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.HorarioLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Classe de teste para HorarioLoader, de modo a testar o carregamento do Horario
 * a partir de um ficheiro CSV 
 */
public class HorarioLoaderTest {

    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";

    /**
     * Configuração inicial para os testes, cria um arquivo CSV de teste.
     *
     * @throws IOException se houver um erro de I/O.
     */
    @Before
    public void setUp() throws IOException {
        createCSVFile(CSV_FILE);
    }

    /**
     * Limpeza após os testes, exclui os arquivos criados.
     */
    @After
    public void tearDown() {
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }

    /**
     * Testa o carregamento do horário a partir de um arquivo CSV e 
     * compara com o conteúdo HTML esperado do arquivo CSV.
     *
     * @throws IOException   se houver um erro de I/O.
     * @throws CsvException  se houver um erro relacionado ao CSV.
     */
    @Test
    public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
        String actualHtml = HorarioLoader.loadHorarioFromCSV(CSV_FILE);
        assertTrue(actualHtml.contains("<H1>Tipos de Salas de Aula</H1>"));
        assertTrue(actualHtml.contains("var table = new Tabulator('#example-table', {"));
    }



    /**
     * Testa o carregamento do horário a partir de um caminho vazio.
     *
     * @throws IOException   se houver um erro de I/O.
     * @throws CsvException  se houver um erro relacionado ao CSV.
     */
    @Test
    public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
        assertNull(HorarioLoader.loadHorarioFromCSV(""));
    }


    @Test
    public void testLoadRegistrosFromCSV() throws IOException, CsvException {
        assertNull(HorarioLoader.loadRegistrosFromCSV(""));
        assertNull(HorarioLoader.loadRegistrosFromCSV(null));

        List<List<String>> records = HorarioLoader.loadRegistrosFromCSV(CSV_FILE);
        assertEquals(1, records.size());

        assertEquals("Engenharia", records.get(0).get(0));
        assertEquals("100", records.get(0).get(1));
        assertEquals("Dr. Silva", records.get(0).get(2));
    }

    /**
     * Testa a leitura dos nomes das colunas de um arquivo CSV.
     *
     * @throws IOException se houver um erro de I/O.
     */
    @Test
    public void testLerNomesColunasDoCSV() throws IOException {
        assertNull(HorarioLoader.lerNomesColunasDoCSV(""));
        assertNull(HorarioLoader.lerNomesColunasDoCSV(null));

        List<String> headerColumns = HorarioLoader.lerNomesColunasDoCSV(CSV_FILE);
        assertNotNull(headerColumns);

        assertEquals(3, headerColumns.size());
        assertEquals("Nome do Curso", headerColumns.get(0));
        assertEquals("Número de Alunos", headerColumns.get(1));
        assertEquals("Professor", headerColumns.get(2));
    }

    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Nome do Curso;Número de Alunos;Professor\n");
            writer.write("Engenharia;100;Dr. Silva\n");
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
