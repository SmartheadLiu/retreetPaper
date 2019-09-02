grammar retreet;                         

prog : func+ ;                                  

func : main 
     | calledfunc+
     ;

main : 'main' '(' locvars ')''{' stmt '}';

calledfunc : funcid '(' lexpr(',' aexpr)* ')' '{' stmt '}';               
           
stmt : block+
     | ifstmt+
     |'{' block ':' block '}'
     |'{'stmt (';')*'}'
     ;
     
block : funccall
      | assgn+ 
      ;

ifstmt :ifpart  elsepart;

ifpart : 'if' '(' bexpr ')' ('{')* stmt ('}')* ;

elsepart : 'else' '{' stmt '}'  ;

funccall : intvars'='funcid'('lexpr (','aexpr)* ')' SEMICOLON               
         ;

assgn : locvars'.'intvars'='aexpr SEMICOLON
      | intvars'='aexpr  SEMICOLON
      | 'return' rtnexpr SEMICOLON
      ;
      
lexpr : locvars
      | lexpr '.' locvars
      ;

bexpr : aexpr('>'|'<'|'>='|'<='|'=='|'!=')'0'
      | lexpr ('=='|'!=')'nil'
      | '!'bexpr
      | bexpr'&&'bexpr
      |'true'
      ;

aexpr : '0' 
      | '1'
      | aexpr ('+'|'-') aexpr
      | intvars
      | locvars '.' intvars
      ;

locvars : ID;

rtnexpr : INT
        | rtnexpr ('+'|'-') rtnexpr
        | intvars
        ;
        
intvars : ID;
        
funcid : ID;

INT : [0-9]+;
ID : [a-zA-Z]+;                       
WS : [ \r\n\t]+ ->skip;
SEMICOLON : ';';
