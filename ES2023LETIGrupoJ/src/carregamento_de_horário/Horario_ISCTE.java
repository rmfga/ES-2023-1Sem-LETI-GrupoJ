package carregamento_de_horário;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.exceptions.CsvException;

public class Horario_ISCTE {
    private List<List<String>> horario;
    private String htmlContent;
    private boolean horarioCarregado;

    public Horario_ISCTE() {
        this.horario = new ArrayList<>();
        this.htmlContent = "";
        this.horarioCarregado = false;
    }

    public void carregarHorario(String csvFilePath) throws IOException, CsvException {
        if (!horarioCarregado) {
            
            this.htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);
            this.horarioCarregado = true;
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
}
