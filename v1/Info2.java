/*
 * 6. Blocks(f)
 */

public class Info2 extends retreetBaseVisitor<String> {
	static int func_count=0; 
	
	public String visitMain(retreetParser.MainContext ctx) {
		String funcid = "main";
		func_count++;
		System.out.println("function "+func_count +": " +funcid);
		return visitChildren(ctx);
	}
	public String visitBlock(retreetParser.BlockContext ctx) {
		String block = ctx.getText();
		System.out.println("block: " + block);
		return visitChildren(ctx);
	}

	public String visitCalledfuncwithaexpr(retreetParser.CalledfuncwithaexprContext ctx) {
		String funcid = ctx.funcid().getText();
		func_count++;
		System.out.println();
		System.out.println("function "+func_count +": " +funcid);
		return visitChildren(ctx);
	}

	public String visitCalledfuncnoaexpr(retreetParser.CalledfuncnoaexprContext ctx) {
		String funcid = ctx.funcid().getText();
		func_count++;
		System.out.println();
		System.out.println("function "+func_count +": " +funcid);
		return visitChildren(ctx);
	}
}