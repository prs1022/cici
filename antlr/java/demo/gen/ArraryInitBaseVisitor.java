// Generated from E:/work_space/LeetCode/cici/antlr/java/demo\ArraryInit.g4 by ANTLR 4.5.1
package demo.gen;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link ArraryInitVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class ArraryInitBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements ArraryInitVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInit(ArraryInitParser.InitContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitValue(ArraryInitParser.ValueContext ctx) { return visitChildren(ctx); }
}