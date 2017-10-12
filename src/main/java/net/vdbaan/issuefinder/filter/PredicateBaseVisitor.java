// Generated from C:/Users/Steven/Documents/4.Projects/IssueFinder/src/main/antlr\Predicate.g4 by ANTLR 4.7

package net.vdbaan.issuefinder.filter;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link PredicateVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public class PredicateBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements PredicateVisitor<T> {
    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitEnclosedExpr(PredicateParser.EnclosedExprContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitRange(PredicateParser.RangeContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitOrExpr(PredicateParser.OrExprContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitExploitableExpr(PredicateParser.ExploitableExprContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitAssign(PredicateParser.AssignContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitGroup(PredicateParser.GroupContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitAndExpr(PredicateParser.AndExprContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitColumn(PredicateParser.ColumnContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitGroupOperator(PredicateParser.GroupOperatorContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitRangeOperator(PredicateParser.RangeOperatorContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public T visitOperator(PredicateParser.OperatorContext ctx) {
        return visitChildren(ctx);
    }
}