import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO 自动生成的方法存根
		// 新建CharStream，从标准输入读入
		// ANTLRInputStream input = new ANTLRInputStream(System.in);

		// 改为直接读入测试文件"test.retreet"
		ANTLRInputStream input = new ANTLRInputStream(readToString("test.retreet"));

		// 新建词法分析器，处理输入的CharStream
		retreetLexer lexer = new retreetLexer(input);

		// 新建词法符号缓冲区，存储词法符号
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// 新建语法分析器，处理缓冲区中的tokens
		retreetParser parser = new retreetParser(tokens);

		// 针对prog规则开始语法分析
		ParseTree tree = parser.prog();

		// 打印语法树
		// System.out.println(tree.toStringTree(parser));
		System.out.println();

		// 新建遍历器
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new Info(), tree);

		/* show AllFuncs、AllParams、AllBlocks、AllCalls、AllNoCalls */
		System.out.println("\n<---- 1. AllFuncs " + Info.funcs_count + " ---->");
		for (int i = 0; i < Info.allfuncs.length; i++) {
			if (Info.allfuncs[i] != null) {
				System.out.println("function " + (i + 1) + ": " + Info.allfuncs[i]);
			} else
				break;
		}
		System.out.println("\n<---- 2. AllParams " + Info.params_count + " ---->");
		for (int i = 0; i < Info.allparams.length; i++) {
			if (Info.allparams[i] != null) {
				System.out.println("Function Parameter " + (i + 1) + ": " + Info.allparams[i]);
			} else
				break;
		}
		System.out.println("\n<---- 3. AllBlocks " + Info.blocks_count + " ---->");
		for (int i = 0; i < Info.allblocks.length; i++) {
			if (Info.allblocks[i] != null) {
				System.out.println("Block " + (i + 1) + ": " + Info.allblocks[i]);
			} else
				break;
		}
		System.out.println("\n<---- 4. AllCalls " + Info.calls_count + " ---->");
		for (int i = 0; i < Info.allcalls.length; i++) {
			if (Info.allcalls[i] != null) {
				System.out.println("Call " + (i + 1) + ": " + Info.allcalls[i]);
			} else
				break;
		}
		System.out.println("\n<---- 5. AllNoCalls " + Info.assigns_count + " ---->");
		for (int i = 0; i < Info.allnocalls.length; i++) {
			if (Info.allnocalls[i] != null) {
				System.out.println("NoCall block " + (i + 1) + ": " + Info.allnocalls[i]);
			} else
				break;
		}
		
		//启动visitor
		System.out.println("\n<---- 6. AllNoCalls  ---->");
		Info2 info_f=new Info2();
		String result= info_f.visit(tree);
		
		
		System.out.println("\n<---- 8. Nodes in the Tree  ---->");
		Nodes nodes=new Nodes();
		String node_ressult= nodes.visit(tree);
	}
	
	

	/*
	 * read programme from file "test.retreet"
	 */
	public static String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.prin/tStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(filecontent);
	}
}
