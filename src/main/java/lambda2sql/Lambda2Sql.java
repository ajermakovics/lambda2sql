package lambda2sql;

import static java.nio.file.Files.createTempDirectory;

import java.io.IOException;
import java.util.function.Predicate;

import com.trigersoft.jaque.expression.LambdaExpression;

/**
 * A utility class for converting java lambdas to SQL.
 * It must be initializes with {@link #init()} before any lambdas are created.
 */
public class Lambda2Sql {

	private static final String DUMP_CLASSES_PROP = "jdk.internal.lambda.dumpProxyClasses";

	/**
	 * Initializes the jdk.internal.lambda.dumpProxyClasses system property with a temporary directory.
	 * See https://bugs.openjdk.java.net/browse/JDK-8023524
	 */
	public static void init() {
		try {
			if( System.getProperty(DUMP_CLASSES_PROP) == null )
				System.setProperty(DUMP_CLASSES_PROP, createTempDirectory("lambda").toString());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Converts a predicate lambda to SQL. Make sure to {@link Lamda2Sql#init()} before creating the predicate.<br/>
	 * <pre>{@code person -> person.getAge() > 50 && person.isActive() }</pre>
	 * Becomes a string:
	 * <pre>{@code "age > 50 AND active" }</pre>
	 * Supported operators: >,>=,<,<=,=,!=,&&,||,!
	 */
	public static <T> String toSql(Predicate<T> predicate) {
		LambdaExpression<Predicate<T>> lambdaExpression = LambdaExpression.parse(predicate);
		return lambdaExpression.accept(new ToSqlVisitor()).toString();
	}
}
