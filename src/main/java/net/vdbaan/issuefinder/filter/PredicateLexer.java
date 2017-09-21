// Generated from C:/Users/Steven/Documents/4.Projects/IssueFinder/src/main/antlr\Predicate.g4 by ANTLR 4.7

package net.vdbaan.issuefinder.filter;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PredicateLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, LPAREN=8, RPAREN=9, 
		AND=10, OR=11, NOT=12, IP=13, SCANNER=14, PORT=15, STATUS=16, PROTOCOL=17, 
		SERVICE=18, RISK=19, EXPLOITABLE=20, DESCRIPTION=21, PLUGIN=22, HOSTNAME=23, 
		IN=24, LIKE=25, BETWEEN=26, GROUP=27, RANGE=28, STRING=29, SQUOTE=30, 
		DQUOTE=31, CHAR=32, WS=33;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "LPAREN", "RPAREN", 
		"AND", "OR", "NOT", "IP", "SCANNER", "PORT", "STATUS", "PROTOCOL", "SERVICE", 
		"RISK", "EXPLOITABLE", "DESCRIPTION", "PLUGIN", "HOSTNAME", "IN", "LIKE", 
		"BETWEEN", "GROUP", "RANGE", "STRING", "A", "B", "C", "D", "E", "F", "G", 
		"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", 
		"V", "W", "X", "Y", "Z", "SQUOTE", "DQUOTE", "CHAR", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'=='", "'!='", "'<'", "'<='", "'>'", "'=>'", "'~='", "'('", "')'", 
		null, null, "'!'", null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, "'''", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "LPAREN", "RPAREN", "AND", 
		"OR", "NOT", "IP", "SCANNER", "PORT", "STATUS", "PROTOCOL", "SERVICE", 
		"RISK", "EXPLOITABLE", "DESCRIPTION", "PLUGIN", "HOSTNAME", "IN", "LIKE", 
		"BETWEEN", "GROUP", "RANGE", "STRING", "SQUOTE", "DQUOTE", "CHAR", "WS"
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


	public PredicateLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Predicate.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2#\u016f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b"+
		"\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\5\13\u0096\n\13\3\f\3\f\3\f"+
		"\3\f\5\f\u009c\n\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\6\34\u0109"+
		"\n\34\r\34\16\34\u010a\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3"+
		"\36\6\36\u0117\n\36\r\36\16\36\u0118\3\36\3\36\3\36\3\36\6\36\u011f\n"+
		"\36\r\36\16\36\u0120\3\36\3\36\3\36\5\36\u0126\n\36\3\37\3\37\3 \3 \3"+
		"!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3"+
		",\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3"+
		"\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\39\39\3:\3:\3:\3:\3;\6;"+
		"\u0165\n;\r;\16;\u0166\3<\6<\u016a\n<\r<\16<\u016b\3<\3<\4\u0118\u0120"+
		"\2=\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37=\2?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2"+
		"i\2k\2m\2o\2q s!u\"w#\3\2 \3\2$$\3\2))\4\2CCcc\4\2DDdd\4\2EEee\4\2FFf"+
		"f\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2"+
		"OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4"+
		"\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\6\2\60=C\\^^c|\5\2\13\f\17\17"+
		"\"\"\2\u015d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\3y\3\2\2"+
		"\2\5|\3\2\2\2\7\177\3\2\2\2\t\u0081\3\2\2\2\13\u0084\3\2\2\2\r\u0086\3"+
		"\2\2\2\17\u0089\3\2\2\2\21\u008c\3\2\2\2\23\u008e\3\2\2\2\25\u0095\3\2"+
		"\2\2\27\u009b\3\2\2\2\31\u009d\3\2\2\2\33\u009f\3\2\2\2\35\u00a2\3\2\2"+
		"\2\37\u00aa\3\2\2\2!\u00af\3\2\2\2#\u00b6\3\2\2\2%\u00bf\3\2\2\2\'\u00c7"+
		"\3\2\2\2)\u00cc\3\2\2\2+\u00d8\3\2\2\2-\u00e4\3\2\2\2/\u00eb\3\2\2\2\61"+
		"\u00f4\3\2\2\2\63\u00f7\3\2\2\2\65\u00fc\3\2\2\2\67\u0104\3\2\2\29\u010e"+
		"\3\2\2\2;\u0125\3\2\2\2=\u0127\3\2\2\2?\u0129\3\2\2\2A\u012b\3\2\2\2C"+
		"\u012d\3\2\2\2E\u012f\3\2\2\2G\u0131\3\2\2\2I\u0133\3\2\2\2K\u0135\3\2"+
		"\2\2M\u0137\3\2\2\2O\u0139\3\2\2\2Q\u013b\3\2\2\2S\u013d\3\2\2\2U\u013f"+
		"\3\2\2\2W\u0141\3\2\2\2Y\u0143\3\2\2\2[\u0145\3\2\2\2]\u0147\3\2\2\2_"+
		"\u0149\3\2\2\2a\u014b\3\2\2\2c\u014d\3\2\2\2e\u014f\3\2\2\2g\u0151\3\2"+
		"\2\2i\u0153\3\2\2\2k\u0155\3\2\2\2m\u0157\3\2\2\2o\u0159\3\2\2\2q\u015b"+
		"\3\2\2\2s\u015f\3\2\2\2u\u0164\3\2\2\2w\u0169\3\2\2\2yz\7?\2\2z{\7?\2"+
		"\2{\4\3\2\2\2|}\7#\2\2}~\7?\2\2~\6\3\2\2\2\177\u0080\7>\2\2\u0080\b\3"+
		"\2\2\2\u0081\u0082\7>\2\2\u0082\u0083\7?\2\2\u0083\n\3\2\2\2\u0084\u0085"+
		"\7@\2\2\u0085\f\3\2\2\2\u0086\u0087\7?\2\2\u0087\u0088\7@\2\2\u0088\16"+
		"\3\2\2\2\u0089\u008a\7\u0080\2\2\u008a\u008b\7?\2\2\u008b\20\3\2\2\2\u008c"+
		"\u008d\7*\2\2\u008d\22\3\2\2\2\u008e\u008f\7+\2\2\u008f\24\3\2\2\2\u0090"+
		"\u0091\7(\2\2\u0091\u0096\7(\2\2\u0092\u0093\7C\2\2\u0093\u0094\7P\2\2"+
		"\u0094\u0096\7F\2\2\u0095\u0090\3\2\2\2\u0095\u0092\3\2\2\2\u0096\26\3"+
		"\2\2\2\u0097\u0098\7~\2\2\u0098\u009c\7~\2\2\u0099\u009a\7Q\2\2\u009a"+
		"\u009c\7T\2\2\u009b\u0097\3\2\2\2\u009b\u0099\3\2\2\2\u009c\30\3\2\2\2"+
		"\u009d\u009e\7#\2\2\u009e\32\3\2\2\2\u009f\u00a0\5M\'\2\u00a0\u00a1\5"+
		"[.\2\u00a1\34\3\2\2\2\u00a2\u00a3\5a\61\2\u00a3\u00a4\5A!\2\u00a4\u00a5"+
		"\5=\37\2\u00a5\u00a6\5W,\2\u00a6\u00a7\5W,\2\u00a7\u00a8\5E#\2\u00a8\u00a9"+
		"\5_\60\2\u00a9\36\3\2\2\2\u00aa\u00ab\5[.\2\u00ab\u00ac\5Y-\2\u00ac\u00ad"+
		"\5_\60\2\u00ad\u00ae\5c\62\2\u00ae \3\2\2\2\u00af\u00b0\5a\61\2\u00b0"+
		"\u00b1\5c\62\2\u00b1\u00b2\5=\37\2\u00b2\u00b3\5c\62\2\u00b3\u00b4\5e"+
		"\63\2\u00b4\u00b5\5a\61\2\u00b5\"\3\2\2\2\u00b6\u00b7\5[.\2\u00b7\u00b8"+
		"\5_\60\2\u00b8\u00b9\5Y-\2\u00b9\u00ba\5c\62\2\u00ba\u00bb\5Y-\2\u00bb"+
		"\u00bc\5A!\2\u00bc\u00bd\5Y-\2\u00bd\u00be\5S*\2\u00be$\3\2\2\2\u00bf"+
		"\u00c0\5a\61\2\u00c0\u00c1\5E#\2\u00c1\u00c2\5_\60\2\u00c2\u00c3\5g\64"+
		"\2\u00c3\u00c4\5M\'\2\u00c4\u00c5\5A!\2\u00c5\u00c6\5E#\2\u00c6&\3\2\2"+
		"\2\u00c7\u00c8\5_\60\2\u00c8\u00c9\5M\'\2\u00c9\u00ca\5a\61\2\u00ca\u00cb"+
		"\5Q)\2\u00cb(\3\2\2\2\u00cc\u00cd\5E#\2\u00cd\u00ce\5k\66\2\u00ce\u00cf"+
		"\5[.\2\u00cf\u00d0\5S*\2\u00d0\u00d1\5Y-\2\u00d1\u00d2\5M\'\2\u00d2\u00d3"+
		"\5c\62\2\u00d3\u00d4\5=\37\2\u00d4\u00d5\5? \2\u00d5\u00d6\5S*\2\u00d6"+
		"\u00d7\5E#\2\u00d7*\3\2\2\2\u00d8\u00d9\5C\"\2\u00d9\u00da\5E#\2\u00da"+
		"\u00db\5a\61\2\u00db\u00dc\5A!\2\u00dc\u00dd\5_\60\2\u00dd\u00de\5M\'"+
		"\2\u00de\u00df\5[.\2\u00df\u00e0\5c\62\2\u00e0\u00e1\5M\'\2\u00e1\u00e2"+
		"\5Y-\2\u00e2\u00e3\5W,\2\u00e3,\3\2\2\2\u00e4\u00e5\5[.\2\u00e5\u00e6"+
		"\5S*\2\u00e6\u00e7\5e\63\2\u00e7\u00e8\5I%\2\u00e8\u00e9\5M\'\2\u00e9"+
		"\u00ea\5W,\2\u00ea.\3\2\2\2\u00eb\u00ec\5K&\2\u00ec\u00ed\5Y-\2\u00ed"+
		"\u00ee\5a\61\2\u00ee\u00ef\5c\62\2\u00ef\u00f0\5W,\2\u00f0\u00f1\5=\37"+
		"\2\u00f1\u00f2\5U+\2\u00f2\u00f3\5E#\2\u00f3\60\3\2\2\2\u00f4\u00f5\5"+
		"M\'\2\u00f5\u00f6\5W,\2\u00f6\62\3\2\2\2\u00f7\u00f8\5S*\2\u00f8\u00f9"+
		"\5M\'\2\u00f9\u00fa\5Q)\2\u00fa\u00fb\5E#\2\u00fb\64\3\2\2\2\u00fc\u00fd"+
		"\5? \2\u00fd\u00fe\5E#\2\u00fe\u00ff\5c\62\2\u00ff\u0100\5i\65\2\u0100"+
		"\u0101\5E#\2\u0101\u0102\5E#\2\u0102\u0103\5W,\2\u0103\66\3\2\2\2\u0104"+
		"\u0105\7]\2\2\u0105\u0108\5;\36\2\u0106\u0107\7.\2\2\u0107\u0109\5;\36"+
		"\2\u0108\u0106\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u010b"+
		"\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\7_\2\2\u010d8\3\2\2\2\u010e\u010f"+
		"\5\21\t\2\u010f\u0110\5;\36\2\u0110\u0111\7.\2\2\u0111\u0112\5;\36\2\u0112"+
		"\u0113\5\23\n\2\u0113:\3\2\2\2\u0114\u0116\5s:\2\u0115\u0117\n\2\2\2\u0116"+
		"\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119\3\2\2\2\u0118\u0116\3\2"+
		"\2\2\u0119\u011a\3\2\2\2\u011a\u011b\5s:\2\u011b\u0126\3\2\2\2\u011c\u011e"+
		"\5q9\2\u011d\u011f\n\3\2\2\u011e\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0123\5q"+
		"9\2\u0123\u0126\3\2\2\2\u0124\u0126\5u;\2\u0125\u0114\3\2\2\2\u0125\u011c"+
		"\3\2\2\2\u0125\u0124\3\2\2\2\u0126<\3\2\2\2\u0127\u0128\t\4\2\2\u0128"+
		">\3\2\2\2\u0129\u012a\t\5\2\2\u012a@\3\2\2\2\u012b\u012c\t\6\2\2\u012c"+
		"B\3\2\2\2\u012d\u012e\t\7\2\2\u012eD\3\2\2\2\u012f\u0130\t\b\2\2\u0130"+
		"F\3\2\2\2\u0131\u0132\t\t\2\2\u0132H\3\2\2\2\u0133\u0134\t\n\2\2\u0134"+
		"J\3\2\2\2\u0135\u0136\t\13\2\2\u0136L\3\2\2\2\u0137\u0138\t\f\2\2\u0138"+
		"N\3\2\2\2\u0139\u013a\t\r\2\2\u013aP\3\2\2\2\u013b\u013c\t\16\2\2\u013c"+
		"R\3\2\2\2\u013d\u013e\t\17\2\2\u013eT\3\2\2\2\u013f\u0140\t\20\2\2\u0140"+
		"V\3\2\2\2\u0141\u0142\t\21\2\2\u0142X\3\2\2\2\u0143\u0144\t\22\2\2\u0144"+
		"Z\3\2\2\2\u0145\u0146\t\23\2\2\u0146\\\3\2\2\2\u0147\u0148\t\24\2\2\u0148"+
		"^\3\2\2\2\u0149\u014a\t\25\2\2\u014a`\3\2\2\2\u014b\u014c\t\26\2\2\u014c"+
		"b\3\2\2\2\u014d\u014e\t\27\2\2\u014ed\3\2\2\2\u014f\u0150\t\30\2\2\u0150"+
		"f\3\2\2\2\u0151\u0152\t\31\2\2\u0152h\3\2\2\2\u0153\u0154\t\32\2\2\u0154"+
		"j\3\2\2\2\u0155\u0156\t\33\2\2\u0156l\3\2\2\2\u0157\u0158\t\34\2\2\u0158"+
		"n\3\2\2\2\u0159\u015a\t\35\2\2\u015ap\3\2\2\2\u015b\u015c\7)\2\2\u015c"+
		"\u015d\3\2\2\2\u015d\u015e\b9\2\2\u015er\3\2\2\2\u015f\u0160\7$\2\2\u0160"+
		"\u0161\3\2\2\2\u0161\u0162\b:\2\2\u0162t\3\2\2\2\u0163\u0165\t\36\2\2"+
		"\u0164\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0164\3\2\2\2\u0166\u0167"+
		"\3\2\2\2\u0167v\3\2\2\2\u0168\u016a\t\37\2\2\u0169\u0168\3\2\2\2\u016a"+
		"\u016b\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016d\3\2"+
		"\2\2\u016d\u016e\b<\2\2\u016ex\3\2\2\2\13\2\u0095\u009b\u010a\u0118\u0120"+
		"\u0125\u0166\u016b\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}