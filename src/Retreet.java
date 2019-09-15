import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Retreet {
	public static void main( String[] args) throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		RetreetLexer lexer = new RetreetLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RetreetParser parser = new RetreetParser(tokens);

		Logger logger = Logger.getLogger("main");

		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		ParseTree tree = null;
		try{
			tree = parser.prog();
			logger.info("Accepted");
		} catch(Exception ex) {
			logger.info("Not Accepted");
			return;
		}

		ParseTreeWalker walker = new ParseTreeWalker();
        RetreetExtractor listener = new RetreetExtractor();
        walker.walk(listener, tree);

        List<String> funcs = listener.getFuncs();
        List<Block> blocks = listener.getBlocks();
        Set<String> calls = listener.getCalls();
        Set<String> noncalls = listener.getNoncalls();
        Map<String, List<String>> funcBlock = listener.getFuncBlock();

        System.out.println("Print out AllFuncs:");
        for (String func : funcs) {
        	System.out.println("Function: " + func);
        }
        System.out.println();

        System.out.println("Print out AllBlocks:");
        for (Block block : blocks) {
        	System.out.println("Block id: " + block.getId() + " Text: " + block.getText());
        }
        System.out.println();

        System.out.println("Print out AllCalls:");
        for (String call : calls) {
        	System.out.println("Call block id: " + call);
        }
        System.out.println();

        System.out.println("Print out AllNoncalls:");
        for (String noncall : noncalls) {
        	System.out.println("Noncall block id: " + noncall);
        }
        System.out.println();

        System.out.println("Print out mapping of functions to blocks:");
        for (String func : funcBlock.keySet()) {
        	List<String> blockList = funcBlock.get(func);
        	System.out.println("Current function: " + func);
        	for (String b : blockList) {
        		System.out.println("	block: " + b);
        	}
        }
        System.out.println();

	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException err){
		throw err;
	}	
}	