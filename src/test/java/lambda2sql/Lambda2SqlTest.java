package lambda2sql;

import static org.junit.Assert.assertEquals;

import java.util.function.Predicate;

import org.junit.BeforeClass;
import org.junit.Test;


public class Lambda2SqlTest {

	@BeforeClass
	public static void init() throws Exception {
		Lambda2Sql.init();
	}

	@Test
	public void testComparisons() throws Exception {
		assertEqual("age = 1", e -> e.getAge() == 1);
		assertEqual("age > 1", e -> e.getAge() > 1);
		assertEqual("age < 1", e -> e.getAge() < 1);
		assertEqual("age >= 1", e -> e.getAge() >= 1);
		assertEqual("age <= 1", e -> e.getAge() <= 1);
		assertEqual("age != 1", e -> e.getAge() != 1);
	}

	@Test
	public void testLogicalOps() throws Exception {
		assertEqual("!active", e -> ! e.isActive() );
		assertEqual("age < 100 AND height > 200", e -> e.getAge() < 100 && e.getHeight() > 200 );
		assertEqual("age < 100 OR height > 200", e -> e.getAge() < 100 || e.getHeight() > 200 );
	}

	@Test
	public void testMultiLogicalOps() throws Exception {
		assertEqual("active AND (age < 100 OR height > 200)", e -> e.isActive() && (e.getAge() < 100 || e.getHeight() > 200) );
	}

	private void assertEqual(String expectedSql, Predicate<Person> p) {
		String sql = Lambda2Sql.toSql(p);
		assertEquals(expectedSql, sql);
	}
}
