package lambda2sql;

import static com.trigersoft.jaque.expression.ExpressionType.Equal;
import static com.trigersoft.jaque.expression.ExpressionType.LogicalAnd;
import static com.trigersoft.jaque.expression.ExpressionType.LogicalOr;

import com.trigersoft.jaque.expression.*;

public class ToSqlVisitor implements ExpressionVisitor<StringBuilder> {

	private StringBuilder sb = new StringBuilder();
	private Expression body;

	@Override
	public StringBuilder visit(BinaryExpression e) {
		boolean quote = e != body && e.getExpressionType() == LogicalOr;

		if( quote ) sb.append('(');

		e.getFirst().accept(this);
		sb.append(' ').append(toSqlOp(e.getExpressionType())).append(' ');
		e.getSecond().accept(this);

		if( quote ) sb.append(')');

		return sb;
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
	public StringBuilder visit(ConstantExpression e) {
		return sb.append(e.getValue().toString());
	}

	@Override
	public StringBuilder visit(InvocationExpression e) {
		return e.getTarget().accept(this);
	}

	@Override
	public StringBuilder visit(LambdaExpression<?> e) {
		this.body = e.getBody();
		return body.accept(this);
	}

	@Override
	public StringBuilder visit(MemberExpression e) {
		String name = e.getMember().getName();
		name = name.replaceAll("^(get|is)", "").toLowerCase();
		return sb.append(name);
	}

	@Override
	public StringBuilder visit(ParameterExpression e) {
		return sb;
	}

	@Override
	public StringBuilder visit(UnaryExpression e) {
		sb.append(ExpressionType.toString(e.getExpressionType()));
		return e.getFirst().accept(this);
	}

}