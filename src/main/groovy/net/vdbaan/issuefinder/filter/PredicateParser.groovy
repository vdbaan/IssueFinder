package net.vdbaan.issuefinder.filter


import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTree

class FindingPredicateParserRuntimeException extends RuntimeException {
    FindingPredicateParserRuntimeException(String message) {
        super(message)
    }
}

class FindingPredicateParser {
    static void main(String[] args) {
//        String txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && !(SERVICE LIKE 'http'))) || (RISK IN [HIGH,LOW])";
        String txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && !(SERVICE LIKE 'http')) || (RISK BETWEEN (LOW,CRITICAL)))"
        FindingPredicateParser fpp = new FindingPredicateParser()
        println fpp.parse(txt)
    }

    FindingPredicate parse(String text) {
        PredicateLexer lexer = new PredicateLexer(CharStreams.fromString(text))
        CommonTokenStream tokens = new CommonTokenStream(lexer)
        PredicateParser parser = new PredicateParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
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
            return null
        }
    }
}

class FindingPredicateVisitor extends PredicateBaseVisitor {
    /*
    expr AND expr              # andExpr
    | expr OR expr               # orExpr
    | LPAREN expr RPAREN         # enclosedExpr
    | NOT expr                   # notExpr
    | column operator STRING     # assign
    | column rangeOperator RANGE # range
    | column groupOperator GROUP # group
    | NOT column                 # notColumn
     */

    @Override
    Object visitAndExpr(PredicateParser.AndExprContext ctx) {
        return new FindingPredicate(visit(ctx.expr(0)), FindingPredicate.LogicalOperation.AND, visit(ctx.expr(1)))
    }

    @Override
    Object visitOrExpr(PredicateParser.OrExprContext ctx) {
        return new FindingPredicate(visit(ctx.expr(0)), FindingPredicate.LogicalOperation.OR, visit(ctx.expr(1)))
    }

    @Override
    Object visitEnclosedExpr(PredicateParser.EnclosedExprContext ctx) {
        return visit(ctx.expr())
    }

    @Override
    Object visitNotExpr(PredicateParser.NotExprContext ctx) {
        return new FindingPredicate(visit(ctx.expr()), FindingPredicate.LogicalOperation.NOT, null)
    }

    @Override
    Object visitAssign(PredicateParser.AssignContext ctx) {
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.operator()), stripQuotes(ctx.STRING().text))
    }

    @Override
    Object visitRange(PredicateParser.RangeContext ctx) {
        ArrayList list = buildList(ctx.RANGE().text)
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.rangeOperator()), list)
    }

    @Override
    Object visitGroup(PredicateParser.GroupContext ctx) {
        return new FindingPredicate(visit(ctx.column()), (FindingPredicate.LogicalOperation) visit(ctx.groupOperator()), buildList(ctx.GROUP().text))
    }

    @Override
    Object visitNotColumn(PredicateParser.NotColumnContext ctx) {
        return new FindingPredicate(visit(ctx.column()), FindingPredicate.LogicalOperation.NOT, null)
    }


    @Override
    Object visitColumn(PredicateParser.ColumnContext ctx) {
        return ColumnName.get(ctx.text.toUpperCase())
    }

    @Override
    Object visitOperator(PredicateParser.OperatorContext ctx) {
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