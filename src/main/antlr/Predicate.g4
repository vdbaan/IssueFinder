/**
 * Copyright (C) 2017 S. van der Baan

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
//    | NOT expr                   # notExpr
    | LPAREN expr RPAREN         # enclosedExpr
    | column operator STRING     # assign
    | column rangeOperator GROUP # range
    | column groupOperator GROUP # group
    | (NOT)? EXPLOITABLE         # exploitableExpr
    ;

column
    : SCANNER
    | IP
    | PORT
    | STATUS
    | PROTOCOL
    | SERVICE
    | RISK
    | EXPLOITABLE
    | DESCRIPTION
    | PLUGIN
    | CVSS
    | HOSTNAME
    ;

groupOperator: IN;
rangeOperator: BETWEEN;

operator
    : '=='
    | '!='
    | '<'
    | '<='
    | '>'
    | '>='
    | '~='
    | (NOT)? LIKE
    ;


LPAREN: '(';
RPAREN: ')';
AND   : '&&' | 'AND';
OR    : '||' | 'OR';
NOT   : '!';

IP: I P;
SCANNER: S C A N N E R;
PORT: P O R T;
STATUS: S T A T U S;
PROTOCOL: P R O T O C O L;
SERVICE: S E R V I C E;
RISK: R I S K;
EXPLOITABLE: E X P L O I T A B L E;
DESCRIPTION: D E S C R I P T I O N;
PLUGIN: P L U G I N;
HOSTNAME: H O S T N A M E;
CVSS: C V S S;

IN: I N;
LIKE: L I K E;
BETWEEN: B E T W E E N;

GROUP: LPAREN WS* STRING WS* (',' WS* STRING WS*)+ RPAREN;

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

CHAR: [a-zA-Z/\\.0-9:;]+;
WS : [ \t\n\r]+ -> skip ; // skip spaces, tabs, newlines, \r (Windows)