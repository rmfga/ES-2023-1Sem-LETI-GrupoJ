package carregamento_de_horario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.exceptions.CsvException;

/**
 * A classe Horario_ISCTE representa um horário do ISCTE, que oferece funcionalidades relacionadas ao carregamento e manipulação dos dados.
 */
public class Horario_ISCTE {

    private List<List<String>> horario;        // Representa os registros do horário
    private String htmlContent;                // Conteúdo HTML do horário
    private boolean horarioCarregado;          // Indica se o horário foi carregado
    private List<String> headerColumns;        // Nomes das colunas do horário
    private List<String> columnOrder;          // Ordem das colunas no horário
    private String horarioCsvFilePath;

    /**
     * Construtor da classe Horario_ISCTE.
     * Inicializa as variáveis da classe.
     */
    public Horario_ISCTE() {
        this.horario = new ArrayList<>();
        this.htmlContent = "";
        this.horarioCarregado = false;
        this.headerColumns = new ArrayList<>();
        this.columnOrder = columnOrderDefault();
        this.horarioCsvFilePath = "";
    }

    /**
     * Carrega o horário a partir de um arquivo CSV.
     *
     * @param csvFilePath Caminho do arquivo CSV.
     * @throws IOException Se ocorrer um erro de E/S durante a leitura do arquivo.
     * @throws CsvException Se ocorrer um erro durante o processamento do CSV.
     */
    public void carregarHorario(String csvFilePath) throws IOException, CsvException {
        if (!horarioCarregado) {
            this.horario = HorarioLoader.loadRegistrosFromCSV(csvFilePath);
            this.htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);
            this.horarioCarregado = true;
            this.headerColumns = HorarioLoader.lerNomesColunasDoCSV(csvFilePath);
        }
    }
    
    public String getHorarioCsvFilePath() {
		return horarioCsvFilePath;
	}

	public void setHorarioCsvFilePath(String horarioCsvFilePath) {
		this.horarioCsvFilePath = horarioCsvFilePath;
	}

    /**
     * Obtém os registos do horário.
     *
     * @return Lista de listas de strings representando os registos do horário.
     */
    public List<List<String>> getHorario() {
        return horario;
    }

    /**
     * Obtém o conteúdo HTML do horário.
     *
     * @return Conteúdo HTML que representa o horário.
     */
    public String getHtmlContent() {
        return htmlContent;
    }

    /**
     * Verifica se o horário foi carregado.
     *
     * @return true se o horário foi carregado, false caso contrário.
     */
    public boolean isHorarioCarregado() {
        return horarioCarregado;
    }

    /**
     * Adiciona registos ao horário.
     *
     * @param registos Lista de listas de strings que representa os registos a serem adicionados.
     */
    public void adicionarRegistrosAoHorario(List<List<String>> registros) {
        horario.addAll(registros);
    }

    /**
     * Obtém os nomes das colunas do horário.
     *
     * @return Lista de strings que representa os nomes das colunas.
     */
    public List<String> getHeaderColumns() {
        return headerColumns;
    }

    /**
     * Obtém o índice de uma coluna pelo seu nome.
     *
     * @param columnName Nome da coluna.
     * @return Índice da coluna ou -1 se a coluna não for encontrada.
     */
    public int getColumnIndex(String columnName) {
        List<String> headerColumns = getHeaderColumns();

        for (int i = 0; i < headerColumns.size(); i++) {
            if (columnName.equals(headerColumns.get(i))) {
                return i;
            }
        }

        return -1; // Retorna -1 se a coluna não for encontrada
    }

    /**
     * Obtém a ordem das colunas no horário.
     *
     * @return Lista de strings que representa a ordem das colunas.
     */
    public List<String> getColumnOrder() {
        
        return columnOrder;
    }

    /**
     * Gera um mapa de nomes de colunas amigáveis para JavaScript.
     *
     * @param columnNames Lista de strings que representa os nomes das colunas.
     * @return Mapa de nomes de colunas originais para identificadores JS amigáveis.
     */
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

    // Método privado para obter a ordem padrão das colunas
    private List<String> columnOrderDefault() {
        List<String> columnOrder = new ArrayList<>();

        // Adiciona os nomes das colunas à lista
        columnOrder.add("Curso");
        columnOrder.add("Unidade Curricular");
        columnOrder.add("Turno");
        columnOrder.add("Turma");
        columnOrder.add("Inscritos no turno");
        columnOrder.add("Dia da semana");
        columnOrder.add("Hora início da aula");
        columnOrder.add("Hora fim da aula");
        columnOrder.add("Data da aula");
        columnOrder.add("Características da sala pedida para a aula");
        columnOrder.add("Sala atribuída à aula");

        return columnOrder;
    }
}