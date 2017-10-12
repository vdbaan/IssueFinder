// Generated from C:/Users/Steven/Documents/4.Projects/IssueFinder/src/main/antlr\Predicate.g4 by ANTLR 4.7

package net.vdbaan.issuefinder.filter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PredicateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, LPAREN=8, RPAREN=9, 
		AND=10, OR=11, NOT=12, IP=13, SCANNER=14, PORT=15, STATUS=16, PROTOCOL=17, 
		SERVICE=18, RISK=19, EXPLOITABLE=20, DESCRIPTION=21, PLUGIN=22, HOSTNAME=23, 
		CVSS=24, IN=25, LIKE=26, BETWEEN=27, GROUP=28, STRING=29, SQUOTE=30, DQUOTE=31, 
		CHAR=32, WS=33;
	public static final int
		RULE_expr = 0, RULE_column = 1, RULE_groupOperator = 2, RULE_rangeOperator = 3, 
		RULE_operator = 4;
	public static final String[] ruleNames = {
		"expr", "column", "groupOperator", "rangeOperator", "operator"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'~='", "'('", "')'", 
		null, null, "'!'", null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, "'''", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "LPAREN", "RPAREN", "AND", 
		"OR", "NOT", "IP", "SCANNER", "PORT", "STATUS", "PROTOCOL", "SERVICE", 
		"RISK", "EXPLOITABLE", "DESCRIPTION", "PLUGIN", "HOSTNAME", "CVSS", "IN", 
		"LIKE", "BETWEEN", "GROUP", "STRING", "SQUOTE", "DQUOTE", "CHAR", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Predicate.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PredicateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EnclosedExprContext extends ExprContext {
		public TerminalNode LPAREN() { return getToken(PredicateParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(PredicateParser.RPAREN, 0); }
		public EnclosedExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterEnclosedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitEnclosedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitEnclosedExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RangeContext extends ExprContext {
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public RangeOperatorContext rangeOperator() {
			return getRuleContext(RangeOperatorContext.class,0);
		}
		public TerminalNode GROUP() { return getToken(PredicateParser.GROUP, 0); }
		public RangeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OR() { return getToken(PredicateParser.OR, 0); }
		public OrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExploitableExprContext extends ExprContext {
		public TerminalNode EXPLOITABLE() { return getToken(PredicateParser.EXPLOITABLE, 0); }
		public TerminalNode NOT() { return getToken(PredicateParser.NOT, 0); }
		public ExploitableExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterExploitableExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitExploitableExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitExploitableExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignContext extends ExprContext {
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public TerminalNode STRING() { return getToken(PredicateParser.STRING, 0); }
		public AssignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitAssign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupContext extends ExprContext {
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public GroupOperatorContext groupOperator() {
			return getRuleContext(GroupOperatorContext.class,0);
		}
		public TerminalNode GROUP() { return getToken(PredicateParser.GROUP, 0); }
		public GroupContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitGroup(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(PredicateParser.AND, 0); }
		public AndExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				_localctx = new EnclosedExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(11);
				match(LPAREN);
				setState(12);
				expr(0);
				setState(13);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new AssignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(15);
				column();
				setState(16);
				operator();
				setState(17);
				match(STRING);
				}
				break;
			case 3:
				{
				_localctx = new RangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(19);
				column();
				setState(20);
				rangeOperator();
				setState(21);
				match(GROUP);
				}
				break;
			case 4:
				{
				_localctx = new GroupContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(23);
				column();
				setState(24);
				groupOperator();
				setState(25);
				match(GROUP);
				}
				break;
			case 5:
				{
				_localctx = new ExploitableExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(27);
					match(NOT);
					}
				}

				setState(30);
				match(EXPLOITABLE);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(41);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(39);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new AndExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(33);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(34);
						match(AND);
						setState(35);
						expr(8);
						}
						break;
					case 2:
						{
						_localctx = new OrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(36);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(37);
						match(OR);
						setState(38);
						expr(7);
						}
						break;
					}
					} 
				}
				setState(43);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ColumnContext extends ParserRuleContext {
		public TerminalNode SCANNER() { return getToken(PredicateParser.SCANNER, 0); }
		public TerminalNode IP() { return getToken(PredicateParser.IP, 0); }
		public TerminalNode PORT() { return getToken(PredicateParser.PORT, 0); }
		public TerminalNode STATUS() { return getToken(PredicateParser.STATUS, 0); }
		public TerminalNode PROTOCOL() { return getToken(PredicateParser.PROTOCOL, 0); }
		public TerminalNode SERVICE() { return getToken(PredicateParser.SERVICE, 0); }
		public TerminalNode RISK() { return getToken(PredicateParser.RISK, 0); }
		public TerminalNode EXPLOITABLE() { return getToken(PredicateParser.EXPLOITABLE, 0); }
		public TerminalNode DESCRIPTION() { return getToken(PredicateParser.DESCRIPTION, 0); }
		public TerminalNode PLUGIN() { return getToken(PredicateParser.PLUGIN, 0); }
		public TerminalNode CVSS() { return getToken(PredicateParser.CVSS, 0); }
		public TerminalNode HOSTNAME() { return getToken(PredicateParser.HOSTNAME, 0); }
		public ColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterColumn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitColumn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitColumn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnContext column() throws RecognitionException {
		ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IP) | (1L << SCANNER) | (1L << PORT) | (1L << STATUS) | (1L << PROTOCOL) | (1L << SERVICE) | (1L << RISK) | (1L << EXPLOITABLE) | (1L << DESCRIPTION) | (1L << PLUGIN) | (1L << HOSTNAME) | (1L << CVSS))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupOperatorContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(PredicateParser.IN, 0); }
		public GroupOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterGroupOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitGroupOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitGroupOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupOperatorContext groupOperator() throws RecognitionException {
		GroupOperatorContext _localctx = new GroupOperatorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_groupOperator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(IN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeOperatorContext extends ParserRuleContext {
		public TerminalNode BETWEEN() { return getToken(PredicateParser.BETWEEN, 0); }
		public RangeOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterRangeOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitRangeOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitRangeOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeOperatorContext rangeOperator() throws RecognitionException {
		RangeOperatorContext _localctx = new RangeOperatorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rangeOperator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(BETWEEN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode LIKE() { return getToken(PredicateParser.LIKE, 0); }
		public TerminalNode NOT() { return getToken(PredicateParser.NOT, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PredicateListener ) ((PredicateListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PredicateVisitor ) return ((PredicateVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_operator);
		int _la;
		try {
			setState(61);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				match(T__0);
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				match(T__1);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				match(T__2);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				match(T__3);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(54);
				match(T__4);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 6);
				{
				setState(55);
				match(T__5);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 7);
				{
				setState(56);
				match(T__6);
				}
				break;
			case NOT:
			case LIKE:
				enterOuterAlt(_localctx, 8);
				{
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(57);
					match(NOT);
					}
				}

				setState(60);
				match(LIKE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3#B\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\37\n\2\3\2\5\2\"\n\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\7\2*\n\2\f\2\16\2-\13\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\5\6=\n\6\3\6\5\6@\n\6\3\6\2\3\2\7\2\4\6\b\n\2\3\3\2\17"+
		"\32\2K\2!\3\2\2\2\4.\3\2\2\2\6\60\3\2\2\2\b\62\3\2\2\2\n?\3\2\2\2\f\r"+
		"\b\2\1\2\r\16\7\n\2\2\16\17\5\2\2\2\17\20\7\13\2\2\20\"\3\2\2\2\21\22"+
		"\5\4\3\2\22\23\5\n\6\2\23\24\7\37\2\2\24\"\3\2\2\2\25\26\5\4\3\2\26\27"+
		"\5\b\5\2\27\30\7\36\2\2\30\"\3\2\2\2\31\32\5\4\3\2\32\33\5\6\4\2\33\34"+
		"\7\36\2\2\34\"\3\2\2\2\35\37\7\16\2\2\36\35\3\2\2\2\36\37\3\2\2\2\37 "+
		"\3\2\2\2 \"\7\26\2\2!\f\3\2\2\2!\21\3\2\2\2!\25\3\2\2\2!\31\3\2\2\2!\36"+
		"\3\2\2\2\"+\3\2\2\2#$\f\t\2\2$%\7\f\2\2%*\5\2\2\n&\'\f\b\2\2\'(\7\r\2"+
		"\2(*\5\2\2\t)#\3\2\2\2)&\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\3\3\2"+
		"\2\2-+\3\2\2\2./\t\2\2\2/\5\3\2\2\2\60\61\7\33\2\2\61\7\3\2\2\2\62\63"+
		"\7\35\2\2\63\t\3\2\2\2\64@\7\3\2\2\65@\7\4\2\2\66@\7\5\2\2\67@\7\6\2\2"+
		"8@\7\7\2\29@\7\b\2\2:@\7\t\2\2;=\7\16\2\2<;\3\2\2\2<=\3\2\2\2=>\3\2\2"+
		"\2>@\7\34\2\2?\64\3\2\2\2?\65\3\2\2\2?\66\3\2\2\2?\67\3\2\2\2?8\3\2\2"+
		"\2?9\3\2\2\2?:\3\2\2\2?<\3\2\2\2@\13\3\2\2\2\b\36!)+<?";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}