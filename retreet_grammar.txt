grammar retreet;                         

prog : func+ ;                                  

func : main NEWLINE*
     | (calledfunc NEWLINE*)+
     ;

main : 'Main' '(' locvars ')'NEWLINE*'{' NEWLINE*stmt '}';

calledfunc : funcid '(' lexpr',' aexpr ')'NEWLINE* '{' NEWLINE*stmt '}'
           | funcid '(' lexpr ')'NEWLINE* '{'NEWLINE* stmt '}'
           ;
           
stmt : ( block SEMICOLON NEWLINE?)+ 
     | (ifstmt NEWLINE*)+
     | ('{' stmt ':' stmt '}'NEWLINE*)+
     | '{' NEWLINE* stmt NEWLINE*'}'NEWLINE*
     ;

block : funccall+
      | assgn+ 
      ;

ifstmt : 'if' '(' bexpr ')' NEWLINE*stmt 'else' NEWLINE*stmt ;

funccall : intvars'='funcid'('lexpr','aexpr')'
         | intvars'='funcid'('lexpr')' 
         ;

assgn : locvars'.'intvars'='aexpr
      | intvars'='aexpr
      | 'return' returnexpr
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

returnexpr : intvars
           | '('intvars')'
           | '('intvars','intvars')'
           | '0'
           ;

intvars : intvars'+'intvars
        | intvars'+'INT
        | ID
        | INT
        ;
        
funcid : ID ;

INT : [0-9]+;
ID : [a-zA-Z]+;                       
NEWLINE : '\r'? '\n';
WS : [ \t]+ ->skip;
SEMICOLON : ';';

