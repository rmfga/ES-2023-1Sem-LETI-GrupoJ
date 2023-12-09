package carregamento_de_horário;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;

public class Horario_ISCTE {
	private List<List<String>> horario;
	private String htmlContent;
	private boolean horarioCarregado;
	private List<String> headerColumns;

	public Horario_ISCTE() {
		this.horario = new ArrayList<>();
		this.htmlContent = "";
		this.horarioCarregado = false;
		this.headerColumns = new ArrayList<>();
	}

	public void carregarHorario(String csvFilePath) throws IOException, CsvException {
		if (!horarioCarregado) {
			this.horario = HorarioLoader.loadRegistrosFromCSV(csvFilePath);
			this.htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);
			this.horarioCarregado = true;
			this.headerColumns = HorarioLoader.lerNomesColunasDoCSV(csvFilePath);
		}
	}

	public List<List<String>> getHorario() {
		return horario;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public boolean isHorarioCarregado() {
		return horarioCarregado;
	}

	public void adicionarRegistrosAoHorario(List<List<String>> registros) {
		horario.addAll(registros);
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
	
	public Map<String, String> generateColumnNameMap(List<String> columnNames) {
        Map<String, String> columnNameMap = new HashMap<>();

        for (String columnName : columnNames) {
            int columnIndex = this.getColumnIndex(columnName);

            if (columnIndex != -1) {
                // Substitua espaços por underscores ou remova espaços
                String jsFriendlyColumnName = columnName.replace(" ", "_"); // ou columnName.replaceAll("\\s", "");

                // Mapeie o nome original para o identificador JS amigável
                columnNameMap.put(columnName, jsFriendlyColumnName);
            }
        }

        return columnNameMap;
    }

}
