//package tests;
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import javax.swing.JFrame;
//import javax.swing.SwingUtilities;
//
//import carregamento_de_horário.Horario_ISCTE;
//import mapeamento.ColumnOrderDialog;
//
///**
// * Esta classe contém testes JUnit para a classe {@link ColumnOrderDialog}.
// * Os testes visam garantir a inicialização correta da GUI juntamente com a ordem das colunas
// * e o correto funcionamento dos métodos relacionados à ordem das colunas, garantindo que a 
// * ordem observada não é null
// */
//public class ColumnOrderDialogTest {
//
//    private Horario_ISCTE horarioISCTE;
//    private ColumnOrderDialog dialog;
//
//    /**
//     * Configuração inicial para os testes.
//     * Inicializa uma instância de {@link Horario_ISCTE} e cria a janela de diálogo.
//     * Aguarda a inicialização da GUI para evitar problemas de concorrência.
//     */
//    @Before
//    public void setUp() {
//        horarioISCTE = new Horario_ISCTE();
//
//        // Cria a janela de diálogo em uma nova thread para evitar bloquear a GUI
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame();
//            dialog = new ColumnOrderDialog(frame, horarioISCTE);
//            dialog.setVisible(false);
//        });
//
//        // Aguarda a inicialização da GUI
//        try {
//            Thread.sleep(1400);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Testa o método {@link ColumnOrderDialog#getCustomOrder()}.
//     * Verifica se a ordem personalizada não é nula.
//     */
//    @Test
//    public void testGetCustomOrder() {
//        // Obtém a ordem personalizada
//        List<String> customOrder = dialog.getCustomOrder();
//
//        // Verifica se não é nulo
//        assertNotNull(customOrder);
//    }
//
//    /**
//     * Testa o método {@link ColumnOrderDialog#getOriginalOrder()}.
//     * Verifica se a ordem original não é nula.
//     */
//    @Test
//    public void testGetOriginalOrder() {
//        // Obtém a ordem original
//        List<String> originalOrder = dialog.getOriginalOrder();
//
//        // Verifica se não é nulo
//        assertNotNull(originalOrder);
//    }
//}
