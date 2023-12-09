package tests;

import carregamento_de_horário.Horario_ISCTE;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.exceptions.CsvException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Horario_ISCTETest {
	
    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";
    private Horario_ISCTE horarioISCTE;

    @Before
    public void setUp() throws IOException {
        createCSVFile(CSV_FILE);
        horarioISCTE = new Horario_ISCTE();
    }

    @After
    public void tearDown() {
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }
    

    @Test
    public void testConstrutor() {
        assertNotNull(horarioISCTE);
        assertNotNull(horarioISCTE.getHorario());
        assertEquals("", horarioISCTE.getHtmlContent());
        assertFalse(horarioISCTE.isHorarioCarregado());
    }
    
    

    @Test
    public void testCarregarHorario() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        assertTrue(horarioISCTE.isHorarioCarregado());
        assertNotNull(horarioISCTE.getHtmlContent());
    }

    @Test
    public void testGetHorario() {
        assertNotNull(horarioISCTE.getHorario());
  
    }

    @Test
    public void testGetHtmlContent() {
        assertEquals("", horarioISCTE.getHtmlContent());
    }

    @Test
    public void testIsHorarioCarregado() {
        assertFalse(horarioISCTE.isHorarioCarregado());
    }
    
    @Test
    public void testAdicionarRegistrosAoHorario() throws IOException, CsvException {
    	horarioISCTE.carregarHorario(CSV_FILE);
    	
        List<List<String>> novosRegistros = new ArrayList<>();
        novosRegistros.add(Arrays.asList("Computação", "150", "Dra. Almeida"));

        horarioISCTE.adicionarRegistrosAoHorario(novosRegistros);
        
        // Verifica se o tamanho do horário aumentou
        assertEquals(2, horarioISCTE.getHorario().size()); 
        
        // Verifica se os novos registros foram adicionados corretamente
        assertEquals("Computação", horarioISCTE.getHorario().get(1).get(0)); 
        assertEquals("150", horarioISCTE.getHorario().get(1).get(1));
        assertEquals("Dra. Almeida", horarioISCTE.getHorario().get(1).get(2));
    }
    
    @Test
    public void testGetHeaderColumns() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        List<String> headerColumns = horarioISCTE.getHeaderColumns();
        assertNotNull(headerColumns);
        assertEquals(3, headerColumns.size());
        assertEquals("Nome do Curso", headerColumns.get(0));
        assertEquals("Número de Alunos", headerColumns.get(1));
        assertEquals("Professor", headerColumns.get(2));
    }


    @Test
    public void testGetColumnIndex() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        assertEquals(0, horarioISCTE.getColumnIndex("Nome do Curso"));
        assertEquals(1, horarioISCTE.getColumnIndex("Número de Alunos"));
        assertEquals(2, horarioISCTE.getColumnIndex("Professor"));
        //Testa quando não existe uma certa coluna, retorna -1
        assertEquals(-1, horarioISCTE.getColumnIndex("Disciplina"));
    }

    @Test
    public void testGenerateColumnNameMap() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        List<String> columnNames = horarioISCTE.getHeaderColumns();
        Map<String, String> columnNameMap = horarioISCTE.generateColumnNameMap(columnNames);
        assertNotNull(columnNameMap);
        assertEquals(3, columnNameMap.size());
        assertTrue(columnNameMap.containsKey("Nome do Curso"));
        assertTrue(columnNameMap.containsKey("Número de Alunos"));
        assertTrue(columnNameMap.containsKey("Professor"));
        assertEquals("Nome_do_Curso", columnNameMap.get("Nome do Curso"));
        assertEquals("Número_de_Alunos", columnNameMap.get("Número de Alunos"));
        assertEquals("Professor", columnNameMap.get("Professor"));
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
}
