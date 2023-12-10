package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
//não tou a fazer do ColumnOrderDialog, GUI e Métricas pq envolve interações com Interface
@Suite
@SelectClasses({ Horario_ISCTETest.class, HorarioLoaderTest.class, MapeamentoTest.class, ListaSalas_ISCTETest.class,
		ListaSalasLoaderTest.class, SaveFilesTest.class})
public class AllTests {

}
