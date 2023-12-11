package tests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.Horario_ISCTE;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Esta classe contém testes JUnit para a classe {@link Horario_ISCTE}.
 * Os testes visam garantir o correto funcionamento dos métodos relacionados ao carregamento e manipulação do horário.
 */
public class Horario_ISCTETest {

    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";
    private Horario_ISCTE horarioISCTE;

    /**
     * Configuração inicial para os testes.
     * Cria um arquivo CSV de teste e instancia a classe {@link Horario_ISCTE}.
     *
     * @throws IOException Se ocorrer um erro ao criar o arquivo CSV.
     */
    @Before
    public void setUp() throws IOException {
        createCSVFile(CSV_FILE);
        horarioISCTE = new Horario_ISCTE();
    }

    /**
     * Limpeza após os testes.
     * Exclui o arquivo CSV de teste e um possível arquivo HTML gerado.
     */
    @After
    public void tearDown() {
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }

    /**
     * Testa o construtor da classe {@link Horario_ISCTE}.
     * Verifica se os objetos são instanciados corretamente.
     */
    @Test
    public void testConstrutor() {
        assertNotNull(horarioISCTE);
        assertNotNull(horarioISCTE.getHorario());
        assertEquals("", horarioISCTE.getHtmlContent());
        assertFalse(horarioISCTE.isHorarioCarregado());
    }

    /**
     * Testa o método {@link Horario_ISCTE#carregarHorario(String)}.
     * Verifica se o horário é carregado corretamente a partir de um arquivo CSV.
     *
     * @throws IOException     Se ocorrer um erro de leitura do arquivo CSV.
     * @throws CsvException    Se ocorrer um erro durante o parsing do arquivo CSV.
     */
    @Test
    public void testCarregarHorario() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        assertTrue(horarioISCTE.isHorarioCarregado());
        assertNotNull(horarioISCTE.getHtmlContent());
    }

    /**
     * Testa o método {@link Horario_ISCTE#getHorario()}.
     * Verifica se o horário não é nulo.
     */
    @Test
    public void testGetHorario() {
        assertNotNull(horarioISCTE.getHorario());
    }
    
    /**
     * Testa o método {@link Horario_ISCTE#getHorarioCsvFilePath()}.
     * Verifica se o conteúdo HTML é uma string vazia inicialmente.
     */
    @Test
    public void testGetHorarioCsvFilePath() {
        assertEquals("", horarioISCTE.getHorarioCsvFilePath());
    }
    
    /**
     * Testa o método Horario_ISCTE.setHorarioCsvFilePath()}.
     * Verifica se o set é feito com sucesso
     */
    @Test
    public void testSetHorarioCsvFilePath() {
        Horario_ISCTE horario = new Horario_ISCTE();

        // Teste 1: Definir o caminho do arquivo CSV
        String caminhoArquivo = "caminho/do/arquivo.csv";
        horario.setHorarioCsvFilePath(caminhoArquivo);

        // Verificar se o caminho do arquivo foi definido corretamente
        assertEquals(caminhoArquivo, horario.getHorarioCsvFilePath());
    }

    /**
     * Testa o metodo Horario_ISCTE.setHorarioCarregado().
     * verifica se o set é realizado com sucesso
     */
    @Test
    public void testSetHorarioCarregado() {
        Horario_ISCTE horario = new Horario_ISCTE();

        // Teste 2: Definir o horário como carregado
        horario.setHorarioCarregado(true);

        // Verificar se o horário foi definido como carregado corretamente
        assertTrue(horario.isHorarioCarregado());

        // Teste 3: Definir o horário como não carregado
        horario.setHorarioCarregado(false);

        // Verificar se o horário foi definido como não carregado corretamente
        assertFalse(horario.isHorarioCarregado());
    }
    
    /**
     * Testa o método {@link Horario_ISCTE#getHtmlContent()}.
     * Verifica se o conteúdo HTML é uma string vazia inicialmente.
     */
    @Test
    public void testGetHtmlContent() {
        assertEquals("", horarioISCTE.getHtmlContent());
    }

    /**
     * Testa o método {@link Horario_ISCTE#getColumnOrder()}.
     * Verifica se a ordem das colunas padrão é retornada corretamente.
     */
    @Test
    public void testGetColumnOrder() {
        List<String> expectedColumnOrder = Arrays.asList(
            "Curso",
            "Unidade Curricular",
            "Turno",
            "Turma",
            "Inscritos no turno",
            "Dia da semana",
            "Hora início da aula",
            "Hora fim da aula",
            "Data da aula",
            "Características da sala pedida para a aula",
            "Sala atribuída à aula"
        );
        List<String> actualColumnOrder = horarioISCTE.getColumnOrder();
        assertNotNull(actualColumnOrder);
        assertEquals(expectedColumnOrder, actualColumnOrder);
    }

    /**
     * Testa o método {@link Horario_ISCTE#adicionarRegistrosAoHorario(List)}.
     * Verifica se novos registros são adicionados corretamente ao horário.
     *
     * @throws IOException     Se ocorrer um erro de leitura do arquivo CSV.
     * @throws CsvException    Se ocorrer um erro durante o parsing do arquivo CSV.
     */
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

    /**
     * Testa o método {@link Horario_ISCTE#getHeaderColumns()}.
     * Verifica se as colunas do cabeçalho são obtidas corretamente.
     *
     * @throws IOException     Se ocorrer um erro de leitura do arquivo CSV.
     * @throws CsvException    Se ocorrer um erro durante o parsing do arquivo CSV.
     */
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

    /**
     * Testa o método {@link Horario_ISCTE#getColumnIndex(String)}.
     * Verifica se os índices das colunas são obtidos corretamente.
     *
     * @throws IOException     Se ocorrer um erro de leitura do arquivo CSV.
     * @throws CsvException    Se ocorrer um erro durante o parsing do arquivo CSV.
     */
    @Test
    public void testGetColumnIndex() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        assertEquals(0, horarioISCTE.getColumnIndex("Nome do Curso"));
        assertEquals(1, horarioISCTE.getColumnIndex("Número de Alunos"));
        assertEquals(2, horarioISCTE.getColumnIndex("Professor"));
        // Testa quando não existe uma certa coluna, retorna -1
        assertEquals(-1, horarioISCTE.getColumnIndex("Disciplina"));
    }

    /**
     * Testa o método {@link Horario_ISCTE#generateColumnNameMap(List)}.
     * Verifica se o mapeamento de nomes de colunas é gerado corretamente.
     *
     * @throws IOException     Se ocorrer um erro de leitura do arquivo CSV.
     * @throws CsvException    Se ocorrer um erro durante o parsing do arquivo CSV.
     */
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

    /**
     * Cria um arquivo CSV de teste.
     *
     * @param filePath O caminho do arquivo CSV a ser criado.
     * @throws IOException Se ocorrer um erro ao criar o arquivo CSV.
     */
    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Adiciona um cabeçalho ao arquivo CSV
            writer.write("Nome do Curso;Número de Alunos;Professor\n");
            // Adiciona uma linha de exemplo
            writer.write("Engenharia;100;Dr. Silva\n");
        }
    }

    /**
     * Exclui um arquivo.
     *
     * @param filePath O caminho do arquivo a ser excluído.
     */
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
