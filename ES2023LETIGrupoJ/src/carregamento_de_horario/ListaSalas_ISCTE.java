package carregamento_de_horario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvException;

/**
 * Esta classe representa a lista de salas do ISCTE, carregando e processando informações a partir de um arquivo CSV.
 */
public class ListaSalas_ISCTE {

    private List<List<String>> salas;
    private String htmlContent;
    private boolean listaSalasCarregado;
    private List<String> headerColumns;
    private Map<String, Integer> salaMap;

    /**
     * Construtor padrão da classe ListaSalas_ISCTE.
     * Inicializa as variáveis necessárias.
     */
    public ListaSalas_ISCTE() {
        this.salas = new ArrayList<>();
        this.htmlContent = "";
        this.listaSalasCarregado = false;
        this.headerColumns = new ArrayList<>();
        this.salaMap = new HashMap<>();
    }

    /**
     * Carrega a lista de salas a partir de um arquivo CSV.
     *
     * @param csvFilePath O caminho do arquivo CSV contendo informações das salas.
     * @throws IOException    Se ocorrer um erro de leitura do arquivo.
     * @throws CsvException   Se ocorrer um erro específico de manipulação de CSV.
     */
    public void carregarListaSalas(String csvFilePath) throws IOException, CsvException {
        if (!listaSalasCarregado) {
            this.salas = ListaSalasLoader.loadRegistrosFromCSV(csvFilePath);
            this.htmlContent = ListaSalasLoader.loadHorarioFromCSV(csvFilePath);
            this.listaSalasCarregado = true;
            this.headerColumns = ListaSalasLoader.lerNomesColunasDoCSV(csvFilePath);
            this.salaMap = ListaSalasLoader.processarCaracterizacaoDasSalas(csvFilePath);
        }
    }

    /**
     * Obtém a lista de salas carregada.
     *
     * @return A lista de salas.
     */
    public List<List<String>> getSalas() {
        return salas;
    }

    /**
     * Obtém o conteúdo HTML da lista de salas.
     *
     * @return O conteúdo HTML.
     */
    public String getHtmlContent() {
        return htmlContent;
    }

    /**
     * Obtém o mapeamento das salas com valores associados.
     *
     * @return O mapa de salas com valores associados.
     */
    public Map<String, Integer> getSalaMap() {
        return salaMap;
    }

    /**
     * Verifica se a lista de salas foi carregada.
     *
     * @return Verdadeiro se a lista de salas foi carregada; falso, caso contrário.
     */
    public boolean isSalasCarregada() {
        return listaSalasCarregado;
    }

    /**
     * Adiciona registos à lista de salas.
     *
     * @param registos Os registros a serem adicionados.
     */
    public void adicionarRegistrosAoHorario(List<List<String>> registros) {
        salas.addAll(registros);
    }

    /**
     * Obtém as colunas de cabeçalho da lista de salas.
     *
     * @return As colunas de cabeçalho.
     */
    public List<String> getHeaderColumns() {
        return headerColumns;
    }

    /**
     * Obtém o índice da coluna a partir do nome da coluna.
     *
     * @param columnName O nome da coluna.
     * @return O índice da coluna ou -1 se a coluna não for encontrada.
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
}
