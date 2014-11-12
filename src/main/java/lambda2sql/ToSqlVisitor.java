package lambda2sql;

import static com.trigersoft.jaque.expression.ExpressionType.Equal;
import static com.trigersoft.jaque.expression.ExpressionType.LogicalAnd;
import static com.trigersoft.jaque.expression.ExpressionType.LogicalOr;

import com.trigersoft.jaque.expression.*;

public class ToSqlVisitor implements ExpressionVisitor<String> {

	boolean top = true;


	@Override
	public String visit(BinaryExpression e) {
		boolean quote = !top && e.getExpressionType() == LogicalOr;
		top = false;

		StringBuilder sb = new StringBuilder();
		if( quote ) sb.append('(');

		String first = e.getFirst().accept(this);
		String second = e.getSecond().accept(this);

		sb.append(first).append(' ').append(toSqlOp(e.getExpressionType())).append(' ').append(second);

		if( quote ) sb.append(')');

		return sb.toString();
	}

	public static String toSqlOp(int expressionType) {
		switch(expressionType) {
			case Equal: return "=";
			case LogicalAnd: return "AND";
			case LogicalOr: return "OR";
		}
		return ExpressionType.toString(expressionType);
	}

	@Override
	public String visit(ConstantExpression e) {
		return e.getValue().toString();
	}

	@Override
	public String visit(InvocationExpression e) {
		return e.getTarget().accept(this);
	}

	@Override
	public String visit(LambdaExpression<?> e) {
		return e.getBody().accept(this);
	}

	@Override
	public String visit(MemberExpression e) {
		String name = e.getMember().getName();
		return name.replaceAll("^(get|is)", "").toLowerCase();
	}

	@Override
	public String visit(ParameterExpression e) {
		return "";
	}

	@Override
	public String visit(UnaryExpression e) {
		String first = e.getFirst().accept(this);
		return ExpressionType.toString(e.getExpressionType()) + first;
	}

}