package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.ListaSalasLoader;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Classe de teste para a ListaSalasLoader.
 */
public class ListaSalasLoaderTest {

	private static final String CSV_FILE = "teste-horario.csv";
	private static final String HTML_FILE = "teste-HTML.html";

	/**
	 * Configuração inicial para os testes, cria um arquivo CSV de teste.
	 *
	 * @throws IOException se houver um erro de I/O.
	 */
	@Before
	public void setUp() throws IOException {
		// cria ficheiro CSV com cabeçalho e uma linha de exemplo
		createCSVFile(CSV_FILE);
	}

	/**
	 * Limpeza após os testes, exclui os arquivos criados.
	 */
	@After
	public void tearDown() {
		// elimina ficheiros criados para os testes
		deleteFile(CSV_FILE);
		deleteFile(HTML_FILE);
	}

	/**
	 * Testa o processamento da caracterização das salas.
	 *
	 * @throws IOException se houver um erro de I/O.
	 */
	@Test
	public void testProcessarCaracterizacaoDasSalas() throws IOException {

		Map<String, Integer> salaMap = ListaSalasLoader.processarCaracterizacaoDasSalas(CSV_FILE);

		assertNotNull(salaMap);
		assertEquals(2, salaMap.size());
		assertEquals(50, salaMap.get("Sala A"));
		assertEquals(30, salaMap.get("Sala B"));
	}

	/**
	 * Testa o carregamento do horário a partir do CSV.
	 *
	 * @throws IOException  se houver um erro de I/O.
	 * @throws CsvException se houver um erro relacionado ao CSV.
	 */
	@Test
	public void testLoadHorarioFromCSVAndCompareWithCSVFile() throws IOException, CsvException {
		// Verifica se o Horario carregado contém as informações necessárias
		String actualHtml = ListaSalasLoader.loadHorarioFromCSV(CSV_FILE);
		assertTrue(actualHtml.contains("<H1>Tipos de Salas de Aula</H1>"));
		assertTrue(actualHtml.contains("var table = new Tabulator('#example-table', {"));
	}

	/**
	 * Testa o carregamento do horário a partir de um caminho de CSV nulo.
	 *
	 * @throws IOException  se houver um erro de I/O.
	 * @throws CsvException se houver um erro relacionado ao CSV.
	 */
	@Test
	public void testLoadHorarioFromCSVWithNullCsvFilePath() throws IOException, CsvException {
		assertNull(ListaSalasLoader.loadHorarioFromCSV(null));
	}

	/**
	 * Testa o carregamento do horário a partir de um caminho de CSV vazio.
	 *
	 * @throws IOException  se houver um erro de I/O.
	 * @throws CsvException se houver um erro relacionado ao CSV.
	 */
	@Test
	public void testLoadHorarioFromCSVWithEmptyCsvFilePath() throws IOException, CsvException {
		assertNull(ListaSalasLoader.loadHorarioFromCSV(""));
	}

	/**
	 * Testa o carregamento de registros do CSV.
	 *
	 * @throws IOException  se houver um erro de I/O.
	 * @throws CsvException se houver um erro relacionado ao CSV.
	 */
	@Test
	public void testLoadRegistrosFromCSV() throws IOException, CsvException {
		// verifica se é permitido carregar registros de ficheiro de caminho vazio ou
		// nulo
		assertNull(ListaSalasLoader.loadRegistrosFromCSV(""));
		assertNull(ListaSalasLoader.loadRegistrosFromCSV(null));

		List<List<String>> records3 = ListaSalasLoader.loadRegistrosFromCSV(CSV_FILE);
		// confirma que o numero de linhas de registros esperados vão ao encontro com o
		// esperado
		assertEquals(2, records3.size());

		// Compara os registros carregados do ficheiro com os esperados
		assertEquals("101", records3.get(0).get(0));
		assertEquals("Sala A", records3.get(0).get(1));
		assertEquals("50", records3.get(0).get(2));

		assertEquals("102", records3.get(1).get(0));
		assertEquals("Sala B", records3.get(1).get(1));
		assertEquals("30", records3.get(1).get(2));
	}

	/**
	 * Testa a leitura dos nomes das colunas do CSV.
	 *
	 * @throws IOException se houver um erro de I/O.
	 */
	@Test
	public void testLerNomesColunasDoCSV() throws IOException {
		// Testa se é possível ler nomes de colunas de um ficheiro de um
		// caminho nulo ou vazio
		assertNull(ListaSalasLoader.lerNomesColunasDoCSV(""));
		assertNull(ListaSalasLoader.lerNomesColunasDoCSV(null));
		List<String> headerColumns = ListaSalasLoader.lerNomesColunasDoCSV(CSV_FILE);
		assertNotNull(headerColumns);

		// Confirma a quantidade de colunas
		assertEquals(3, headerColumns.size());

		// Confirma o nome das colunas
		assertEquals("Número", headerColumns.get(0));
		assertEquals("Sala", headerColumns.get(1));
		assertEquals("Capacidade", headerColumns.get(2));
	}

	private void createCSVFile(String filePath) throws IOException {
		try (FileWriter writer = new FileWriter(filePath)) {
			// Adiciona cabecalho ao arquivo CSV
			writer.write("Número;Sala;Capacidade\n");
			// Adiciona duas linhas de exempllo
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
