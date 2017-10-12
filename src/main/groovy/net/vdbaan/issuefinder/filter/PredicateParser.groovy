/*
 *  Copyright (C) 2017  S. van der Baan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.vdbaan.issuefinder.filter

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNSimulator
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTree

import java.util.logging.Level

@CompileStatic
class FindingPredicateParserRuntimeException extends RuntimeException {
    FindingPredicateParserRuntimeException(String message) {
        super(message)
    }
}

@Log
@CompileStatic
class FindingPredicateParser {
    FindingPredicate parse(String text) {
        PredicateLexer lexer = new PredicateLexer(CharStreams.fromString(text))
        CommonTokenStream tokens = new CommonTokenStream(lexer)
        PredicateParser parser = new PredicateParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            void syntaxError(Recognizer<?, ? extends ATNSimulator> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
                throw new FindingPredicateParserRuntimeException("error")
            }
        })
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION)
        ParseTree tree = parser.expr()
        FindingPredicateVisitor v = new FindingPredicateVisitor()
        try {
            FindingPredicate fp = (FindingPredicate) v.visit(tree)
            return fp
        } catch (FindingPredicateParserRuntimeException r) {
            log.log(Level.FINE,'Got an exception',r)
            return null
        }
    }
}

@CompileStatic
class FindingPredicateVisitor extends PredicateBaseVisitor {

    @Override
    Object visitAndExpr(PredicateParser.AndExprContext ctx) { // expr AND expr
        return new FindingPredicate(visit(ctx.expr(0)), FindingPredicate.LogicalOperation.AND, visit(ctx.expr(1)))
    }

    @Override
    Object visitOrExpr(PredicateParser.OrExprContext ctx) { // expr OR expr
        return new FindingPredicate(visit(ctx.expr(0)), FindingPredicate.LogicalOperation.OR, visit(ctx.expr(1)))
    }

    @Override
    Object visitEnclosedExpr(PredicateParser.EnclosedExprContext ctx) { // LPAREN expr RPAREN
        return visit(ctx.expr())
    }


    @Override
    Object visitAssign(PredicateParser.AssignContext ctx) { // column operator STRING
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.operator()), stripQuotes(ctx.STRING().text))
    }

    @Override
    Object visitRange(PredicateParser.RangeContext ctx) { // column rangeOperator RANGE
        List list = buildList(ctx.GROUP().text)
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.rangeOperator()), list)
    }

    @Override
    Object visitGroup(PredicateParser.GroupContext ctx) { // column groupOperator GROUP
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.groupOperator()), buildList(ctx.GROUP().text))
    }

    @Override
    Object visitExploitableExpr(PredicateParser.ExploitableExprContext ctx) { //EXPLOITABLE
        FindingPredicate result = new FindingPredicate(ColumnName.EXPLOITABLE,null,null)
        if(ctx.childCount == 2) {
            result.operation = FindingPredicate.LogicalOperation.NOT
        }
        return result
    }

    @Override
    Object visitColumn(PredicateParser.ColumnContext ctx) {
        return ColumnName.get(ctx.text.toUpperCase())
    }

    @Override
    Object visitOperator(PredicateParser.OperatorContext ctx) {
        if(ctx.childCount == 2) {
            return FindingPredicate.LogicalOperation.NLIKE
        } else
        return FindingPredicate.LogicalOperation.get(ctx.text.toUpperCase())
    }

    @Override
    Object visitGroupOperator(PredicateParser.GroupOperatorContext ctx) {
        return FindingPredicate.LogicalOperation.get(ctx.text.toUpperCase())
    }

    @Override
    Object visitRangeOperator(PredicateParser.RangeOperatorContext ctx) {
        return FindingPredicate.LogicalOperation.get(ctx.text.toUpperCase())
    }

    protected Object aggregateResult(Object aggregate, Object nextResult) {
        if (nextResult == null) return aggregate
        return super.aggregateResult(aggregate, nextResult)
    }

    private String stripQuotes(String text) {
        if (text.startsWith('"') || text.startsWith("'")) {
            def len = text.length()
            return text.substring(1, len - 1)
        } else return text
    }

    private List buildList(String list) {
        ArrayList result = new ArrayList()
        def len = list.length()
        String workable = list.substring(1, len - 1)
        workable.tokenize(",").each { token ->
            result << stripQuotes(token)
        }
        return result
    }
}