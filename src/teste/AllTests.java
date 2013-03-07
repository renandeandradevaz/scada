package teste;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GrupoOperadorControllerTest.class, HibernateUtilTest.class })
public class AllTests {

}
