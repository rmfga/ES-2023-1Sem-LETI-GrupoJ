package carregamento_de_horário;

import java.io.IOException;

import com.opencsv.exceptions.CsvException;

public class Horario_ISCTE {
	private String htmlContent;
	private boolean horarioCarregado;

	public Horario_ISCTE() {
		this.htmlContent = "";
		this.horarioCarregado = false;
	}

	public void carregarHorario(String csvFilePath) throws IOException, CsvException {
		this.htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);
		this.horarioCarregado = true;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public boolean isHorarioCarregado() {
		return horarioCarregado;
	}
}
