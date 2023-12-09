package carregamento_de_horário;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;

public class ListaSalas_ISCTE {
	private List<List<String>> salas;
	private String htmlContent;
	private boolean listaSalasCarregado;
	private List<String> headerColumns;
	private Map<String, Integer> salaMap;

	public ListaSalas_ISCTE() {
		this.salas = new ArrayList<>();
		this.htmlContent = "";
		this.listaSalasCarregado = false;
		this.headerColumns = new ArrayList<>();
		this.salaMap = new HashMap<>();
	}

	public void carregarListaSalas(String csvFilePath) throws IOException, CsvException {
		if (!listaSalasCarregado) {
			this.salas = ListaSalasLoader.loadRegistrosFromCSV(csvFilePath);
			this.htmlContent = ListaSalasLoader.loadHorarioFromCSV(csvFilePath);
			this.listaSalasCarregado = true;
			this.headerColumns = ListaSalasLoader.lerNomesColunasDoCSV(csvFilePath);
			this.salaMap = ListaSalasLoader.processarCaracterizacaoDasSalas(csvFilePath);
		}
	}

	public List<List<String>> getSalas() {
		return salas;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public Map<String, Integer> getSalaMap() {
		return salaMap;
	}

	public boolean isSalasCarregada() {
		return listaSalasCarregado;
	}

	public void adicionarRegistrosAoHorario(List<List<String>> registros) {
		salas.addAll(registros);
	}

	public List<String> getHeaderColumns() {
		return headerColumns;
	}

	public int getColumnIndex(String columnName) {
		List<String> headerColumns = getHeaderColumns();

		for (int i = 0; i < headerColumns.size(); i++) {
			if (columnName.equals(headerColumns.get(i))) {
				return i;
			}
		}

		return -1; // Retorna -1 se a coluna não for encontrada
	}

}
