import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;

public class HorarioLoader {

    public static void main(String[] args) {
        String arquivoCSV = "horario-exemplo.csv";

        try (FileReader reader = new FileReader(arquivoCSV);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String curso = record.get("Curso");
                String unidadeCurricular = record.get("Unidade Curricular");
                String turno = record.get("Turno");
                String turma = record.get("Turma");
                int inscritos = Integer.parseInt(record.get("Inscritos no turno"));
                String diaSemana = record.get("Dia da semana");
                String horaInicio = record.get("Hora início da aula");
                String horaFim = record.get("Hora fim da aula");
                String dataAula = record.get("Data da aula");
                String caracteristicasSala = record.get("Características da sala pedida para a aula");
                String salaAtribuida = record.get("Sala atribuída à aula");

                System.out.println("Curso: " + curso);
                System.out.println("Unidade Curricular: " + unidadeCurricular);
                System.out.println("Turno: " + turno);
                System.out.println("Turma: " + turma);
                System.out.println("Inscritos no turno: " + inscritos);
                System.out.println("Dia da semana: " + diaSemana);
                System.out.println("Hora início da aula: " + horaInicio);
                System.out.println("Hora fim da aula: " + horaFim);
                System.out.println("Data da aula: " + dataAula);
                System.out.println("Características da sala pedida: " + caracteristicasSala);
                System.out.println("Sala atribuída: " + salaAtribuida);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
