//package tests;
//
//import static org.junit.Assert.*;
//import org.junit.Test;
//
//import application.GUI;
//import carregamento_de_horário.SaveFiles;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
///**
// * Esta classe contém testes JUnit para a classe {@link GUI}.
// * Os testes visam garantir o correto funcionamento dos métodos relacionados à interface gráfica.
// */
//public class GUITest {
//
//    /**
//     * Testa o método {@link GUI#createAndShowGUI()}.
//     * Verifica se a execução do método não lança exceções inesperadas.
//     */
//    @Test
//    public void testCreateAndShowGUI() {
//        // Verifica se a função createAndShowGUI() não lança exceções
//        GUI gui = new GUI();
//        try {
//            gui.createAndShowGUI();
//        } catch (Exception e) {
//            fail("Exceção inesperada: " + e.getMessage());
//        }
//    }
//
//}
