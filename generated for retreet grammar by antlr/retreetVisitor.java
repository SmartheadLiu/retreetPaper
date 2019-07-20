// Generated from retreet.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link retreetParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface retreetVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link retreetParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(retreetParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(retreetParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(retreetParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code calledfuncwithaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code calledfuncnoaexpr}
	 * labeled alternative in {@link retreetParser#calledfunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(retreetParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(retreetParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#ifstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfstmt(retreetParser.IfstmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funccallwithaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunccallwithaexpr(retreetParser.FunccallwithaexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funccallnoaexpr}
	 * labeled alternative in {@link retreetParser#funccall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunccallnoaexpr(retreetParser.FunccallnoaexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#assgn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssgn(retreetParser.AssgnContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#lexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLexpr(retreetParser.LexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#bexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBexpr(retreetParser.BexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#aexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAexpr(retreetParser.AexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#locvars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocvars(retreetParser.LocvarsContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#rtnexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRtnexpr(retreetParser.RtnexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#intvars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntvars(retreetParser.IntvarsContext ctx);
	/**
	 * Visit a parse tree produced by {@link retreetParser#funcid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncid(retreetParser.FuncidContext ctx);
}