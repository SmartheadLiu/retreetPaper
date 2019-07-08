import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
public class Test {

	public static void main(String[] args) throws Exception{
		// TODO 自动生成的方法存根
		//新建CharStream，从标准输入读入
		//ANTLRInputStream input = new ANTLRInputStream(System.in);
		
		//改为直接读入测试文件"test.retreet"
		ANTLRInputStream input = new ANTLRInputStream(readToString("test.retreet"));
		
		//新建词法分析器，处理输入的CharStream
		retreetLexer lexer = new retreetLexer(input);
		
		//新建词法符号缓冲区，存储词法符号
		CommonTokenStream tokens= new CommonTokenStream(lexer);
		
		//新建语法分析器，处理缓冲区中的tokens
		retreetParser parser = new retreetParser(tokens);
		
		//针对prog规则开始语法分析
		ParseTree tree= parser.prog();
		
		//打印语法树
		System.out.println(tree.toStringTree(parser));
		
		//新建遍历器
		ParseTreeWalker walker = new ParseTreeWalker();
		//walker.walk(new Info(), tree); info.class still need debug
		//System.out.println();
	}
	/*
	 * 读入测试文件test.retreet
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
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return new String(filecontent);  
    }	
}


