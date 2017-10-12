// Generated from C:/Users/Steven/Documents/4.Projects/IssueFinder/src/main/antlr\Predicate.g4 by ANTLR 4.7

package net.vdbaan.issuefinder.filter;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PredicateParser}.
 */
public interface PredicateListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code enclosedExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedExpr(PredicateParser.EnclosedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code enclosedExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedExpr(PredicateParser.EnclosedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code range}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRange(PredicateParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code range}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRange(PredicateParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(PredicateParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(PredicateParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exploitableExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExploitableExpr(PredicateParser.ExploitableExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exploitableExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExploitableExpr(PredicateParser.ExploitableExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(PredicateParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(PredicateParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code group}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGroup(PredicateParser.GroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code group}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGroup(PredicateParser.GroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(PredicateParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link PredicateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(PredicateParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PredicateParser#column}.
	 * @param ctx the parse tree
	 */
	void enterColumn(PredicateParser.ColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PredicateParser#column}.
	 * @param ctx the parse tree
	 */
	void exitColumn(PredicateParser.ColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PredicateParser#groupOperator}.
	 * @param ctx the parse tree
	 */
	void enterGroupOperator(PredicateParser.GroupOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PredicateParser#groupOperator}.
	 * @param ctx the parse tree
	 */
	void exitGroupOperator(PredicateParser.GroupOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PredicateParser#rangeOperator}.
	 * @param ctx the parse tree
	 */
	void enterRangeOperator(PredicateParser.RangeOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PredicateParser#rangeOperator}.
	 * @param ctx the parse tree
	 */
	void exitRangeOperator(PredicateParser.RangeOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PredicateParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(PredicateParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PredicateParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(PredicateParser.OperatorContext ctx);
}