// Generated from retreet.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link retreetParser}.
 */
public interface retreetListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link retreetParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(retreetParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(retreetParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(retreetParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(retreetParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(retreetParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(retreetParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code calledfuncwithaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 */
	void enterCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code calledfuncwithaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 */
	void exitCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code calledfuncnoaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 */
	void enterCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code calledfuncnoaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 */
	void exitCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(retreetParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(retreetParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(retreetParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(retreetParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#ifstmt}.
	 * @param ctx the parse tree
	 */
	void enterIfstmt(retreetParser.IfstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#ifstmt}.
	 * @param ctx the parse tree
	 */
	void exitIfstmt(retreetParser.IfstmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funccallwithaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 */
	void enterFunccallwithaexpr(retreetParser.FunccallwithaexprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funccallwithaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 */
	void exitFunccallwithaexpr(retreetParser.FunccallwithaexprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funccallnoaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 */
	void enterFunccallnoaexpr(retreetParser.FunccallnoaexprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funccallnoaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 */
	void exitFunccallnoaexpr(retreetParser.FunccallnoaexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#assgn}.
	 * @param ctx the parse tree
	 */
	void enterAssgn(retreetParser.AssgnContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#assgn}.
	 * @param ctx the parse tree
	 */
	void exitAssgn(retreetParser.AssgnContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#lexpr}.
	 * @param ctx the parse tree
	 */
	void enterLexpr(retreetParser.LexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#lexpr}.
	 * @param ctx the parse tree
	 */
	void exitLexpr(retreetParser.LexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#bexpr}.
	 * @param ctx the parse tree
	 */
	void enterBexpr(retreetParser.BexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#bexpr}.
	 * @param ctx the parse tree
	 */
	void exitBexpr(retreetParser.BexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#aexpr}.
	 * @param ctx the parse tree
	 */
	void enterAexpr(retreetParser.AexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#aexpr}.
	 * @param ctx the parse tree
	 */
	void exitAexpr(retreetParser.AexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#locvars}.
	 * @param ctx the parse tree
	 */
	void enterLocvars(retreetParser.LocvarsContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#locvars}.
	 * @param ctx the parse tree
	 */
	void exitLocvars(retreetParser.LocvarsContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#rtnexpr}.
	 * @param ctx the parse tree
	 */
	void enterRtnexpr(retreetParser.RtnexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#rtnexpr}.
	 * @param ctx the parse tree
	 */
	void exitRtnexpr(retreetParser.RtnexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#intvars}.
	 * @param ctx the parse tree
	 */
	void enterIntvars(retreetParser.IntvarsContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#intvars}.
	 * @param ctx the parse tree
	 */
	void exitIntvars(retreetParser.IntvarsContext ctx);
	/**
	 * Enter a parse tree produced by {@link retreetParser#funcid}.
	 * @param ctx the parse tree
	 */
	void enterFuncid(retreetParser.FuncidContext ctx);
	/**
	 * Exit a parse tree produced by {@link retreetParser#funcid}.
	 * @param ctx the parse tree
	 */
	void exitFuncid(retreetParser.FuncidContext ctx);
}