grammar Retreet;                         

prog 
      : (function)+ 
      ;                                  

id
      : IDENTIFIER
      ;

function 
      : main 
      | func
      ;

main 
      : 'main' '(' id ')''{' stmt+ '}'
      ;

func 
      : id '(' param_list ')' '{' stmt+ '}'
      ;               

param_list 
      : id   // loc variable
      | id param_tail   // loc & int variables
      ;

param_tail
      : ',' id param_tail   // int variable
      | // empty
      ;
           
stmt 
      : block_withid
      | if_stmt
      | '[' block_withid ':' block_withid ']' // parallel
      ;

block_withid
      : block '//' INT
      ;
     
block 
      : call
      | assgn_list
      ;

call 
      : id '(' arg_list ')' ';'
      ;

arg_list
      : lexpr arg_list_tail
      ;

arg_list_tail
      : ',' aexpr arg_list_tail
      | // empty
      ;

assgn_list
      : assgn+
      ;

assgn 
      : field '=' aexpr ';'
      | id '=' aexpr ';'
      | 'return' ';'
      ;

if_stmt 
      :if_part else_part;

if_part 
      : 'if' '(' bexpr ')' '{' stmt+ '}'
      ;

else_part 
      : 'else' '{' stmt+ '}'
      ;

lexpr 
      : id
      | lexpr '.' dir
      ;

dir
      : 'left'
      | 'right'
      ;

aexpr 
      : INT
      | id
      | field
      | aexpr aop aexpr
      ;

aop
      : '+'
      | '-'
      ;

field
      : lexpr '.' id
      ;

bexpr
      : lit bexpr_suffix
      ;

bexpr_suffix
      : '&&' lit bexpr_suffix
      | '||' lit bexpr_suffix
      | // empty
      ;

lit
      : '!' basic_cond
      | basic_cond
      ;

basic_cond
      : aexpr compop aexpr
      | lexpr eqop 'nil'
      | 'true'
      | 'false'
      ;

compop
      : '>'
      | '<'
      | '>='
      | '<='
      | eqop
      ;

eqop
      : '=='
      | '!='
      ;

WHITESPACE
      : ( ' ' | '\t' | '\r' | '\n' )+ -> skip
      ;

KEYWORD
      : 'main'
      | 'if'
      | 'else'
      | 'nil'
      | 'return'
      | 'left'
      | 'right'
      | 'true'
      | 'false'
      ;

IDENTIFIER 
      : [a-zA-Z]+
      ;

INT
      : [0-9]+
      ;
