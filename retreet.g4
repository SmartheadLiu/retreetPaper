grammar retreet;                         

prog : func+ ;                                  

func : main NEWLINE*
     | (calledfunc NEWLINE*)+
     ;

main : 'main' '(' locvars ')'NEWLINE*'{' NEWLINE*stmt '}';

calledfunc : funcid '(' lexpr',' aexpr ')'NEWLINE* '{' NEWLINE*stmt '}'                                #calledfuncwithaexpr
           | funcid '(' lexpr ')'NEWLINE* '{'NEWLINE* stmt '}'                                         #calledfuncnoaexpr
           ;
           
stmt : ( block SEMICOLON NEWLINE?)+ 
     | (ifstmt NEWLINE*)+
     | ('{' stmt ':' stmt '}'NEWLINE*)+
     | '{' NEWLINE* stmt NEWLINE*'}'NEWLINE*
     ;

block : funccall+
      | assgn+ 
      ;

ifstmt :ifpart NEWLINE* elsepart;

ifpart : 'if''(' bexpr ')'NEWLINE*stmt;

elsepart : 'else' NEWLINE* stmt;

funccall : intvars'='funcid'('lexpr','aexpr')'                                                      #funccallwithaexpr
         | intvars'='funcid'('lexpr')'                                                              #funccallnoaexpr
         ;

assgn : locvars'.'intvars'='aexpr
      | intvars'='aexpr
      | 'return' rtnexpr
      ;
      
lexpr : locvars
      | lexpr '.' locvars
      ;

bexpr : 'true'
      | aexpr'>''0'
      | lexpr '==''nil'
      | '!'bexpr
      | bexpr'&&'bexpr
      ;

aexpr : '0' 
      | '1'
      | aexpr ('+'|'-') aexpr
      | intvars
      | locvars '.' intvars
      ;

locvars : ID;

rtnexpr : '0' 
        | '1'
        | rtnexpr ('+'|'-') rtnexpr
        | intvars
        ;
        
intvars : INT
        | ID
        ;
        
funcid : ID ;

INT : [0-9]+;
ID : [a-zA-Z]+;                       
NEWLINE : '\r'? '\n';
WS : [ \t]+ ->skip;
SEMICOLON : ';';
HH : [\r\n]+ ->skip;
