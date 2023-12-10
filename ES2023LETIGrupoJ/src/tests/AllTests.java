package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suíte de teste que agrupa e executa todos os testes relacionados ao sistema.
 * Esta suíte inclui testes para as seguintes classes:
 * - {@link Horario_ISCTETest}
 * - {@link HorarioLoaderTest}
 * - {@link MapeamentoTest}
 * - {@link ListaSalas_ISCTETest}
 * - {@link ListaSalasLoaderTest}
 * - {@link SaveFilesTest}
 */
@Suite
@SelectClasses({ 
    Horario_ISCTETest.class, 
    HorarioLoaderTest.class, 
    MapeamentoTest.class, 
    ListaSalas_ISCTETest.class,
    ListaSalasLoaderTest.class, 
    SaveFilesTest.class
})
public class AllTests {

}
