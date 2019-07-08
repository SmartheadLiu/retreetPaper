import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class Test {

	public static void main(String[] args) throws Exception{
		// TODO 自动生成的方法存根
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		retreetLexer lexer = new retreetLexer(input);
		CommonTokenStream tokens= new CommonTokenStream(lexer);
		retreetParser parser = new retreetParser(tokens);
		ParseTree tree= parser.prog();
		System.out.println(tree.toStringTree(parser));		
	}

}
