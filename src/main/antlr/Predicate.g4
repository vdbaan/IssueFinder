grammar Predicate;

//IP == "127.0.0.1"
//SCANNER ~= 'nmap'
//SERVICE LIKE 'http'
//PORT LIKE "443"
//EXPLOITABLE
//(SERVICE LIKE 'SMB') && !EXPLOITABLE
// (PORT LIKE "443") || (SERVICE LIKE "http")
// ((IP == "127.0.0.1") && (!EXPLOITABLE)) || ((PORT LIKE "443") && (SERVICE LIKE "http"))
// Predicate(Predicate(IP,==,"127.0.0.1).and(Predicate.not(EXPLOITABLE)).or(Predicate(PORT,LIKE,"443").and(Predicate(SERVICE,LIKE,'http')))

@header {
package net.vdbaan.issuefinder.parser;
}

expr: expr AND expr              # andExpr
    | expr OR expr               # orExpr
    | LPAREN expr RPAREN         # enclosedExpr
    | NOT expr                   # notExpr
    | column operator STRING     # assign
    | column rangeOperator RANGE # range
    | column groupOperator GROUP # group
    | NOT column                 # notColumn
    ;

LPAREN: '(';
RPAREN: ')';
AND   : '&&' | 'AND';
OR    : '||' | 'OR';
NOT   : '!';

 //SCANNER("SCANNER"),IP("IP"),PORT("PORT"),SERVICE("SERVICE"),RISK("RISK"),EXPLOITABLE("EXPLOITABLE"),DESCRIPTION("DESCRIPTION")
column
    : SCANNER
    | IP
    | PORT
    | SERVICE
    | RISK
    | EXPLOITABLE
    | DESCRIPTION
    | PLUGIN
    ;

groupOperator: IN;
rangeOperator: BETWEEN;

operator
    : '=='
    | '!='
    | '<'
    | '<='
    | '>'
    | '=>'
    | '~='
    | LIKE
    ;

IP: I P;
SCANNER: S C A N N E R;
PORT: P O R T;
SERVICE: S E R V I C E;
RISK: R I S K;
EXPLOITABLE: E X P L O I T A B L E;
DESCRIPTION: D E S C R I P T I O N;
PLUGIN: P L U G I N;

IN: I N;
LIKE: L I K E;
BETWEEN: B E T W E E N;

GROUP: '[' STRING (',' STRING)+ ']';
RANGE: LPAREN STRING ',' STRING RPAREN;
STRING
    : DQUOTE ~["]+? DQUOTE
    | SQUOTE ~[']+? SQUOTE
    | CHAR
    ;

fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];


SQUOTE: '\'' -> skip;
DQUOTE: '"' -> skip;
//CHAR : ~[ \t\r\n"']+ ; // // match any chars except space tab, newline, double and single quote

CHAR: [a-zA-Z/\\.0-9:;]+;
WS : [ \t\n\r]+ -> skip ; // skip spaces, tabs, newlines, \r (Windows)