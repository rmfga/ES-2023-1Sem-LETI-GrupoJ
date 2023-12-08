package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ColumnOrderDialogTest.class, Horario_ISCTETest.class, HorarioLoaderTest.class, MapeamentoTest.class,
		MétricasTest.class, SalaTest.class })
public class AllTests {

}
