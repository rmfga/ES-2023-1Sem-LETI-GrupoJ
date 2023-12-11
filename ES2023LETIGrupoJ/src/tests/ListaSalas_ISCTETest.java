package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.Horario_ISCTE;
import carregamento_de_horario.ListaSalas_ISCTE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de teste para {@link ListaSalas_ISCTE}.
 */
public class ListaSalas_ISCTETest {

    private static final String CSV_FILE = "teste-salas.csv";
    private ListaSalas_ISCTE listaSalasISCTE;

    /**
     * Configuração inicial para os testes, cria um arquivo CSV de teste.
     *
     * @throws IOException se houver um erro de I/O.
     */
    @BeforeEach
    void setUp() throws IOException {
        createCSVFile(CSV_FILE);
        listaSalasISCTE = new ListaSalas_ISCTE();
    }

    /**
     * Limpeza após os testes, exclui o arquivo criado.
     */
    @AfterEach
    void tearDown() {
        deleteFile(CSV_FILE);
    }

    /**
     * Testa o carregamento da lista de salas.
     *
     * @throws IOException   se houver um erro de I/O.
     * @throws CsvException  se houver um erro relacionado ao CSV.
     */
    @Test
    void testCarregarListaSalas() throws IOException, CsvException {
        assertFalse(listaSalasISCTE.isSalasCarregada());

        listaSalasISCTE.carregarListaSalas(CSV_FILE);

        assertTrue(listaSalasISCTE.isSalasCarregada());
        assertNotNull(listaSalasISCTE.getSalas());
        assertNotNull(listaSalasISCTE.getHtmlContent());
        assertNotNull(listaSalasISCTE.getHeaderColumns());
        assertNotNull(listaSalasISCTE.getSalaMap());
    }

    /**
     * Testa a adição de registros à lista de salas.
     */
    @Test
    void testAdicionarRegistrosAoHorario() {
        assertFalse(listaSalasISCTE.isSalasCarregada());

        List<List<String>> registros = Arrays.asList(Arrays.asList("101", "Sala A", "50"),
                Arrays.asList("102", "Sala B", "30"));

        listaSalasISCTE.adicionarRegistrosAoHorario(registros);

        assertNotNull(listaSalasISCTE.getSalas(), "A lista de salas não deveria ser nula");
        assertEquals(2, listaSalasISCTE.getSalas().size(), "A lista de salas deveria ter 2 elementos");
    }

    /**
     * Testa a obtenção do índice de uma coluna.
     *
     * @throws IOException   se houver um erro de I/O.
     * @throws CsvException  se houver um erro relacionado ao CSV.
     */
    @Test
    void testGetColumnIndex() throws IOException, CsvException {
        listaSalasISCTE.carregarListaSalas(CSV_FILE);

        int index = listaSalasISCTE.getColumnIndex("Sala");
        assertEquals(1, index);

        index = listaSalasISCTE.getColumnIndex("Inexistente");
        assertEquals(-1, index);
    }

    private void createCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Número;Sala;Capacidade\n");
            writer.write("101;Sala A;50\n");
            writer.write("102;Sala B;30\n");
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
