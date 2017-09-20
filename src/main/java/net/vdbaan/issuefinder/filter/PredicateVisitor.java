// Generated from C:/Users/Steven/Documents/4.Projects/IssueFinder/src/main/antlr\Predicate.g4 by ANTLR 4.7

package net.vdbaan.issuefinder.filter;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PredicateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PredicateVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code enclosedExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedExpr(PredicateParser.EnclosedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(PredicateParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code range}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(PredicateParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notColumn}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotColumn(PredicateParser.NotColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(PredicateParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(PredicateParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code group}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(PredicateParser.GroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(PredicateParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PredicateParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn(PredicateParser.ColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PredicateParser#groupOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupOperator(PredicateParser.GroupOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PredicateParser#rangeOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeOperator(PredicateParser.RangeOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PredicateParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(PredicateParser.OperatorContext ctx);
}