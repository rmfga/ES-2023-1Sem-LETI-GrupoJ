package carregamento_de_horário;

import java.util.List;

public class Sala {
	private String edificio;
	private String nome;
    private String tipo;
    private int capacidade;

    public Sala(String edificio, String nome, String tipo, int capacidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.capacidade = capacidade;
    }

    public String getEdificio() {
    	return edificio;
    }
    
    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }
    
    public void setEdificio(String edificio) {
    	this.edificio = edificio;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }


}
