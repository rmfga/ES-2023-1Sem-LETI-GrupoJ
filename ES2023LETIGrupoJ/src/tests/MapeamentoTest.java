package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horário.Horario_ISCTE;
import mapeamento.Mapeamento;

/**
 * Classe de teste para a classe {@link Mapeamento}.
 */
public class MapeamentoTest {

    private static final String CSV_FILE = "teste-horario.csv";
    private static final String HTML_FILE = "teste-HTML.html";
    private Horario_ISCTE horarioISCTE;

    /**
     * Configuração inicial para os testes, cria um arquivo CSV de exemplo e instância um objeto {@link Horario_ISCTE}.
     *
     * @throws IOException se ocorrer um erro de I/O durante a criação do arquivo CSV.
     */
    @Before
    public void setUp() throws IOException {
        createCSVFile(CSV_FILE);
        horarioISCTE = new Horario_ISCTE(); 
    }

    /**
     * Limpeza após os testes, deleta os arquivos criados durante os testes.
     */
    @After
    public void tearDown() {
        deleteFile(CSV_FILE);
        deleteFile(HTML_FILE);
    }

    /**
     * Testa o carregamento do horário a partir de um CSV usando uma ordem de colunas personalizada.
     *
     * @throws IOException   se ocorrer um erro de I/O durante o teste.
     * @throws CsvException  se ocorrer um erro relacionado ao CSV durante o teste.
     */
    @Test
    public void testLoadHorarioFromCSV_CustomOrder() throws IOException, CsvException {
        horarioISCTE.carregarHorario(CSV_FILE);
        List<String> customOrder = Arrays.asList("Nome do Curso", "Número de Alunos", "Professor");

        String htmlContent = Mapeamento.loadHorarioFromCSV_CustomOrder(horarioISCTE, customOrder);

        assertNotNull(htmlContent);
        assertTrue(htmlContent.contains("<html"));
        assertTrue(htmlContent.contains("Tipos de Salas de Aula"));
        assertTrue(htmlContent.contains("example-table"));
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
