package application;

import javax.swing.SwingUtilities;

/**
 * Classe principal que contém o método main para iniciar a aplicação.
 */
public class Main {

    /**
     * Método principal que inicia a aplicação Swing.
     * @param args Argumentos da linha de comando (não utilizados neste caso).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.createAndShowGUI();
        });
    }
}