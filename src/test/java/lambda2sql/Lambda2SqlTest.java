package lambda2sql;

import org.junit.Assert;
import org.junit.Test;

public class Lambda2SqlTest {

	@Test
	public void testComparisons() {
		assertEqual("age = 1", e -> e.getAge() == 1);
		assertEqual("age > 1", e -> e.getAge() > 1);
		assertEqual("age < 1", e -> e.getAge() < 1);
		assertEqual("age >= 1", e -> e.getAge() >= 1);
		assertEqual("age <= 1", e -> e.getAge() <= 1);
		assertEqual("age != 1", e -> e.getAge() != 1);
	}

	@Test
	public void testLogicalOps() {
		assertEqual("!isActive", e -> !e.isActive());
		assertEqual("age < 100 AND height > 200", e -> e.getAge() < 100 && e.getHeight() > 200);
		assertEqual("age < 100 OR height > 200", e -> e.getAge() < 100 || e.getHeight() > 200);
	}

	@Test
	public void testMultipleLogicalOps() {
		assertEqual("isActive AND (age < 100 OR height > 200)", e -> e.isActive() && (e.getAge() < 100 || e.getHeight() > 200));
		assertEqual("(age < 100 OR height > 200) AND isActive", e -> (e.getAge() < 100 || e.getHeight() > 200) && e.isActive());
	}

	@Test
	public void testWithVariables() {
		String name = "Donald";
		assertEqual("name = 'Donald'", person -> person.getName() == name);
	}

	private void assertEqual(String expectedSql, SqlPredicate<Person> p) {
		String sql = Lambda2Sql.toSql(p);
		Assert.assertEquals(expectedSql, sql);
	}
}
