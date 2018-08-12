package lambda2sql;

import com.trigersoft.jaque.expression.LambdaExpression;

import java.util.function.Predicate;

/**
 * A utility class for converting java lambdas to SQL.
 */
public class Lambda2Sql {

	/**
	 * Converts a predicate lambda to SQL.
	 * <pre>{@code person -> person.getAge() > 50 && person.isActive() }</pre>
	 * Becomes a string:
	 * <pre>{@code "age > 50 AND active" }</pre>
	 * Supported operators: >,>=,<,<=,=,!=,&&,||,!
	 */
	public static <T> String toSql(SqlPredicate<T> predicate) {
		LambdaExpression<Predicate<T>> lambdaExpression = LambdaExpression.parse(predicate);
		return lambdaExpression.accept(new ToSqlVisitor()).toString();
	}
}
